package br.com.ether.config;

import br.com.ether.model.CredenciaisModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class EmailConfig {

    @Value("${br.com.ether.mail.host}")
    private String host;

    @Value("${br.com.ether.mail.port}")
    private int port;

    public Session setMailConfig(CredenciaisModel credenciaisModel) {
        // Configurações para o servidor SMTP do Outlook

        String usuario = credenciaisModel.getLogin();
        String senha = credenciaisModel.getSenha();

        // Configurações adicionais
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Autenticação
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, senha);
            }
        };

        // Cria uma sessão
        return Session.getInstance(props, auth);
    }

}
