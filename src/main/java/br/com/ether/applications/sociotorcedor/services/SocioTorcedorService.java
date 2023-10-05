package br.com.ether.applications.sociotorcedor.services;

import br.com.ether.model.CredenciaisModel;
import br.com.ether.repository.DataBase;
import br.com.ether.utilities.Aguardar;
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

    private final Aguardar aguardar;
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
        List<CredenciaisModel> acessosModelList = dataBase.getAcessos();

        if (!acessosModelList.isEmpty()) {
            acessosModelList.forEach(e -> {
                if (e.getPlataforma().equalsIgnoreCase(PLATAFORMA))
                    run(e);
            });
        } else {
            logger.registraLog("Não há acessos para serem realizados");
        }

    }

    public void run(CredenciaisModel acessosModel) {
        login(acessosModel);
        goToExperiencias();
        goToExtrato();
        quit();
    }

    private void login(CredenciaisModel acessosModel) {
        logger.registraLog("Iniciando o processo de login no site do Sócio Torcedor | Dia: " + dateUtility.getToday(FORMAT));

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
                logger.registraLog("Inserindo o e-mail: " + acessosModel.getLogin());

                wait.until(ExpectedConditions.presenceOfElementLocated((By.id("mat-input-0"))));
                driver.findElement(By.id("mat-input-1")).sendKeys(acessosModel.getSenha());
                logger.registraLog("Inserindo a senha: " + new StringBuilder(acessosModel.getSenha()).replace(0, acessosModel.getSenha().length(), "*".repeat(acessosModel.getSenha().length())));

                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[text()=' ENTRAR ']")));
                driver.findElement(By.xpath("//button[text()=' ENTRAR ']")).click();

                aguardar.segundos(10);

                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//nav/a[text()=' EXPERIÊNCIAS ']")));
                driver.findElement(By.xpath("//nav/a[text()=' EXPERIÊNCIAS ']")).click();

                aguardar.segundos(10);

                logger.registraLog("Login realizado com sucesso");
                break;
            } catch (Exception e) {
                logger.registraException("Erro ao realizar o login no site do Sócio Torcedor", e);
                logger.registraErro("Tentando novamente em 1 minuto");
                driver.quit();
                aguardar.minutos(1);
            }
        }
    }


    private void goToExperiencias() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        logger.registraLog("Acessando a página de experiências");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//nav/a[text()=' EXPERIÊNCIAS ']")));
        driver.findElement(By.xpath("//nav/a[text()=' EXPERIÊNCIAS ']")).click();
    }

    private void goToExtrato() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        logger.registraLog("Acessando a página de extrato");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[text()='EXTRATO']")));
        driver.findElement(By.xpath("//a[text()='EXTRATO']")).click();

        aguardar.segundos(10);

        WebElement elementPoints = driver.findElement(By.xpath("//span[contains(text(), 'pts')]"));

        logger.registraLog("Extrato acessado com sucesso");
        logger.registraLog("Pontos disponíveis: " + elementPoints.getText());
        logger.registraLog("========================================================================================================");
    }

    private void quit() {
        driver.quit();
    }
}
