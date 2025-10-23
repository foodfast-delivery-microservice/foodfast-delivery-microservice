package com.example.common.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// Dùng để gửi phản hồi (thất bại)
@Data
@AllArgsConstructor
public class StockFailedEvent {
    private Long orderId;
    private String reason;
}