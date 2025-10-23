package com.example.demo.infracstructor.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Phải khớp tên với bên gửi
    public static final String ORDER_EXCHANGE = "order_exchange";

    // Tên queue để nhận việc
    public static final String STOCK_CHECK_QUEUE = "stock_check_queue";

    // 1. Khai báo Exchange (phải giống hệt bên Order Service)
    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    // 2. Khai báo Queue (hòm thư)
    @Bean
    public Queue stockCheckQueue() {
        return new Queue(STOCK_CHECK_QUEUE);
    }

    // 3. Gắn kết (Binding)
    // "Tất cả event 'OrderCreated' trên 'order_exchange' -> hãy copy vào 'stock_check_queue'"
    @Bean
    public Binding binding(Queue stockCheckQueue, TopicExchange orderExchange) {
        return BindingBuilder
                .bind(stockCheckQueue)
                .to(orderExchange)
                .with("OrderCreated"); // <-- Phải khớp với event.getType()
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}