package com.example.demo;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class MensageriaConfig {

    @Value("${spring.rabbitmq-config.queues.queue-consumer}")
    private String queueNameConsumer;
}
