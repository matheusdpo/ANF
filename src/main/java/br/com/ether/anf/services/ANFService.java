package br.com.ether.anf.services;


import br.com.ether.model.CredentialsModel;
import br.com.ether.model.DadosHistoricoModel;
import br.com.ether.model.DatasDBModel;
import br.com.ether.repository.DataBase;
import br.com.ether.utilities.*;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class ANFService {
    private WebDriverWait wait = null;
    private WebDriver driver = null;
    private final DateUtility dateUtility;
    private final DataBase dataBase;
    private final EmailUtility emailUtility;
    private final SeleniumUtility seleniumUtility;
    private final WaitMoment waitMoment;
    private final FolderUtility folderUtility;
    private final ConverterUtility converterUtility;
    private final LogUtility logger;
    private static final String LINK_LOGIN = "https://www.nfse.gov.br/EmissorNacional/Login?ReturnUrl=%2fEmissorNacional%2f";
    private static final String LINK_ISSUE = "https://www.nfse.gov.br/EmissorNacional/DPS/Pessoas";
    private static final String NAME = System.getenv("USERNAME");
    private static final String PATH_DOWNLOAD_WINDOWS = "C:\\Workspace\\Downloads\\";
    private static final String PATH_DOWNLOAD_LINUX = "/home/" + NAME + "/Downloads/";
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final String PATH_SAVE_NF = "\\NF\\";
    private static String KEY = "";
    private static String BASE64 = "";
    private static String PATH = "";
    @Value("${br.com.ether.mail.body}")
    private String body;
    @Value("${br.com.ether.mail.to}")
    private String to;
    @Value("${br.com.ether.mail.cc}")
    private String cc;
    @Value("${br.com.ether.mail.bcc}")
    private String bcc;
    @Value("${br.com.ether.mail.mail}")
    private String mailLogin;
    @Value("${br.com.ether.mail.passwd}")
    private String passwdMail;
    @Value("${br.com.ether.cnpj}")
    private String CNPJ;
    @Value("${br.com.ether.passwdCNPJ}")
    private String passwdCNPJ;

    public void run(DatasDBModel datasDBModel) {
        issueInvoice(datasDBModel);

        getInformations();

        insertDB(datasDBModel, CNPJ);

        sendMail();
    }

    private void issueInvoice(DatasDBModel datasDBModel) {
        try {
            while (true) {
                login();

                driver.get(LINK_ISSUE);

                logger.registerLog("Issuing Invoice");

                seleniumUtility.sendKeysByID(driver, wait, "DataCompetencia", dateUtility.getToday("dd/MM/yyyy"));

                seleniumUtility.clickElementByXPath(driver, wait, "//label[contains(text(), 'Prestador')]");

                seleniumUtility.waitByXpath(driver, wait, "//div[@id=\"modalLoading\" and contains(@style, \"display: none;\")]");

                seleniumUtility.waitByXpath(driver, wait, "//div[@id=\"modalLoading\" and contains(@style, \"display: none;\")]");

                seleniumUtility.clickElementByXPath(driver, wait, "//label[contains(text(), 'Brasil')]");

                seleniumUtility.sendKeysByID(driver, wait, "Tomador_Inscricao", datasDBModel.getCnpj_customer());

                seleniumUtility.clickElementByID(driver, wait, "btn_Tomador_Inscricao_pesquisar");

                if (!datasDBModel.getIm_customer().equalsIgnoreCase("")) {
                    seleniumUtility.sendKeysByID(driver, wait, "Tomador_InscricaoMunicipal", datasDBModel.getIm_customer());
                }

                if (!datasDBModel.getPhone_customer().equalsIgnoreCase("")) {
                    seleniumUtility.sendKeysByID(driver, wait, "Tomador_Telefone", datasDBModel.getPhone_customer());
                }

                if (!datasDBModel.getMail_customer().equalsIgnoreCase("")) {
                    seleniumUtility.sendKeysByID(driver, wait, "Tomador_Email", datasDBModel.getMail_customer());
                }

                //TODO ADICIONAR ENDEREÇO

                seleniumUtility.clickElementByID(driver, wait, "btnAvancar");

                seleniumUtility.clickElementByXPath(driver, wait, "//*[@id=\"pnlLocalPrestacao\"]/div/div/div[2]/div/span[1]/span[1]/span");

                seleniumUtility.sendKeysByXPath(driver, wait, "/html/body/span/span/span[1]/input", datasDBModel.getCity());

                seleniumUtility.clickElementByXPath(driver, wait, "//li[@class=\"select2-results__option select2-results__option--selectable select2-results__option--highlighted\" and @role=\"option\" and @aria-selected=\"true\"]");

                seleniumUtility.clickElementByXPath(driver, wait, "//span[@class=\"select2-selection select2-selection--single\" and @role=\"combobox\" and @aria-haspopup=\"true\" and @aria-expanded=\"false\" and @tabindex=\"0\" and @aria-disabled=\"false\" and @aria-labelledby=\"select2-ServicoPrestado_CodigoTributacaoNacional-container\" and @aria-controls=\"select2-ServicoPrestado_CodigoTributacaoNacional-container\"]");

                seleniumUtility.sendKeysByXPath(driver, wait, "/html/body/span/span/span[1]/input", datasDBModel.getCnae());

                seleniumUtility.clickElementByXPath(driver, wait, "//li[@class=\"select2-results__option select2-results__option--selectable select2-results__option--highlighted\"]");

                seleniumUtility.waitByXpath(driver, wait, "//div[@id=\"modalLoading\" and contains(@style, \"display: none;\")]");

                seleniumUtility.clickElementByXPath(driver, wait, "//label[contains(text(), 'Não')]");

                seleniumUtility.waitByXpath(driver, wait, "//div[@id=\"modalLoading\" and contains(@style, \"display: none;\")]");

                seleniumUtility.clickElementByID(driver, wait, "ServicoPrestado_Descricao");

                seleniumUtility.sendKeysByID(driver, wait, "ServicoPrestado_Descricao", datasDBModel.getDesc_customer());

                seleniumUtility.clickElementByXPath(driver, wait, "/html/body/div[1]/form/div[7]/button");

                seleniumUtility.clickElementByID(driver, wait, "Valores_ValorServico");

                seleniumUtility.sendKeysByID(driver, wait, "Valores_ValorServico", datasDBModel.getValue());

                seleniumUtility.clickElementByXPath(driver, wait, "//*[@id=\"pnlOpcaoParaMEI\"]/div/div/label");

                seleniumUtility.clickElementByXPath(driver, wait, "/html/body/div[1]/form/div[7]/button");

                seleniumUtility.clickElementByID(driver, wait, "btnProsseguir");

                seleniumUtility.clickElementByXPath(driver, wait, "//span[text()='Visualizar NFS-e']");

                seleniumUtility.clickElementByXPath(driver, wait, "//a[@data-original-title='Download DANFSe']");

                isDownloaded();

                seleniumUtility.quit(driver);

                logger.registerLog("Invoice has been issued successfully!");
                break;
            }
        } catch (Exception e) {
            logger.registerException("There was an error issuing the invoice", e);
            logger.registerError("Trying again in 1 minute");
            waitMoment.minutes(1);
            seleniumUtility.quit(driver);
        }
    }

    private void login() {
        logger.registerLog("Loggin in to Goverment Website");

        while (true) {
            try {
                if (OS.equalsIgnoreCase("linux"))
                    driver = seleniumUtility.getDriver(false, false, PATH_DOWNLOAD_LINUX);
                else
                    driver = seleniumUtility.getDriver(false, false, PATH_DOWNLOAD_WINDOWS);

                wait = new WebDriverWait(driver, Duration.ofSeconds(30));
                driver.manage().window().maximize();
                driver.get(LINK_LOGIN);

                seleniumUtility.sendKeysByID(driver, wait, "Inscricao", CNPJ);

                seleniumUtility.sendKeysByID(driver, wait, "Senha", passwdCNPJ);

                seleniumUtility.clickElementByXPath(driver, wait, "//button[text()='Entrar']");

                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("wgtUltimasNFSe")));

                logger.registerLog("Logged in successfully!");
                break;
            } catch (Exception e) {
                logger.registerException("There was an error trying to Loggin", e);
                logger.registerError("Trying again in 1 minute");
                seleniumUtility.quit(driver);
                waitMoment.minutes(1);
            }
        }
    }

    private void isDownloaded() {
        while (true) {
            if (OS.equalsIgnoreCase("linux")) {
                if (folderUtility.isPDFHere(PATH_DOWNLOAD_LINUX))
                    break;
            } else {
                if (folderUtility.isPDFHere(PATH_DOWNLOAD_WINDOWS))
                    break;
            }
        }
    }

    private void getInformations() {
        logger.registerLog("Getting informations");

        //Mover arquivo para pasta
        if (OS.equalsIgnoreCase("linux")) {
            BASE64 = converterUtility.encondeBase64(folderUtility.getFile(PATH_DOWNLOAD_LINUX));
            KEY = folderUtility.getKey(PATH_DOWNLOAD_LINUX);
            PATH = folderUtility.moveFile(PATH_DOWNLOAD_LINUX, PATH_SAVE_NF, OS);
        } else {
            BASE64 = converterUtility.encondeBase64(folderUtility.getFile(PATH_DOWNLOAD_WINDOWS));
            KEY = folderUtility.getKey(PATH_DOWNLOAD_WINDOWS);
            PATH = folderUtility.moveFile(PATH_DOWNLOAD_WINDOWS, PATH_SAVE_NF, OS);
        }
    }

    private void insertDB(DatasDBModel datasDBModel, String CNPJ) {
        DadosHistoricoModel dadosHistoricoModel = DadosHistoricoModel.builder()
                .cnpj(CNPJ)
                .data_da_emissao(dateUtility.getToday("dd/MM/yyyy"))
                .valor(datasDBModel.getValue())
                .chave(KEY)
//                .base64(base64)
                .build();

        logger.registerLog("Inserting data into the database");
        logger.registerLog("Key: " + KEY);
        logger.registerLog("Path: " + PATH);
        logger.registerLog("Base64: " + BASE64);
        dataBase.insertHistorico(dadosHistoricoModel);
        logger.registerLog("Information inserted successfully!");
    }

    private void sendMail() {

        CredentialsModel credentialsModel = CredentialsModel.builder()
                .login(mailLogin)
                .passwd(passwdMail)
                .build();

        String subject = "NF mês " + dateUtility.getToday("MM/yyyy") + " | Yank! Solutions";

        emailUtility.sendMail(
                subject,
                body,
                to,
                cc,
                bcc,
                PATH,
                credentialsModel);

    }
}