package com.example.common.interfaces.dto;

import lombok.Data;

// Dùng để hứng payload từ OrderCreated
@Data // (Lombok)
public class OrderCreatedEventPayload {
    private Long orderId;
    private String orderCode;
    private Long userId;
    // ... (Thêm các trường khác, ví dụ list sản phẩm nếu cần)
}
