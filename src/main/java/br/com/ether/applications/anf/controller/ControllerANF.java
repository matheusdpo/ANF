package br.com.ether.applications.anf.controller;

import br.com.ether.applications.anf.services.ANFService;
import br.com.ether.model.CredenciaisModel;
import br.com.ether.model.DadosDBModel;
import br.com.ether.repository.DataBase;
import br.com.ether.utilities.DateUtility;
import br.com.ether.utilities.LogUtility;
import br.com.ether.utilities.SeleniumUtility;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.impl.auth.SystemDefaultCredentialsProvider;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.sql.SQLOutput;
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

    @PostConstruct
    public void iniciar() {
        WebDriver driver = seleniumUtility.getDriver(false, false, "E:\\mapeamentoSelerium");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.manage().window().maximize();
        driver.get("https://www.nfse.gov.br/EmissorNacional/Notas/Visualizar/Index/35541022251335957000120000000000000123076587295936");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Inscricao")));
        WebElement usuario = driver.findElement(By.id("Inscricao"));
        usuario.sendKeys("");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Senha")));
        WebElement senha = driver.findElement(By.id("Senha"));
        senha.sendKeys("");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/section/div/div/div[2]/div[2]/div[1]/div/form/div[3]/button")));
        WebElement botaoLogin = driver.findElement(By.xpath("/html/body/section/div/div/div[2]/div[2]/div[1]/div/form/div[3]/button"));
        botaoLogin.click();





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
