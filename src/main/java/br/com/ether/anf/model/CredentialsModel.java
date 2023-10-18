package br.com.ether.anf.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CredentialsModel {
    private long id;
    private String plataform;
    private String login;
    private String passwd;

}
