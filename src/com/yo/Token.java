package com.yo;

public class Token {
    private String atributo;
    private String valor;
    private String erro = "";
    private int l;

    // ATRIBUTOS PARA ANALISE SEMANTICA

    private String local = "";
    private String operador = "";
    private String codigo = "";
    private String verdadeiro = "";
    private String falso = "";
    private String tipo = "";
    private String inicio = "";
    private String fim = "";

    public Token(String atributo, String valor, int l) {
        this.atributo = atributo;
        this.valor = valor;
        this.l = l;
    }

    public Token(String atributo, String valor) {
        this.atributo = atributo;
        this.valor = valor;
        this.l = 0;
    }

    String getAtributo() {
        return atributo;
    }

    void setAtributo(String atributo) {
        this.atributo = atributo;
    }

    String getValor() {
        return valor;
    }

    @Override
    public String toString() {
        if (this.erro.equals("")) {
            return "[" + atributo + ", " + valor + "]";
        } else {
            if (this.valor != null) {
                return "[" + atributo + ", " + this.erro + ", Char: '" + this.valor + "', linha: " + this.l + "]";
            }
            return "[" + atributo + ", " + this.erro + ", linha: " + this.l + "]";
        }
    }

    void setValor(String valor) {
        this.valor = valor;
    }

    String getErro() {
        return erro;
    }

    void setErro(String erro) {
        this.erro = erro;
    }

    public int getL() {
        return l;
    }

    void setL(int l) {
        this.l = l;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    String getCodigo() {
        return codigo;
    }

    void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getVerdadeiro() {
        return verdadeiro;
    }

    void setVerdadeiro(String verdadeiro) {
        this.verdadeiro = verdadeiro;
    }

    String getFalso() {
        return falso;
    }

    void setFalso(String falso) {
        this.falso = falso;
    }

    String getTipo() {
        return tipo;
    }

    void setTipo(String tipo) {
        this.tipo = tipo;
    }

    String getInicio() {
        return inicio;
    }

    void setInicio(String inicio) {
        this.inicio = inicio;
    }

    String getFim() {
        return fim;
    }

    void setFim(String fim) {
        this.fim = fim;
    }

    public String getOperador() {
        return operador;
    }

    void setOperador(String operador) {
        this.operador = operador;
    }


}
