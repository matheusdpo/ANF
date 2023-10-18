package br.com.ether.repository;

import br.com.ether.model.CredentialsModel;
import br.com.ether.model.DadosHistoricoModel;
import br.com.ether.model.DatasDBModel;
import br.com.ether.utilities.LogUtility;
import br.com.ether.utilities.WaitMoment;
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
    private static final String SELECT_CASOS = "SELECT * FROM dados_anf WHERE status = 1;";
    private static final String SELECT_ACESSOS = "SELECT * FROM acessos;";
    private static final String INSERT = "INSERT INTO historico_notas (cnpj, data_da_emissao, valor, chave) VALUES (?, ?, ?, ?);";

    private final LogUtility logger;
    private final WaitMoment waitMoment;

    private Connection connection;

    public void connectDB() {
        logger.registerLog("Starting the connection to the database");
        logger.registerLog("URL: " + URL);
        logger.registerLog("USERNAME: " + USERNAME);
        logger.registerLog("PASSWORD: " + new StringBuilder(PASSWORD).replace(0, PASSWORD.length(), "*".repeat(PASSWORD.length())));

        while (true) {
            try {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                logger.registerLog("Connection to the database successfully");
                break;
            } catch (Exception e) {
                logger.registraException("There was an error connecting to the database", e);
                logger.registraErro("Trying again in 1 minute");
                //aguardar.minutos(1);
                logger.registraErro("=============================================");
            }
        }
    }

    public List<DatasDBModel> getCases() {
        logger.registerLog("Starting to capture data from the database");

        List<DatasDBModel> datasDBModelList = null;

        while (true) {
            try {
                ResultSet resultSet = select(SELECT_CASOS);

                datasDBModelList = new ArrayList<>();

                while (resultSet.next()) {

                    DatasDBModel datasDBModel = DatasDBModel
                            .builder()
                            .id(resultSet.getLong("id"))
                            .status(resultSet.getInt("status"))
                            .valor_nf(resultSet.getString("valor_nf"))
                            .cnpj_tomador(resultSet.getString("cnpj_tomador"))
                            .im_prestador(resultSet.getString("im_prestador"))
                            .telefone_prestador(resultSet.getString("telefone_prestador"))
                            .email_prestador(resultSet.getString("email_prestador"))
                            .descricao_servico(resultSet.getString("descricao_servico"))
                            .endereco_prestador(resultSet.getString("endereco_prestador"))
                            .municipio(resultSet.getString("municipio"))
                            .cnae(resultSet.getString("cnae"))
                            .status(resultSet.getInt("status"))
                            .build();

                    datasDBModelList.add(datasDBModel);
                }
                break;
            } catch (Exception e) {
                logger.registraException("There was an error capturing data from the database", e);
                logger.registraErro("Trying again in 1 minute");
                waitMoment.minutes(1);
                logger.registraErro("=============================================");
                connectDB();
            }
        }
        return datasDBModelList;
    }

    public List<CredentialsModel> getCredentials() {
        logger.registerLog("Starting to capture data from the database");
        logger.registerLog("SELECT: " + SELECT_ACESSOS);

        List<CredentialsModel> acessosModelList = null;

        while (true) {
            try {
                ResultSet resultSet = select(SELECT_ACESSOS);

                acessosModelList = new ArrayList<>();

                while (resultSet.next()) {

                    CredentialsModel credentialsModel = CredentialsModel
                            .builder()
                            .id(resultSet.getLong("id"))
                            .login(resultSet.getString("login"))
                            .passwd(resultSet.getString("senha"))
                            .plataform(resultSet.getString("plataforma"))
                            .build();


                    acessosModelList.add(credentialsModel);
                }
                break;
            } catch (Exception e) {
                logger.registraException("There was an error capturing data from the database", e);
                logger.registraErro("Trying again in 1 minute");
                waitMoment.minutes(1);
                logger.registraErro("=============================================");
                connectDB();
            }
        }
        return acessosModelList;
    }

    private ResultSet select(String select) {
        logger.registerLog("Starting the query to the database");
        logger.registerLog("SELECT: " + select);

        ResultSet resultSet = null;


        while (true) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(select);
                resultSet = preparedStatement.executeQuery();
                break;
            } catch (Exception e) {
                logger.registraException("There was an error querying the database", e);
                logger.registraErro("Trying again in 1 minute");
                waitMoment.minutes(1);
                logger.registraErro("=============================================");
                connectDB();
            }
        }
        return resultSet;
    }

    public void insertHistorico(DadosHistoricoModel dadosHistoricoModel) {
        logger.registerLog("Starting to insert data into the database");
        logger.registerLog("INSERT: " + INSERT);

        while (true) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT);

                preparedStatement.setString(1, dadosHistoricoModel.getCnpj());
                preparedStatement.setString(2, dadosHistoricoModel.getData_da_emissao());
                preparedStatement.setString(3, dadosHistoricoModel.getValor());
                preparedStatement.setString(4, dadosHistoricoModel.getChave());
                preparedStatement.execute();
                break;
            } catch (Exception e) {
                logger.registraException("There was an error inserting data into the database", e);
                logger.registraErro("Trying again in 1 minute");
                waitMoment.minutes(1);
                logger.registraErro("=============================================");
                connectDB();
            }
        }
    }
}
