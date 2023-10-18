package br.com.ether.anf.utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SeleniumUtility {
    private final FolderUtility folderUtility;

    public WebDriver getDriver(boolean headless, boolean incognito, String pathDownload) {
        WebDriverManager.chromedriver().setup();

        System.setProperty("webdriver.chrome.verboseLogging", "false");
        System.setProperty("webdriver.chrome.silentOutput", "true");

        ChromeOptions options = new ChromeOptions();

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.prompt_for_download", false);
        folderUtility.isFolderExist(pathDownload);
        prefs.put("download.default_directory", pathDownload);

        options.setExperimentalOption("prefs", prefs);

        if (incognito)
            options.addArguments("--incognito");
        if (headless)
            options.addArguments("--headless");
        options.addArguments("--disable-default-app");
        options.addArguments("--accept-notifications");
        options.addArguments("--remote-allow-origins=*");
        return new ChromeDriver(options);
    }

    public void quit(WebDriver driver) {
        try {
            driver.quit();
        } catch (Exception e) {

        }

        try {
            driver.close();
        } catch (Exception ee) {

        }
    }

    public void sendKeysByXPath(WebDriver driver, WebDriverWait wait, String xpath, String value) {
        while (true) {
            try {
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
                driver.findElement(By.xpath(xpath)).sendKeys(value);

                break;
            } catch (Exception e) {
                System.out.println("erro ao enviar " + value + " no xpath " + xpath);

            }
        }
    }

    public void sendKeysByID(WebDriver driver, WebDriverWait wait, String id, String value) {
        while (true) {
            try {
                driver.findElement(By.id(id)).sendKeys(value);

                break;
            } catch (Exception e) {
                System.out.println("erro ao enviar " + value + " no id " + id);
            }
        }
    }

    public void clickElementByXPath(WebDriver driver, WebDriverWait wait, String xpath) {
        while (true) {
            try {
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
                driver.findElement(By.xpath(xpath)).click();

                break;
            } catch (Exception e) {
                System.out.println("erro ao clicar no xpath " + xpath);

            }
        }
    }

    public void clickElementByID(WebDriver driver, WebDriverWait wait, String id) {
        while (true) {
            try {
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
                driver.findElement(By.id(id)).click();

                break;
            } catch (Exception e) {
                System.out.println("erro ao clicar no id " + id);
            }
        }
    }

    public void waitByXpath(WebDriver driver, WebDriverWait wait, String xpath) {
        while (true) {
            try {
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
                break;
            } catch (Exception e) {
                System.out.println("erro ao esperar pelo xpath " + xpath);
            }
        }
    }
}
