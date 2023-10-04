package br.com.ether.utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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
}
