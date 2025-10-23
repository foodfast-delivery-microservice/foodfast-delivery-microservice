package com.example.demo.infracstructor;

import com.example.common.interfaces.dto.OrderCreatedEventPayload;
import com.example.demo.infracstructor.config.RabbitMQConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final StockService stockService; // Lớp nghiệp vụ
    private final ObjectMapper objectMapper = new ObjectMapper(); // Để chuyển JSON

    @RabbitListener(queues = RabbitMQConfig.STOCK_CHECK_QUEUE)
    public void handleOrderCreated(String payload) { // Nhận payload (JSON string)
        try {
            log.info("<<< Received event: {}", payload);

            // Chuyển JSON string thành Object
            OrderCreatedEventPayload event = objectMapper
                    .readValue(payload, OrderCreatedEventPayload.class);

            // GỌI SERVICE NGHIỆP VỤ
            stockService.processStockReservation(event);

        } catch (Exception e) {
            log.error("Error processing OrderCreated event: {}", e.getMessage());
            // TODO: Gửi vào một queue lỗi (Dead Letter Queue) để xử lý thủ công
        }
    }
}