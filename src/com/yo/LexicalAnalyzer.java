package com.yo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class LexicalAnalyzer {

    private File file;
    private FileInputStream fileInputStream;
    private BufferedInputStream bufferInputStream;
    private int column, row;
    private List<String> reservedWords, symbols;

    LexicalAnalyzer(File file, List<String> reservedWords, List<String> symbols) throws FileNotFoundException {
        this.file = file;
        this.reservedWords = reservedWords;
        this.symbols = symbols;
        this.fileInputStream = new FileInputStream(this.file);
        this.bufferInputStream = new BufferedInputStream(this.fileInputStream);
        this.column = 0;
        this.row = 0;
    }

    private String FindPattern(String regex) throws IOException {
        String value = "";
        char currentPosition;

        while(this.bufferInputStream.available() > 0) {
            currentPosition = readChar();
            if(String.valueOf(currentPosition).matches(regex)){
                value += currentPosition;
            } else {
                break;
            }
        }
        return value;
    }

    private char readChar() throws IOException{
        this.bufferInputStream.mark(2);
        char current = (char) this.bufferInputStream.read();
        this.column++;

        if(current == ' '){
            return '\n';
        }

        while(current =='\n'){
            this.column = 0;
            this.row++;
            this.bufferInputStream.mark(2);
            current = (char) this.bufferInputStream.read();
        }
        return current;
    }

    List<Token> analyser() throws IOException{
        List<Token> tokens = new ArrayList<>();
        Token token = recognizeWord();

        while(token != null){
            tokens.add(token);
            token = recognizeWord();
        }
        tokens.add(new Token("$", "yogod", this.column, this.row));
        return tokens;
    }

    private Token recognizeWord() throws IOException{
        Token token = null;
        char currentPosition = readChar();
        while(currentPosition == ' '){
            currentPosition = readChar();
        }
        if(currentPosition == '\n'){
            return null;
        }
        String word = String.valueOf(currentPosition);

        if(word.matches("[0-9]")){
            this.bufferInputStream.reset();
            this.column--;
            token = FindNumber();
        } else if(word.matches("[a-z]")){
            this.bufferInputStream.reset();
            this.column--;
            token = FindReservedWord();
        } else if(word.matches("[()+\\-*/.]")) {
            this.bufferInputStream.reset();
            this.column--;
            token = FindArithmeticalOperator();
        } else {
            this.bufferInputStream.reset();
            this.column--;
        }
        return token;
    }

    private Token FindNumber() throws IOException {
        Token number = new Token("num", "", this.column, this.row);
        String value = FindPattern("[0-9]");
        this.bufferInputStream.reset();
        this.column--;
        number.setValue(value);
        return number;
    }

    private Token FindReservedWord() throws IOException {
        Token reservedWord = new Token("reserved", "", this.column, this.row);
        String value = FindPattern("[a-z]");
        if(this.reservedWords.contains(value)){
            reservedWord.setValue(value);
        }else{
            reservedWord.setError("Nao e uma palavra reservada");
        }
        this.bufferInputStream.reset();
        this.column--;
        return reservedWord;
    }

    private Token FindArithmeticalOperator() throws IOException {
        Token arithmeticalOperator = new Token("oparithmetical", "", this.column, this.row);
        char current = readChar();
        arithmeticalOperator.setValue(String.valueOf(current));
        return arithmeticalOperator;
    }
}