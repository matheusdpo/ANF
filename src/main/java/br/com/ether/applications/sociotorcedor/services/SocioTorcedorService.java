package br.com.ether.applications.sociotorcedor.services;

import br.com.ether.model.CredentialsModel;
import br.com.ether.repository.DataBase;
import br.com.ether.utilities.WaitMoment;
import br.com.ether.utilities.DateUtility;
import br.com.ether.utilities.LogUtility;
import br.com.ether.utilities.SeleniumUtility;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SocioTorcedorService {

    private final WaitMoment waitMoment;
    private final SeleniumUtility seleniumUtility;
    private final LogUtility logger;
    private final DateUtility dateUtility;
    private final DataBase dataBase;
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String PLATAFORMA = "ST";
    private static final String PATH_DOWNLOAD = "C:\\Workspace\\Downloads\\";
    private static final String FORMAT = "dd/MM/yyyy HH:mm:ss";

    public void init() {
        List<CredentialsModel> acessosModelList = dataBase.getCredentials();

        if (!acessosModelList.isEmpty()) {
            acessosModelList.forEach(e -> {
                if (e.getPlataform().equalsIgnoreCase(PLATAFORMA))
                    run(e);
            });
        } else {
            logger.registerLog("Não há acessos para serem realizados");
        }

    }

    public void run(CredentialsModel acessosModel) {
        login(acessosModel);
        goToExperiencias();
        goToExtrato();
        quit();
    }

    private void login(CredentialsModel acessosModel) {
        logger.registerLog("Iniciando o processo de login no site do Sócio Torcedor | Dia: " + dateUtility.getToday(FORMAT));

        while (true) {
            try {
                driver = seleniumUtility.getDriver(true, false, PATH_DOWNLOAD);
                wait = new WebDriverWait(driver, Duration.ofSeconds(30));

                driver.get("https://sociotorcedor.com.br/");
                driver.manage().window().maximize();

                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[text()=' LOGIN ']")));
                driver.findElement(By.xpath("//a[text()=' LOGIN ']")).click();

                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("mat-input-0")));
                driver.findElement(By.id("mat-input-0")).sendKeys(acessosModel.getLogin());
                logger.registerLog("Inserindo o e-mail: " + acessosModel.getLogin());

                wait.until(ExpectedConditions.presenceOfElementLocated((By.id("mat-input-0"))));
                driver.findElement(By.id("mat-input-1")).sendKeys(acessosModel.getPasswd());
                logger.registerLog("Inserindo a senha: " + new StringBuilder(acessosModel.getPasswd()).replace(0, acessosModel.getPasswd().length(), "*".repeat(acessosModel.getPasswd().length())));

                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[text()=' ENTRAR ']")));
                driver.findElement(By.xpath("//button[text()=' ENTRAR ']")).click();

                waitMoment.seconds(10);

                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//nav/a[text()=' EXPERIÊNCIAS ']")));
                driver.findElement(By.xpath("//nav/a[text()=' EXPERIÊNCIAS ']")).click();

                waitMoment.seconds(10);

                logger.registerLog("Login realizado com sucesso");
                break;
            } catch (Exception e) {
                logger.registerException("Erro ao realizar o login no site do Sócio Torcedor", e);
                logger.registerError("Tentando novamente em 1 minuto");
                driver.quit();
                waitMoment.minutes(1);
            }
        }
    }


    private void goToExperiencias() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        logger.registerLog("Acessando a página de experiências");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//nav/a[text()=' EXPERIÊNCIAS ']")));
        driver.findElement(By.xpath("//nav/a[text()=' EXPERIÊNCIAS ']")).click();
    }

    private void goToExtrato() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        logger.registerLog("Acessando a página de extrato");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[text()='EXTRATO']")));
        driver.findElement(By.xpath("//a[text()='EXTRATO']")).click();

        waitMoment.seconds(10);

        WebElement elementPoints = driver.findElement(By.xpath("//span[contains(text(), 'pts')]"));

        logger.registerLog("Extrato acessado com sucesso");
        logger.registerLog("Pontos disponíveis: " + elementPoints.getText());
        logger.registerLog("========================================================================================================");
    }

    private void quit() {
        driver.quit();
    }
}
