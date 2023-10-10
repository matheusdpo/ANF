package br.com.ether.applications.anf.controller;

import br.com.ether.applications.anf.services.ANFService;
import br.com.ether.model.CredenciaisModel;
import br.com.ether.model.DadosDBModel;
import br.com.ether.repository.DataBase;
import br.com.ether.utilities.Aguardar;
import br.com.ether.utilities.DateUtility;
import br.com.ether.utilities.LogUtility;
import br.com.ether.utilities.SeleniumUtility;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.time.Duration;
import java.util.List;

@Controller
@EnableScheduling
@RequiredArgsConstructor
public class ControllerANF {
    private final SeleniumUtility seleniumUtility;

    private final DataBase dataBase;
    private final LogUtility logger;
    private final DateUtility dateUtility;
    private final ANFService anfService;
    private final Aguardar aguardar;

    @PostConstruct
    public void iniciar() throws InterruptedException {
        WebDriver driver = seleniumUtility.getDriver(false, false, "E:\\mapeamentoSelerium");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.manage().window().maximize();
        driver.get("https://www.nfse.gov.br/EmissorNacional/Notas/Visualizar/Index/35541022251335957000120000000000000123076587295936");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Inscricao")));
        WebElement usuario = driver.findElement(By.id("Inscricao"));
        usuario.sendKeys("51335957000120");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Senha")));
        WebElement senha = driver.findElement(By.id("Senha"));
        senha.sendKeys("#FazoL13");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/section/div/div/div[2]/div[2]/div[1]/div/form/div[3]/button")));
        WebElement botaoLogin = driver.findElement(By.xpath("/html/body/section/div/div/div[2]/div[2]/div[1]/div/form/div[3]/button"));
        botaoLogin.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnNovaNFSe")));
        WebElement novanf = driver.findElement(By.id("btnNovaNFSe"));
        novanf.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("DataCompetencia")));
        WebElement campoData = driver.findElement(By.id("DataCompetencia"));
        campoData.sendKeys(dateUtility.getToday("dd/MM/yyyy"), Keys.ENTER);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"pnlTomador\"]/div[1]/div/div")));
        WebElement tomadorLocal = driver.findElement(By.xpath("//*[@id=\"pnlTomador\"]/div[1]/div/div"));
        tomadorLocal.click();

        aguardar.segundos(4);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[contains(text(), 'Brasil')]")));
        WebElement brasil = driver.findElement(By.xpath("//label[contains(text(), 'Brasil')]"));
        brasil.click();

        aguardar.segundos(4);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Tomador_Inscricao")));
        WebElement cnpjoto = driver.findElement(By.id("Tomador_Inscricao"));
        cnpjoto.sendKeys("15149076000150");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btn_Tomador_Inscricao_pesquisar")));
        WebElement cnpjotoPesquisar = driver.findElement(By.id("btn_Tomador_Inscricao_pesquisar"));
        cnpjotoPesquisar.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Tomador_InscricaoMunicipal")));
        WebElement inscricaoMunicipal = driver.findElement(By.id("Tomador_InscricaoMunicipal"));
        inscricaoMunicipal.sendKeys("40028922");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Tomador_Telefone")));
        WebElement tomadorTel = driver.findElement(By.id("Tomador_Telefone"));
        tomadorTel.sendKeys("40028922");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Tomador_Email")));
        WebElement tomadorEmail = driver.findElement(By.id("Tomador_Email"));
        tomadorEmail.sendKeys("flamengo@gmail.com");


        aguardar.segundos(4);

        //wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"Tomador_EnderecoNacional_CEP\"]")));
        //WebElement tomadorCEP = driver.findElement(By.xpath("//*[@id=\"Tomador_EnderecoNacional_CEP\"]"));

        //driver.findElement(By.xpath("//div[@id=\"pnlTomadorEndereco\" and contains(@style, 'display: none;')]"));
        //tomadorCEP.sendKeys("");

        //wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"Tomador_EnderecoNacional_CEP\"]")));
        //WebElement cepPesquisar = driver.findElement(By.xpath("//*[@id=\"btn_Tomador_EnderecoNacional_CEP\"]"));
        //cepPesquisar.click();

        //wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Tomador_EnderecoNacional_Numero")));
        //WebElement numeroCasa = driver.findElement(By.id("Tomador_EnderecoNacional_Numero"));
        //numeroCasa.sendKeys("13");

        aguardar.segundos(3);


        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnAvancar")));
        WebElement avancarPessoas = driver.findElement(By.id("btnAvancar"));
        avancarPessoas.click();

        aguardar.segundos(5);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"pnlLocalPrestacao\"]/div/div/div[2]/div/span[1]/span[1]/span")));
        WebElement clickMunicipio = driver.findElement(By.xpath("//*[@id=\"pnlLocalPrestacao\"]/div/div/div[2]/div/span[1]/span[1]/span"));
        clickMunicipio.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/span/span/span[1]/input")));
        WebElement sendMunicipio = driver.findElement(By.xpath("/html/body/span/span/span[1]/input"));
        sendMunicipio.sendKeys("Taubaté");

        aguardar.segundos(5);

        driver.findElement(By.id("select2-LocalPrestacao_CodigoMunicipioPrestacao-results")).click();

        System.out.println("a");

        aguardar.segundos(5);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"pnlServicoPrestado\"]/div/div[1]/div/div/span[1]/span[1]/span")));
        WebElement clickCodigo = driver.findElement(By.xpath("//*[@id=\"pnlServicoPrestado\"]/div/div[1]/div/div/span[1]/span[1]/span"));
        clickCodigo.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/span/span/span[1]/input")));
        WebElement sendCodigo = driver.findElement(By.xpath("/html/body/span/span/span[1]/input"));
        sendCodigo.sendKeys("Análise e desenvolvimento de sistemas");

        aguardar.segundos(5);

        driver.findElement(By.xpath("//*[@id=\"select2-ServicoPrestado_CodigoTributacaoNacional-results\"]/li[1]")).click();

        System.out.println("funfou bb");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[contains(text(), 'Não')]")));
        WebElement clickServico = driver.findElement(By.xpath("//label[contains(text(), 'Não')]"));
        clickServico.click();

        aguardar.segundos(5);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ServicoPrestado_Descricao")));
        WebElement clickDescricao = driver.findElement(By.id("ServicoPrestado_Descricao"));
        clickDescricao.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ServicoPrestado_Descricao")));
        WebElement sendDescricao = driver.findElement(By.id("ServicoPrestado_Descricao"));
        sendDescricao.sendKeys("Clube de Regatas do Flamengo");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/form/div[7]/button")));
        WebElement avancarServico = driver.findElement(By.xpath("/html/body/div[1]/form/div[7]/button"));
        avancarServico.click();

        aguardar.segundos(2);


        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Valores_ValorServico")));
        WebElement clickValor = driver.findElement(By.id("Valores_ValorServico"));
        clickValor.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Valores_ValorServico")));
        WebElement sendValor = driver.findElement(By.id("Valores_ValorServico"));
        sendValor.sendKeys("000");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Valores_ValorServico")));
        WebElement clickNenhumValor = driver.findElement(By.xpath("//*[@id=\"pnlOpcaoParaMEI\"]/div/div/label"));
        clickNenhumValor.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/form/div[7]/button")));
        WebElement avancarValores = driver.findElement(By.xpath("/html/body/div[1]/form/div[7]/button"));
        avancarValores.click();

        System.out.println("Revisao da NFS-e");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnProsseguir")));
        WebElement emitirNota = driver.findElement(By.id("btnProsseguir"));
        emitirNota.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Baixar XML']")));
        WebElement baixarNota = driver.findElement(By.xpath("//span[text()='Baixar XML']"));
        baixarNota.click();

    }


    public void init() {
        dataBase.connectDB();
    }

    @Scheduled(cron = "0 0 12 30 * *")
    public void run() {
        logger.registraLog("=============================================");
        logger.registraLog("Iniciando ANF | Mês: " + dateUtility.getToday("MM/yyyy"));

        List<DadosDBModel> dadosDBModelList = dataBase.getCasos();

        if (!dadosDBModelList.isEmpty()) {
            List<CredenciaisModel> acessosModelList = dataBase.getAcessos();
            acessosModelList.forEach(e -> {
                if (e.getPlataforma().equalsIgnoreCase("NF"))
                    dadosDBModelList.forEach(d -> anfService.run(d, e));
            });
        }

        logger.registraLog("Finalizando ANF | Mês: " + dateUtility.getToday("MM/yyyy"));
    }

}
