package br.com.ether.applications.anf.services;


import br.com.ether.model.CredenciaisModel;
import br.com.ether.model.DadosDBModel;
import br.com.ether.model.DadosHistoricoModel;
import br.com.ether.repository.DataBase;
import br.com.ether.utilities.DateUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ANFService {

    private final DateUtility dateUtility;
    private final DataBase dataBase;
    public void run(DadosDBModel dadosDBModel, CredenciaisModel credenciaisModel) {

        DadosHistoricoModel dadosHistoricoModel = DadosHistoricoModel.builder()
                .cnpj(credenciaisModel.getLogin())
                .data_da_emissao(dateUtility.getToday("dd/MM/yyyy"))
                .valor(dadosDBModel.getValor_nf())
                .chave("12345678901234567890123456789012345678901234")
                .build();

        dataBase.insertHistorico(dadosHistoricoModel);
    }
}
