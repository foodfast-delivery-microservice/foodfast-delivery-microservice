package com.example.order_service.infrastructure.config;

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

    public static final String ORDER_EXCHANGE = "order_exchange";
    public static final String STOCK_RESERVED_QUEUE = "stock_reserved_queue";
    public static final String STOCK_FAILED_QUEUE = "stock_failed_queue";
    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    // Bean này giúp RabbitTemplate tự động chuyển Object/String thành JSON
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // --- Queues ---
    @Bean
    public Queue stockReservedQueue() {
        return new Queue(STOCK_RESERVED_QUEUE);
    }

    @Bean
    public Queue stockFailedQueue() {
        return new Queue(STOCK_FAILED_QUEUE);
    }

    // --- Bindings ---
    @Bean
    public Binding stockReservedBinding(Queue stockReservedQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(stockReservedQueue).to(orderExchange).with("StockReserved");
    }

    @Bean
    public Binding stockFailedBinding(Queue stockFailedQueue, TopicExchange orderExchange) {
        return BindingBuilder.bind(stockFailedQueue).to(orderExchange).with("StockFailed");
    }
}