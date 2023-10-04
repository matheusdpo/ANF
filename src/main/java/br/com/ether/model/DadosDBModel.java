package br.com.ether.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
public class DadosDBModel {

    private String login;
    private String senha;
    private String valor_nf;
    private String data_competencia;
    private String cnpj_tomador;
    private String im_prestador;
    private String telefone_prestador;
    private String email_prestador;
    private String endereco_prestador;
    private String municipio;
    private String cnae;
    private String descricao_servico;
    private String chave;
    private int status;
}
