package com.example.order_service.infrastructure;

import com.example.order_service.domain.model.EventStatus;
import com.example.order_service.domain.model.OutboxEvent;
import com.example.order_service.domain.repository.OutboxEventRepository;
import com.example.order_service.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxProcessor {

    private final OutboxEventRepository outboxEventRepository;
    private final RabbitTemplate rabbitTemplate; // Tiêm RabbitTemplate

    // Chạy định kỳ, ví dụ mỗi 5 giây
    @Scheduled(fixedRate = 5000)
    public void processOutboxEvents() {
        // 1. Tìm các event mới, giới hạn số lượng để xử lý (ví dụ 100)
        List<OutboxEvent> events = outboxEventRepository
                .findByStatusOrderByCreatedAtAsc(EventStatus.NEW /*, Pageable.ofSize(100)*/);
        if (!events.isEmpty()) {
            log.info("Found {} new outbox events to process...", events.size());
        }

        for (OutboxEvent event : events) {
            try {
                // 2. Publish sự kiện lên RabbitMQ
                // (Giả sử bạn có 1 exchange "order_exchange" và routing_key là event type)
                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.ORDER_EXCHANGE, // Tên exchange
                        event.getType(),      // Routing key (ví dụ: "OrderCreated")
                        event.getPayload()    // Nội dung (chuỗi JSON)
                );

                // Cập nhật status
                event.setStatus(EventStatus.PROCESSED);
                outboxEventRepository.save(event);

                log.info("Successfully processed event {}", event.getId());
            } catch (Exception e) {
                log.error("Failed to publish event {}: {}", event.getId(), e.getMessage());
                event.setStatus(EventStatus.FAILED); // Đánh dấu FAILED để thử lại sau
                outboxEventRepository.save(event);
            }
        }
    }

    // Phải chạy trong một transaction MỚI
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateEventStatus(OutboxEvent event, EventStatus status) {
        event.setStatus(status);
        outboxEventRepository.save(event);
    }
}
