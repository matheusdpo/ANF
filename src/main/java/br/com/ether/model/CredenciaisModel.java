package br.com.ether.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CredenciaisModel {
    private long id;
    private String plataforma;
    private String login;
    private String senha;

}
