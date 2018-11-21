package com.yo;

public class Token {
    private String attribute;
    private String value;
    private String error = "";
    private int row;

    private String local = "";
    private String operator = "";
    private String code = "";
    private String true_label = "";
    private String false_label = "";
    private String type = "";
    private String begin = "";
    private String end = "";

    public Token(String attribute, String valor, int row) {
        this.attribute = attribute;
        this.value = valor;
        this.row = row;
    }

    public Token(String attribute, String value) {
        this.attribute = attribute;
        this.value = value;
        this.row = 0;
    }

    String getAttribute() {
        return attribute;
    }

    void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    String getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (this.error.equals("")) {
            return "[" + attribute + ", " + value + "]";
        } else {
            if (this.value != null) {
                return "[" + attribute + ", " + this.error + ", Char: '" + this.value + "', linha: " + this.row + "]";
            }
            return "[" + attribute + ", " + this.error + ", linha: " + this.row + "]";
        }
    }

    void setValue(String value) {
        this.value = value;
    }

    String getError() {
        return error;
    }

    void setError(String error) {
        this.error = error;
    }

    public int getRow() {
        return row;
    }

    void setRow(int row) {
        this.row = row;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    String getCode() {
        return code;
    }

    void setCode(String code) {
        this.code = code;
    }

    public String getTrue_label() {
        return true_label;
    }

    void setTrue_label(String true_label) {
        this.true_label = true_label;
    }

    String getFalse_label() {
        return false_label;
    }

    void setFalse_label(String false_label) {
        this.false_label = false_label;
    }

    String getType() {
        return type;
    }

    void setType(String type) {
        this.type = type;
    }

    String getBegin() {
        return begin;
    }

    void setBegin(String begin) {
        this.begin = begin;
    }

    String getEnd() {
        return end;
    }

    void setEnd(String end) {
        this.end = end;
    }

    public String getOperator() {
        return operator;
    }

    void setOperator(String operator) {
        this.operator = operator;
    }

}
