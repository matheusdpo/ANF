package br.com.ether.repository;

import br.com.ether.model.CredenciaisModel;
import br.com.ether.model.DadosDBModel;
import br.com.ether.model.DadosHistoricoModel;
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
    private static final String SELECT_CASOS = "SELECT * FROM dados_anf WHERE status = 1;";
    private static final String SELECT_ACESSOS = "SELECT * FROM acessos;";
    private static final String INSERT = "INSERT INTO historico_notas (cnpj, data_da_emissao, valor, chave) VALUES (?, ?, ?, ?);";

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
        logger.registraLog("Iniciando a captura de dados do banco de dados");

        List<DadosDBModel> dadosDBModelList = null;

        while (true) {
            try {
                ResultSet resultSet = select(SELECT_CASOS);

                dadosDBModelList = new ArrayList<>();

                while (resultSet.next()) {

                    DadosDBModel dadosDBModel = DadosDBModel
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

    public List<CredenciaisModel> getAcessos() {
        logger.registraLog("Iniciando a consulta no banco de dados");
        logger.registraLog("SELECT: " + SELECT_ACESSOS);

        List<CredenciaisModel> acessosModelList = null;

        while (true) {
            try {
                ResultSet resultSet = select(SELECT_ACESSOS);

                acessosModelList = new ArrayList<>();

                while (resultSet.next()) {

                    CredenciaisModel credenciaisModel = CredenciaisModel
                            .builder()
                            .id(resultSet.getLong("id"))
                            .login(resultSet.getString("login"))
                            .senha(resultSet.getString("senha"))
                            .plataforma(resultSet.getString("plataforma"))
                            .build();


                    acessosModelList.add(credenciaisModel);
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
        return acessosModelList;
    }

    private ResultSet select(String select) {
        logger.registraLog("Realizando a consulta no banco de dados");
        logger.registraLog("SELECT: " + select);

        ResultSet resultSet = null;


        while (true) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(select);
                resultSet = preparedStatement.executeQuery();
                break;
            } catch (Exception e) {
                logger.registraException("Erro ao consultar o banco de dados", e);
                logger.registraErro("Tentando novamente em 1 minuto");
                aguardar.minutos(1);
                logger.registraErro("=============================================");
                connectDB();
            }
        }
        return resultSet;
    }

    public void insertHistorico(DadosHistoricoModel dadosHistoricoModel) {
        logger.registraLog("Iniciando a inserção de dados no banco de dados");
        logger.registraLog("INSERT: " + INSERT);

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
                logger.registraException("Erro ao inserir dados no banco de dados", e);
                logger.registraErro("Tentando novamente em 1 minuto");
                aguardar.minutos(1);
                logger.registraErro("=============================================");
                connectDB();
            }
        }
    }
}
