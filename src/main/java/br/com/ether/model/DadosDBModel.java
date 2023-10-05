package br.com.ether.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DadosDBModel {
    private long id;
    private String valor_nf;
    private String cnpj_tomador;
    private String im_prestador;
    private String telefone_prestador;
    private String email_prestador;
    private String descricao_servico;
    private String endereco_prestador;
    private String municipio;
    private String cnae;

    private int status;
}
