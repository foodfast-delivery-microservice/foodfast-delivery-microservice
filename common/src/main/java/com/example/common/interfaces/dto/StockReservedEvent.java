package com.example.common.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// Dùng để gửi phản hồi (thành công)
@Data
@AllArgsConstructor
public class StockReservedEvent {
    private Long orderId;
}