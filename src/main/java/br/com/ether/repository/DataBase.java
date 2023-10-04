package br.com.ether.repository;

import br.com.ether.model.AcessoModel;
import br.com.ether.model.DadosDBModel;
import br.com.ether.utilities.Aguardar;
import br.com.ether.utilities.LogUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Component
@Configuration
@RequiredArgsConstructor
public class DataBase {

    @Value("${br.com.ether.db.url}")
    private String URL;

    @Value("${br.com.ether.db.username}")
    private String USERNAME;

    @Value("${br.com.ether.db.password}")
    private String PASSWORD;

    @Value("${br.com.ether.db.driver}")
    private String DRIVER;
    private static final String SELECT_CASOS = "SELECT * FROM 'dados_anf' WHERE 'status' = 1";
    private static final String SELECT_ACESSOS = "SELECT * FROM 'acessos'";

    private final LogUtility logger;
    private final Aguardar aguardar;

    private Connection connection;

    public void connectDB() {
        logger.registraLog("Iniciando a conexão com o banco de dados");
        logger.registraLog("URL: " + URL);
        logger.registraLog("USERNAME: " + USERNAME);
        logger.registraLog("PASSWORD: " + new StringBuilder(PASSWORD).replace(0, PASSWORD.length(), "*".repeat(PASSWORD.length())));

        while (true) {
            try {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                logger.registraLog("Conexão com o banco de dados realizada com sucesso");
                break;
            } catch (Exception e) {
                logger.registraException("Erro ao conectar com o banco de dados", e);
                logger.registraErro("Tentando novamente em 1 minuto");
                aguardar.minutos(1);
                logger.registraErro("=============================================");
            }
        }
    }

    public List<DadosDBModel> getCasos() {
        logger.registraLog("Iniciando a consulta no banco de dados");
        logger.registraLog("SELECT: " + SELECT_CASOS);

        List<DadosDBModel> dadosDBModelList = null;

        while (true) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CASOS);
                ResultSet resultSet = preparedStatement.executeQuery();

                dadosDBModelList = new ArrayList<>();

                while (resultSet.next()) {
                    DadosDBModel dadosDBModel = new DadosDBModel();

                    dadosDBModel.setLogin(resultSet.getString("login"));
                    dadosDBModel.setSenha(resultSet.getString("senha"));
                    dadosDBModel.setValor_nf(resultSet.getString("valor_nf"));
                    dadosDBModel.setData_competencia(resultSet.getString("data_competencia"));
                    dadosDBModel.setCnpj_tomador(resultSet.getString("cnpj_tomador"));
                    dadosDBModel.setIm_prestador(resultSet.getString("im_prestador"));
                    dadosDBModel.setTelefone_prestador(resultSet.getString("telefone_prestador"));
                    dadosDBModel.setEmail_prestador(resultSet.getString("email_prestador"));
                    dadosDBModel.setEndereco_prestador(resultSet.getString("endereco_prestador"));
                    dadosDBModel.setMunicipio(resultSet.getString("municipio"));
                    dadosDBModel.setCnae(resultSet.getString("cnae"));
                    dadosDBModel.setDescricao_servico(resultSet.getString("descricao_servico"));
                    dadosDBModel.setChave(resultSet.getString("chave"));
                    dadosDBModel.setStatus(resultSet.getInt("status"));

                    dadosDBModelList.add(dadosDBModel);
                }
                break;
            } catch (Exception e) {
                logger.registraException("Erro ao consultar o banco de dados", e);
                logger.registraErro("Tentando novamente em 1 minuto");
                aguardar.minutos(1);
                logger.registraErro("=============================================");
                connectDB();
            }
        }
        return dadosDBModelList;
    }

    public List<AcessoModel> getAcessos() {
        logger.registraLog("Iniciando a consulta no banco de dados");
        logger.registraLog("SELECT: " + SELECT_ACESSOS);

        List<AcessoModel> acessoModelList = null;

        while (true) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ACESSOS);
                ResultSet resultSet = preparedStatement.executeQuery();

                acessoModelList = new ArrayList<>();

                while (resultSet.next()) {
                    AcessoModel acessoModel = new AcessoModel();

                    acessoModel.setLogin(resultSet.getString("login"));
                    acessoModel.setSenha(resultSet.getString("senha"));
                    acessoModel.setPlataforma(resultSet.getString("plataforma"));

                    acessoModelList.add(acessoModel);
                }
                break;
            } catch (Exception e) {
                logger.registraException("Erro ao consultar o banco de dados", e);
                logger.registraErro("Tentando novamente em 1 minuto");
                aguardar.minutos(1);
                logger.registraErro("=============================================");
                connectDB();
            }
        }
        return acessoModelList;
    }
}
