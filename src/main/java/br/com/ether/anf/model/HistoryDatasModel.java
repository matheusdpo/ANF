package br.com.ether.anf.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HistoryDatasModel {
    private long id;
    private String cnpj;
    private String data_da_emissao;
    private String valor;
    private String chave;
    private String base64;
}
