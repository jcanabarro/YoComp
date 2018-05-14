package com.yo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class LexicalAnalyzer {
    private FileInputStream fileInputStream;
    private BufferedInputStream bufferInputStream;
    private int column, row;
    private List<String> reservedWords, symbols;

    LexicalAnalyzer(File file, List<String> reservedWords, List<String> symbols) throws FileNotFoundException {
        this.reservedWords = reservedWords;
        this.symbols = symbols;
        this.fileInputStream = new FileInputStream(file);
        this.bufferInputStream = new BufferedInputStream(this.fileInputStream);
        this.column = 0;
        this.row = 0;
    }

    private String FindPattern(String regex) throws IOException {
        String value = "";
        char currentPosition;
        while (this.bufferInputStream.available() > 0) {
            currentPosition = readChar();
            if (String.valueOf(currentPosition).matches(regex)) {
                value += currentPosition;
            } else {
                break;
            }
        }
        return value;
    }

    private void resetBuffer() throws IOException {
        this.bufferInputStream.reset();
        this.column--;
    }

    private char readChar() throws IOException {
        this.bufferInputStream.mark(2);
        char current = (char) this.bufferInputStream.read();
        this.column++;
        if (current == ' ') {
            return '\n';
        }
        while (current == '\n') {
            this.column = 0;
            this.row++;
            this.bufferInputStream.mark(2);
            current = (char) this.bufferInputStream.read();
        }
        return current;
    }

    protected List<Token> analyser() throws IOException {
        List<Token> tokens = new ArrayList<>();
        Token token = recognizeToken();
        while (token != null) {
            tokens.add(token);
            token = recognizeToken();
        }
        tokens.add(new Token("$", "yogod", this.column, this.row));
        return tokens;
    }

    private Token recognizeToken() throws IOException {
        Token token = null;
        char currentPosition = readChar();
        while (currentPosition == ' ' || currentPosition == '\n') {
            currentPosition = readChar();
        }

        String word = String.valueOf(currentPosition);
        if (word.matches("[0-9]")) {
            resetBuffer();
            token = FindNumber();
        } else if (word.matches("[a-z]")) {
            resetBuffer();
            token = FindReservedWord();
        } else if (word.matches("[()+\\-*/%.]")) {
            resetBuffer();
            token = FindOperator("oparithmetical");
        } else if (word.matches("[()!&*/|.]")) {
            resetBuffer();
            token = FindOperator("oplogical");
        } else {
            resetBuffer();
        }
        return token;
    }

    private Token FindNumber() throws IOException {
        Token number = new Token("num", "", this.column, this.row);
        String value = FindPattern("[0-9]");
        resetBuffer();
        if(readChar() == '.'){
            value += '.';
            value += FindPattern("[0-9]");
        } else {
            resetBuffer();
        }
        number.setValue(value);
        return number;
    }

    private Token FindReservedWord() throws IOException {
        Token reservedWord = new Token("", "", this.column, this.row);
        String value = FindPattern("[a-z]");
        if (this.reservedWords.contains(value)) {
            reservedWord.setValue(value);
            reservedWord.setAttribute("reserved");
        } else {
            reservedWord.setError("Nao e uma palavra reservada");
        }
        resetBuffer();
        return reservedWord;
    }

    private Token FindOperator(String attribute) throws IOException {
        Token operator = new Token(attribute, "", this.column, this.row);
        String current = String.valueOf(readChar());
        char nextOperator = readChar();
        if (current.equals(String.valueOf(nextOperator))) {
            current += nextOperator;
        } else {
            resetBuffer();
        }
        operator.setValue(String.valueOf(current));
        return operator;
    }
}