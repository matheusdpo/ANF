package br.com.ether.utilities;


import br.com.ether.config.EmailConfig;
import br.com.ether.model.CredenciaisModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;

@Component
@Configuration
@RequiredArgsConstructor
public class EmailUtility {

    private final LogUtility logger;
    private final EmailConfig emailConfig;

    @Value("${br.com.ether.mail.mail}")
    private String mail;

    public void sendMail(String subject, String body, String to, String cc, String bcc, String attachment, CredenciaisModel credenciaisModel) {

        logger.registraLog("Preparando envio de e-mail");

        // Cria uma sessão
        Session session = emailConfig.setMailConfig(credenciaisModel);

        try {
            // Cria uma mensagem de email
            Message mensagem = new MimeMessage(session);
            mensagem.setFrom(new InternetAddress(mail));

            // Adiciona destinatários (TO, CC, BCC)
            mensagem.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            mensagem.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(cc));
            mensagem.setRecipients(Message.RecipientType.BCC,
                    InternetAddress.parse(bcc));

            mensagem.setSubject(subject);

            // Cria a parte de texto do email
            MimeBodyPart corpoEmail = new MimeBodyPart();

            corpoEmail.setContent(body, "text/html; charset=UTF-8");

            // Anexar arquivos
            Multipart multipart = new MimeMultipart();

            // Adicionar a parte de texto ao multipart
            multipart.addBodyPart(corpoEmail);

            // Anexar um arquivo
            addAttachment(multipart, attachment);

            // Define o conteúdo do email como o multipart
            mensagem.setContent(multipart);

            // Envia o email
            Transport.send(mensagem);

            logger.registraLog("E-mail enviado com sucesso!");
        } catch (MessagingException e) {
            logger.registraException("Erro ao enviar e-mail", e);
        }
    }

    // Função para adicionar anexos
    public static void addAttachment(Multipart multipart, String filePath) throws MessagingException {
        File file = new File(filePath);
        if (file.exists()) {
            MimeBodyPart attachmentPart = new MimeBodyPart();
            FileDataSource source = new FileDataSource(filePath);
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName(new File(filePath).getName());
            multipart.addBodyPart(attachmentPart);
        }

    }
}
