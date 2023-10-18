package br.com.ether.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DatasDBModel {
    private long id;
    private String value;
    private String cnpj_customer;
    private String im_customer;
    private String phone_customer;
    private String mail_customer;
    private String desc_customer;
    private String address_customer;
    private String city;
    private String cnae;

    private int status;
}
