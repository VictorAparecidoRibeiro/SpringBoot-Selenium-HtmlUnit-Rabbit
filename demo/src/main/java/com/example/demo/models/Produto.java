package com.example.demo.models;

import java.io.Serializable;

public class Produto implements Serializable {

    private String urlImagem;

    private String descricao;

    private String moeda;

    private Double valor;

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getMoeda() {
        return moeda;
    }

    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }

    public String getValorFormatado(){
        String retorno = "";
        if(this.valor != null && this.moeda != null){
            retorno = this.moeda + " " + this.valor;
        }

        return retorno;
    }
}
