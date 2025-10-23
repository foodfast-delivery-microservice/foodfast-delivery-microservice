package com.example.demo.infracstructor;

import com.example.common.interfaces.dto.OrderCreatedEventPayload;
import com.example.common.interfaces.dto.StockFailedEvent;
import com.example.common.interfaces.dto.StockReservedEvent;
import com.example.demo.infracstructor.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

    private final RabbitTemplate rabbitTemplate;
    // private final ProductRepository productRepository; // (để kiểm tra kho thật)

    public void processStockReservation(OrderCreatedEventPayload event) {

        // TODO: Logic kiểm tra kho thật
        // ... (productRepository.findById(item.getProductId())...)

        boolean isStockAvailable = true; // Giả sử là còn hàng

        if (isStockAvailable) {
            log.info("Stock reserved for order {}", event.getOrderId());
            // GỬI PHẢN HỒI: Thành công
            StockReservedEvent replyEvent = new StockReservedEvent(event.getOrderId());

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_EXCHANGE, // Gửi lại vào exchange CŨ
                    "StockReserved",               // ... với routing key MỚI
                    replyEvent
            );
        } else {
            log.warn("Stock FAILED for order {}", event.getOrderId());
            // GỬI PHẢN HỒI: Thất bại
            StockFailedEvent replyEvent = new StockFailedEvent(event.getOrderId(), "Out of stock");

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_EXCHANGE,
                    "StockFailed",                 // ... với routing key MỚI
                    replyEvent
            );
        }
    }
}
