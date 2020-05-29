package com.example.demo.crawler;

import com.example.demo.models.Produto;
import com.example.demo.producer.ProducerProdutos;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static javafx.scene.input.KeyCode.L;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

@Service
public class ConsultaMercadoLivre{

    final static Logger LOGGER = Logger.getLogger(ConsultaMercadoLivre.class);

    String XPATH_BTN_PAGINA = "//*[contains(@class, 'andes-pagination__button andes-pagination__button--next')]";



    public List<Produto> consultaProdutos(String descricaoProduto) throws InterruptedException {

        List<Produto> listaProdutosRetorno = new ArrayList<>();

        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
        ChromeOptions chromeOptions = new ChromeOptions();

        WebDriver driver = new ChromeDriver();

        long tempo = 10;

        WebDriverWait wait = new WebDriverWait(driver, 50);
        try {
            driver.get("https://www.mercadolivre.com.br/");
            driver.findElement(By.className("nav-search-input")).sendKeys(descricaoProduto + Keys.ENTER);
            List<WebElement> webElementsResult = new ArrayList<>();

            WebElement quantidadeTotalResultados = driver.findElement(new By.ByClassName("quantity-results"));

            LOGGER.info("Quantidade total de resultados : " + quantidadeTotalResultados.getText());


            Boolean peloMenosUma = false;

            do {

                if (peloMenosUma && driver.findElements(By.xpath(XPATH_BTN_PAGINA)).size() > 0) {
                    driver.findElement(By.xpath(XPATH_BTN_PAGINA)).click();
                }

                if (!peloMenosUma) {
                    peloMenosUma = true;
                }

                webElementsResult = driver.findElements(By.xpath("//*[contains(@class,'rowItem') ]"));

                LOGGER.info("Quantidade na página atual " + webElementsResult.size());

                for (WebElement elemento : webElementsResult) {

                    Produto produto = new Produto();

                    produto.setDescricao(elemento.findElement(new By.ByClassName("main-title")).getAttribute("innerText"));
                    produto.setUrlImagem(elemento.findElement(new By.ByClassName("images-viewer")).getAttribute("item-url"));

                    produto.setMoeda(elemento.findElement(new By.ByClassName("price__symbol")).getAttribute("innerText"));

                    produto.setValor(Double.parseDouble(elemento.findElement(new By.ByClassName("price__fraction")).getAttribute("innerText")));


                    listaProdutosRetorno.add(produto);

                }

                LOGGER.info("Quantidade na lista de retorno " + listaProdutosRetorno.size());

            }while (driver.findElements(By.xpath(XPATH_BTN_PAGINA)).size()>0);





            return listaProdutosRetorno;
        } finally {
            driver.quit();
        }
    }



}