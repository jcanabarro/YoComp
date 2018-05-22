package com.yo;

public class Token {

    private String attribute;
    private String value;
    private String error = "";
    private int row;

    Token(String attribute, String value, int row) {
        this.attribute = attribute;
        this.value = value;
        this.row = row;
    }

    public String getAttribute() {
        return this.attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
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

    public String getError() { return this.error; }

    public void setError(String error) {
        this.error = error;
    }

    public int getRow() {
        return this.row;
    }

    public void setRow(int row) { this.row = row; }

}
