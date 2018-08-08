package com.yo;

public class TokenRegra {

    private String value;
    private String tipo;
    private String codigo;
    private int local;
    private boolean condicao;


    public TokenRegra(){
        this.value = "";
        this.tipo = "";
        this.codigo = "";
        this.local = 0;
        this.condicao = false;
    }

    public TokenRegra(String value, String tipo, String codigo, int local) {
        this.value = value;
        this.tipo = tipo;
        this.codigo = codigo;
        this.local = local;
        this.condicao = false;
    }



    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void concatenarCodigo(String codigo){
        this.codigo.concat(codigo);
    }

    public void concatenarCodigo(String codigo, String codigo2){
        this.codigo.concat(" ");
        this.codigo.concat(codigo);
        this.codigo.concat(" ");
        this.codigo.concat(codigo2);
    }

    public int getLocal() {
        return local;
    }

    public void setLocal(int local) {
        this.local = local;
    }

    public boolean isCondicao() {
        return condicao;
    }

    public void setCondicao(boolean condicao) {
        this.condicao = condicao;
    }



}
