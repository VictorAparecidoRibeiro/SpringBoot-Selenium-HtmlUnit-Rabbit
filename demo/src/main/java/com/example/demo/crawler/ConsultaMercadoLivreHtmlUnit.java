package com.example.demo.crawler;

import com.example.demo.models.Produto;
import com.example.demo.producer.ProducerProdutos;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConsultaMercadoLivreHtmlUnit{

    private static final String URL = "https://lista.mercadolivre.com.br/";

    private static final String QTDE_ITENS_BY = "//div[@class='quantity-results']";

    private static final String LISTA_ITENS_PESQUISADOS_POR_PAGINA = "//*[@id='searchResults']/li";

    private static final String VALOR_PARTE_INTEIRA_FRACAO = ".//span[@class='price__fraction']";

    private static final String VALOR_PARTE_INTEIRA_MOEDA = ".//span[contains(text(), 'R$')]";

    private static final String BTN_NEXT = "//a[span[contains(text(), 'Próxima')]]";

    private static final String PARTE_DECIMAL_BY  = ".//span[@class='price__decimals']";
    private static final String DESCRICAO_BY = ".//span[@class='main-title']";
    private static final String IMAGEM_BY = ".//div[@class='images-viewer']//img";


    private WebClient client;
    private HtmlPage page;


    final static Logger logger = Logger.getLogger(ProducerProdutos.class);


    public ConsultaMercadoLivreHtmlUnit() {
        client = new WebClient(BrowserVersion.CHROME);
        client.getOptions().setPrintContentOnFailingStatusCode(false);
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);
    }

    public void filtroPesquisa(String consulta) throws IOException {
        logger.info("Consultando ..." + consulta);
        page = client.getPage(URL + consulta);
    }

    public List<Produto> consultaProdutosHtmlUnit() throws IOException {

        String titulo = page.getTitleText();
        int quantidadeTotalItens = Integer.parseInt(((HtmlDivision) page.getFirstByXPath(QTDE_ITENS_BY)).getTextContent().replaceAll("\\D+", ""));

        logger.info("Quantidade total de itens : " + quantidadeTotalItens);


        List<Produto> listaRetorno = new ArrayList<>();
        boolean passouPeloMenosUmaVez = false;

        do {
            if (passouPeloMenosUmaVez && page.getByXPath(BTN_NEXT).size() > 0) {
                logger.info("Indo para próxima página");
                page = ((HtmlAnchor) page.getFirstByXPath(BTN_NEXT)).click();
            }

            if (!passouPeloMenosUmaVez) {
                passouPeloMenosUmaVez = true;
            }


            page.executeJavaScript("window.scrollTo(0, document.body.scrollHeight)");

            List<HtmlListItem> produtos = page.getByXPath(LISTA_ITENS_PESQUISADOS_POR_PAGINA);

            for (HtmlListItem produto : produtos) {

                Produto produtoRetorno = new Produto();

                HtmlElement parteDecimal = produto.getFirstByXPath(VALOR_PARTE_INTEIRA_FRACAO);

                HtmlElement parteDaMoeda = produto.getFirstByXPath(VALOR_PARTE_INTEIRA_MOEDA);

                String valorConcatenado = parteDecimal != null ? parteDecimal.getTextContent() : parteDaMoeda != null ? parteDaMoeda.getTextContent().replaceAll("\\D", "") : null;
                HtmlElement spanParteDecimal = produto.getByXPath(PARTE_DECIMAL_BY).size() > 0 ? (HtmlElement) produto.getFirstByXPath(PARTE_DECIMAL_BY) : null;
                BigDecimal valor = new BigDecimal(valorConcatenado + (spanParteDecimal != null ? "." + spanParteDecimal.getTextContent() : ""));
                String descricao = ((HtmlElement) produto.getFirstByXPath(DESCRICAO_BY)).getTextContent();


                String urlImagem = ((HtmlImage) produto.getFirstByXPath(IMAGEM_BY)).getAttribute("src");



                produtoRetorno.setDescricao(descricao);
                produtoRetorno.setUrlImagem(urlImagem);
                produtoRetorno.setValor(Double.parseDouble(valor.toString()));

                listaRetorno.add(produtoRetorno);


            }
        } while (page.getByXPath(BTN_NEXT).size() > 0);

        return listaRetorno;
    }



    public void clientClose() {
        client.close();
        client = null;
    }
}
