package com.example.demo.producer;

import com.example.demo.crawler.ConsultaMercadoLivre;
import com.example.demo.crawler.ConsultaMercadoLivreHtmlUnit;
import com.example.demo.models.Produto;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProducerProdutos {

    final static Logger logger = Logger.getLogger(ProducerProdutos.class);

    @Autowired
    ConsultaMercadoLivre consulta;

    @Autowired
    ConsultaMercadoLivreHtmlUnit consultaHtmlUnit;


    @PostConstruct
    public void roda() throws InterruptedException, IOException {
        //ConsultaMercadoLivre consulta ;

        List<Produto> listaProduto = new ArrayList<>();

        //consultaHtmlUnit.filtroPesquisa("Kindle");
        //listaProduto = consultaHtmlUnit.consultaProdutosHtmlUnit();
        listaProduto = consulta.consultaProdutos("Kindle");

        for (Produto produto : listaProduto) {

            logger.info("---------------------------------------");
            logger.info("Descrição : " + produto.getDescricao());
            logger.info("URL Imagem : " + produto.getUrlImagem());
            logger.info("Valor : " + produto.getValor());
            logger.info("---------------------------------------");

        }
    }
}
