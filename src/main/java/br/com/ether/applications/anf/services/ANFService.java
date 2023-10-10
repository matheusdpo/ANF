package br.com.ether.applications.anf.services;


import br.com.ether.model.CredenciaisModel;
import br.com.ether.model.DadosDBModel;
import br.com.ether.model.DadosHistoricoModel;
import br.com.ether.repository.DataBase;
import br.com.ether.utilities.DateUtility;
import br.com.ether.utilities.EmailUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ANFService {

    private final DateUtility dateUtility;
    private final DataBase dataBase;
    private final EmailUtility emailUtility;
    private static final String PATH_DOWNLOAD = "C:\\Users\\mathe\\Downloads\\";
    private static final String PATH_SAVE_NF = "C:\\Users\\mathe\\Desktop\\NF\\";
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

        DadosHistoricoModel dadosHistoricoModel = DadosHistoricoModel.builder()
                .cnpj(credenciaisModel.getLogin())
                .data_da_emissao(dateUtility.getToday("dd/MM/yyyy"))
                .valor(dadosDBModel.getValor_nf())
                .chave("12345678901234567890123456789012345678901234")
                .build();

        String namenf = "NF_Matheus_P_Oliveira_" + dateUtility.getToday("MM-yyyy") + ".pdf";
        dataBase.insertHistorico(dadosHistoricoModel);
        emailUtility.sendMail("NF mês " + dateUtility.getToday("MM/yyyy") + " | Yank! Solutions",
                BODY, "matheusoliveira1991@hotmail.com", "", "", "", credenciaisModel);
    }
}
