package br.com.ether.applications.anf.services;


import br.com.ether.model.CredenciaisModel;
import br.com.ether.model.DadosDBModel;
import br.com.ether.model.DadosHistoricoModel;
import br.com.ether.repository.DataBase;
import br.com.ether.utilities.*;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ANFService {

    private WebDriverWait wait = null;
    private WebDriver driver = null;
    private final DateUtility dateUtility;
    private final DataBase dataBase;
    private final EmailUtility emailUtility;
    private final SeleniumUtility seleniumUtility;
    private final Aguardar aguardar;
    private final FolderUtility folderUtility;
    private final ConverterUtility converterUtility;
    private final LogUtility logger;
    private static final String LINK_LOGIN = "https://www.nfse.gov.br/EmissorNacional/Login?ReturnUrl=%2fEmissorNacional%2f";
    private static final String LINK_EMITIR = "https://www.nfse.gov.br/EmissorNacional/DPS/Pessoas";

    private static final String NAME = System.getenv("USERNAME");
    private static final String PATH_DOWNLOAD_WINDOWS = "C:\\Workspace\\Downloads\\";
    private static final String PATH_DOWNLOAD_LINUX = "/home/" + NAME + "/Downloads/";
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final String PATH_SAVE_NF = "\\NF\\";

    private static final String BODY = "<html><body>"
            + "<h1>Assunto do Email</h1>"
            + "<p>Prezado(a) destinatário,</p>"
            + "<p>Enviamos a seguir a nota fiscal referente aos serviços prestados.</p>"
            + "<p>Por favor, encontre o arquivo da nota fiscal em anexo a este email.</p>"
            + "<p>Caso tenha alguma dúvida ou necessite de mais informações, não hesite em nos contatar.</p>"
            + "<p>Atenciosamente,<br>Sua Empresa</p>"
            + "<p><i>Nota: Este é um email automático. Por favor, não responda a este email.</i></p>"
            + "</body></html>";

    public void run(DadosDBModel dadosDBModel, CredenciaisModel credenciaisModel) {

        try {
            while (true) {
                login(credenciaisModel);

                driver.get(LINK_EMITIR);

                logger.registraLog("Emitindo nota fiscal");

                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("DataCompetencia")));
                WebElement campoData = driver.findElement(By.id("DataCompetencia"));
                campoData.sendKeys(dateUtility.getToday("dd/MM/yyyy"));

                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"pnlTomador\"]/div[1]/div/div")));
                WebElement tomadorLocal = driver.findElement(By.xpath("//*[@id=\"pnlTomador\"]/div[1]/div/div"));
                tomadorLocal.click();

                aguardar.segundos(5);

                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[contains(text(), 'Brasil')]")));
                WebElement brasil = driver.findElement(By.xpath("//label[contains(text(), 'Brasil')]"));
                brasil.click();

                aguardar.segundos(5);

                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Tomador_Inscricao")));
                WebElement cnpjoto = driver.findElement(By.id("Tomador_Inscricao"));
                cnpjoto.sendKeys(dadosDBModel.getCnpj_tomador());

                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btn_Tomador_Inscricao_pesquisar")));
                WebElement cnpjotoPesquisar = driver.findElement(By.id("btn_Tomador_Inscricao_pesquisar"));
                cnpjotoPesquisar.click();

                if (!dadosDBModel.getIm_prestador().equalsIgnoreCase("")) {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Tomador_InscricaoMunicipal")));
                    WebElement inscricaoMunicipal = driver.findElement(By.id("Tomador_InscricaoMunicipal"));
                    inscricaoMunicipal.sendKeys(dadosDBModel.getIm_prestador());
                }

                if (!dadosDBModel.getTelefone_prestador().equalsIgnoreCase("")) {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Tomador_Telefone")));
                    WebElement tomadorTel = driver.findElement(By.id("Tomador_Telefone"));
                    tomadorTel.sendKeys(dadosDBModel.getTelefone_prestador());
                }

                if (!dadosDBModel.getEmail_prestador().equalsIgnoreCase("")) {
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Tomador_Email")));
                    WebElement tomadorEmail = driver.findElement(By.id("Tomador_Email"));
                    tomadorEmail.sendKeys(dadosDBModel.getEmail_prestador());
                }

                aguardar.segundos(5);

                //TODO ADICIONAR ENDEREÇO

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

                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnAvancar")));
                WebElement avancarPessoas = driver.findElement(By.id("btnAvancar"));
                avancarPessoas.click();

                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"pnlLocalPrestacao\"]/div/div/div[2]/div/span[1]/span[1]/span")));
                WebElement clickMunicipio = driver.findElement(By.xpath("//*[@id=\"pnlLocalPrestacao\"]/div/div/div[2]/div/span[1]/span[1]/span"));
                clickMunicipio.click();

                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/span/span/span[1]/input")));
                WebElement sendMunicipio = driver.findElement(By.xpath("/html/body/span/span/span[1]/input"));
                sendMunicipio.sendKeys(dadosDBModel.getMunicipio());

                aguardar.segundos(5);

                driver.findElement(By.id("select2-LocalPrestacao_CodigoMunicipioPrestacao-results")).click();

                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"pnlServicoPrestado\"]/div/div[1]/div/div/span[1]/span[1]/span")));
                WebElement clickCodigo = driver.findElement(By.xpath("//*[@id=\"pnlServicoPrestado\"]/div/div[1]/div/div/span[1]/span[1]/span"));
                clickCodigo.click();

                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/span/span/span[1]/input")));
                WebElement sendCodigo = driver.findElement(By.xpath("/html/body/span/span/span[1]/input"));
                sendCodigo.sendKeys(dadosDBModel.getCnae());

                aguardar.segundos(5);

                driver.findElement(By.xpath("//*[@id=\"select2-ServicoPrestado_CodigoTributacaoNacional-results\"]/li[1]")).click();

                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[contains(text(), 'Não')]")));
                WebElement clickServico = driver.findElement(By.xpath("//label[contains(text(), 'Não')]"));
                clickServico.click();

                aguardar.segundos(5);

                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ServicoPrestado_Descricao")));
                WebElement clickDescricao = driver.findElement(By.id("ServicoPrestado_Descricao"));
                clickDescricao.click();

                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ServicoPrestado_Descricao")));
                WebElement sendDescricao = driver.findElement(By.id("ServicoPrestado_Descricao"));
                sendDescricao.sendKeys(dadosDBModel.getDescricao_servico());

                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/form/div[7]/button")));
                WebElement avancarServico = driver.findElement(By.xpath("/html/body/div[1]/form/div[7]/button"));
                avancarServico.click();

                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Valores_ValorServico")));
                WebElement clickValor = driver.findElement(By.id("Valores_ValorServico"));
                clickValor.click();

                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Valores_ValorServico")));
                WebElement sendValor = driver.findElement(By.id("Valores_ValorServico"));
                sendValor.sendKeys(dadosDBModel.getValor_nf());

                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Valores_ValorServico")));
                WebElement clickNenhumValor = driver.findElement(By.xpath("//*[@id=\"pnlOpcaoParaMEI\"]/div/div/label"));
                clickNenhumValor.click();

                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/form/div[7]/button")));
                WebElement avancarValores = driver.findElement(By.xpath("/html/body/div[1]/form/div[7]/button"));
                avancarValores.click();

                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnProsseguir")));
                WebElement emitirNota = driver.findElement(By.id("btnProsseguir"));
                emitirNota.click();

                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Visualizar NFS-e']")));
                WebElement viewNota = driver.findElement(By.xpath("//span[text()='Visualizar NFS-e']"));
                viewNota.click();

                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@data-original-title='Download DANFSe']")));
                WebElement baixarNota = driver.findElement(By.xpath("//a[@data-original-title='Download DANFSe']"));
                baixarNota.click();

                aguardar.segundos(5);

                logger.registraLog("Nota fiscal emitida com sucesso");

                driver.quit();
                break;
            }
        } catch (Exception e) {
            logger.registraException("Erro ao emitir nota fiscal", e);
            logger.registraErro("Tentando novamente em 1 minuto");
            aguardar.minutos(1);
            driver.quit();
        }

        String chave = "";
        String base64 = "";
        String caminho = "";

        //Mover arquivo para pasta
        if (OS.equalsIgnoreCase("linux")) {
            base64 = converterUtility.encondeBase64(folderUtility.getFile(PATH_DOWNLOAD_LINUX));
            chave = folderUtility.getChave(PATH_DOWNLOAD_LINUX);
            caminho = folderUtility.moveFile(PATH_DOWNLOAD_LINUX, PATH_SAVE_NF, OS);
        } else {
            base64 = converterUtility.encondeBase64(folderUtility.getFile(PATH_DOWNLOAD_WINDOWS));
            chave = folderUtility.getChave(PATH_DOWNLOAD_WINDOWS);
            caminho = folderUtility.moveFile(PATH_DOWNLOAD_WINDOWS, PATH_SAVE_NF, OS);
        }

        DadosHistoricoModel dadosHistoricoModel = DadosHistoricoModel.builder()
                .cnpj(credenciaisModel.getLogin())
                .data_da_emissao(dateUtility.getToday("dd/MM/yyyy"))
                .valor(dadosDBModel.getValor_nf())
                .chave(chave)
                .base64(base64)
                .build();

        dataBase.insertHistorico(dadosHistoricoModel);

        List<CredenciaisModel> acessosModelList = dataBase.getAcessos();

        String finalCaminho = caminho;

        acessosModelList.forEach(e -> {
            if (e.getPlataforma().equalsIgnoreCase("outlook")) //change to yank
//                if (e.getPlataforma().equalsIgnoreCase("yank"))
                emailUtility.sendMail(
                        "NF mês " + dateUtility.getToday("MM/yyyy") + " | Yank! Solutions",
                        BODY,
                        "matheusoliveira1991@hotmail.com",
                        "",
                        "",
                        finalCaminho, e);
        });
    }

    private void login(CredenciaisModel credenciaisModel) {
        logger.registraLog("Realizando Login no Site do Governo");
        logger.registraLog("OS: " + OS);

        while (true) {
            try {
                if (OS.equalsIgnoreCase("linux"))
                    driver = seleniumUtility.getDriver(false, false, PATH_DOWNLOAD_LINUX);
                else
                    driver = seleniumUtility.getDriver(false, false, PATH_DOWNLOAD_WINDOWS);

                wait = new WebDriverWait(driver, Duration.ofSeconds(30));
                driver.manage().window().maximize();
                driver.get(LINK_LOGIN);

                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Inscricao")));
                WebElement usuario = driver.findElement(By.id("Inscricao"));
                usuario.sendKeys(credenciaisModel.getLogin());

                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("Senha")));
                WebElement senha = driver.findElement(By.id("Senha"));
                senha.sendKeys(credenciaisModel.getSenha());

                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[text()='Entrar']")));
                WebElement botaoLogin = driver.findElement(By.xpath("//button[text()='Entrar']"));
                botaoLogin.click();

                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("wgtUltimasNFSe")));

                logger.registraLog("Login realizado com sucesso");
                break;
            } catch (Exception e) {
                logger.registraException("Erro ao realizar login", e);
                logger.registraErro("Tentando novamente em 1 minuto");
                driver.quit();
//            aguardar.minutos(1);
            }
        }
    }
}
