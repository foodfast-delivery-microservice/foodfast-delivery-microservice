package com.example.demo.infracstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Gửi tin nhắn đến một queue tên là "test-queue"
     */
    public void sendMessage(String message) {
        // Tham số 1: Tên Exchange (để trống là dùng default exchange)
        // Tham số 2: Tên Queue (hoặc routing key)
        // Tham số 3: Nội dung tin nhắn
        rabbitTemplate.convertAndSend("test-queue", message);
        System.out.println(">>> ĐÃ GỬI TIN NHẮN: " + message);
    }
}
