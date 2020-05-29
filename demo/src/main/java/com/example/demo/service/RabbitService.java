package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitService {

    private static final Logger logger = LoggerFactory.getLogger(RabbitService.class);

    private RabbitTemplate rabbitTemplate;
    private AmqpAdmin amqpAdmin;

    @Autowired
    public RabbitService(RabbitTemplate rabbitTemplate, AmqpAdmin amqpAdmin) {
        this.rabbitTemplate = rabbitTemplate;
        this.amqpAdmin = amqpAdmin;
    }

    @Value("${spring.rabbitmq-config.queues.queue-consumer}")
    String queue;

    public void adicionaPesquisaNaFila(String chaveParaPesquisaJson)
    {
        rabbitTemplate.convertAndSend(queue, chaveParaPesquisaJson);
    }
}
