package com.example.order_service.infrastructure;

import com.example.common.interfaces.dto.StockFailedEvent;
import com.example.common.interfaces.dto.StockReservedEvent;
import com.example.order_service.application.usecase.UpdateOrderStatusUseCase;
import com.example.order_service.domain.model.OrderStatus;
import com.example.order_service.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockEventConsumer {

    // Tiêm UseCase (hoặc Service) của chính Order Service
    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;

    @RabbitListener(queues = RabbitMQConfig.STOCK_RESERVED_QUEUE)
    public void handleStockReserved(StockReservedEvent event) {
        log.info("<<< Stock Reserved for order: {}", event.getOrderId());
        // Gọi nghiệp vụ: Cập nhật status
        updateOrderStatusUseCase.execute(
                event.getOrderId(),
                OrderStatus.CONFIRMED);
    }

    @RabbitListener(queues = RabbitMQConfig.STOCK_FAILED_QUEUE)
    public void handleStockFailed(StockFailedEvent event) {
        log.warn("<<< Stock Failed for order: {}. Reason: {}", event.getOrderId(), event.getReason());
        // Gọi nghiệp vụ: Cập nhật status
        updateOrderStatusUseCase.execute(event.getOrderId(), OrderStatus.CANCELLED);
    }
}