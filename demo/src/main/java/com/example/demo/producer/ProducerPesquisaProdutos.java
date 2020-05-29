package com.example.demo.producer;

import com.example.demo.service.RabbitService;
import com.example.demo.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProducerPesquisaProdutos {

    private static final Logger logger = LoggerFactory.getLogger(ProducerPesquisaProdutos.class);

    @Value("${spring.rabbitmq-config.queues.queue-consumer}")
    String queue;

    @Autowired
    private RabbitService rabbitService;


    @PostConstruct
    public void run() {
        try {
            logger.info("Começando a executar o producer");

            List<String> listaParaConsulta = new ArrayList<>();

            listaParaConsulta.add("Kindle");
            listaParaConsulta.add("Camiseta MoonSpell");
            listaParaConsulta.add("Camiseta Nick Cave");
            listaParaConsulta.add("Box quadrinhos HellBoy");


            for (String consulta : listaParaConsulta) {

                rabbitService.adicionaPesquisaNaFila(JsonUtil.writeJson(consulta));
                logger.info("Enviando consuta -> " + consulta);
            }

            logger.info("Consultas adicionadas na fila");
        } catch (Exception ex) {
            logger.error("Erro no producer de pesquisa", ex);
        }
    }
}
