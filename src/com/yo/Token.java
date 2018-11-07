package com.yo;

public class Token {

    private String attribute;
    private String value;
    private String error = "";
    private String code = "";
    private int row;

    Token(String attribute, String value, int row) {
        this.attribute = attribute;
        this.value = value;
        this.row = row;
    }

    public Token(String atributo, String valor) {
        this.attribute = atributo;
        this.value = valor;
    }

    public String getAttribute() {
        return this.attribute;
    }

    void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        if(this.error.equals("")) {
            return "<" + this.attribute + ", " + this.value + ">";
        } else {
            if(this.value != null) {
                return "<" + this.attribute + ", " + this.error + ", Char: '" + this.value +"', row: "+ this.row +">";
            }
            return "<" + this.attribute + ", " + this.error + ", row: "+ this.row + ", column: "+">";
        }
    }

    void setValue(String value) { this.value = value; }

    String getError() { return this.error; }

    void setError(String error) {
        this.error = error;
    }

    public int getRow() {
        return this.row;
    }

    void setRow(int row) { this.row = row; }

    void setCode(String code) {
        this.code = code;
    }
    String getCode() {
        return this.code;
    }

}
