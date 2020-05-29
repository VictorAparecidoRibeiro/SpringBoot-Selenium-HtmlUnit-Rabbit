package com.example.demo.consumer;

import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@Service
public class ConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerService.class);

    @Autowired
    private ConsumerPesquisaProdutos consumerPesquisaProdutos;

    @RabbitListener(queues = {"${spring.rabbitmq-config.queues.queue-consumer}"})
    public void run(String jsonConsultaFila) {

        LOGGER.info("Início da execução do consumer");

        try {
            consumerPesquisaProdutos.run(jsonConsultaFila);
        } catch (Exception ex) {
            LOGGER.warn("Falha na execução do consumer " +  ex);
        }

        LOGGER.info("Fim da execução do consumer");
    }
}
