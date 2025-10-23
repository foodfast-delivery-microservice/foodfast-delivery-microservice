package com.example.demo.infracstructor;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    /**
     * Lắng nghe bất kỳ tin nhắn nào trên "test-queue"
     */
    @RabbitListener(queues = "test-queue")
    public void handleMessage(String message) {
        // Xử lý tin nhắn ở đây
        System.out.println("<<< ĐÃ NHẬN TIN NHẮN: " + message);
    }
}
