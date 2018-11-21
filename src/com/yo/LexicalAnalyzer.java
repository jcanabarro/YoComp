package com.yo;

import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class LexicalAnalyzer {
    private BufferedInputStream bufferInputStream;
    private int row;
    private List<String> reservedWords, symbols, specialOperators;

    LexicalAnalyzer(File file, List<String> reservedWords, List<String> symbols, List<String> specialOperators)
            throws FileNotFoundException {
        this.reservedWords = reservedWords;
        this.symbols = symbols;
        this.bufferInputStream = new BufferedInputStream(new FileInputStream(file));
        this.row = 1;
        this.specialOperators = specialOperators;
    }

    private String FindPattern(String regex) throws IOException {
        String value = "";
        char currentPosition;
        while (this.bufferInputStream.available() > 0) {
            currentPosition = readChar();
            if ((currentPosition == ' ') || (currentPosition == '\n')) {
                break;
            } else if (String.valueOf(currentPosition).matches(regex)) {
                value += currentPosition;
            } else {
                break;
            }
        }
        return value;
    }

    private String readString(char symbol) throws IOException {
        String value = "";
        char currentPosition;
        while (this.bufferInputStream.available() > 0) {
            currentPosition = readChar();
            if (currentPosition != symbol) {
                value += currentPosition;
            } else {
                break;
            }
        }
        return value;
    }

    private void resetBuffer() throws IOException {
        this.bufferInputStream.reset();
    }

    private char readChar() throws IOException {
        this.bufferInputStream.mark(2);
        char current = (char) this.bufferInputStream.read();
        if (current == '\n') {
            this.row++;
            return ' ';
        }
        return current;
    }

    List<Token> analyser() throws IOException {
        List<Token> tokens = new ArrayList<>();
        Token token = recognizeToken();
        while (token != null) {
            tokens.add(token);
            if(!token.getError().equals("")) break;
            token = recognizeToken();
        }
        return tokens;
    }

    private char readPosition() throws IOException {

        char currentPosition = readChar();
        while (currentPosition == ' ' || currentPosition == '\n') {
            currentPosition = readChar();
        }
        return  currentPosition;
    }

    private Token recognizeToken() throws IOException {
        Token token = null;
        char currentPosition = readPosition();
        if (currentPosition == '?'){
            readString('?');
            currentPosition = readPosition();
        }

        String word = String.valueOf(currentPosition);
        if (word.matches("[0-9]")) {
            resetBuffer();
            token = FindNumber();
        } else if (word.matches("[a-zA-Z]")) {
            resetBuffer();
            token = FindReservedWord();
        } else if (word.matches("[+\\-*/%]")) {
            resetBuffer();
            token = FindOperator("oparithmetical");
        } else if (word.matches("[!&*/|]")) {
            resetBuffer();
            token = FindOperator("oplogical");
        } else if (word.matches("[=<>!]")) {
            resetBuffer();
            token = FindOperator("oprelational");
        } else if (word.matches("[()={}\\[\\]:;,]")) {
            resetBuffer();
            token = FindSymbols();
        } else if (word.matches("[\"']")) {
            resetBuffer();
            token = FindString();
        } else {
            if (!word.equals("\uFFFF")) // EOF
            token = ErrorStatement(word);
        }
        return token;
    }

    private Token FindNumber() throws IOException {
        Token number = new Token("int", null, this.row);
        String value = FindPattern("[0-9]");
        resetBuffer();
        number.setValue(value);
        if (readChar() == '.') {
            value += '.';
            String validation = FindPattern("[0-9]");
            value += validation;
            number.setValue(value);
            number.setAttribute("float");
            resetBuffer();
            if(validation.equals("")){
                number.setError("Wrong float definition");
            }
        } else {
            resetBuffer();
        }
        number.setRow(this.row);
        return number;
    }

    private Token FindReservedWord() throws IOException {
        Token reservedWord = new Token("", "", this.row);
        String value = FindPattern("[a-zA-Z0-9]");
        resetBuffer();
        if (this.reservedWords.contains(value)) {
            reservedWord.setValue(value);
            reservedWord.setAttribute("reserved");
        } else {
            reservedWord.setValue(value);
            reservedWord.setAttribute("id");
        }
        resetBuffer();
        return reservedWord;
    }

    private Token FindOperator(String attribute) throws IOException {
        Token operator = new Token(attribute, "", this.row);
        String current = String.valueOf(readChar());
        char nextOperator = readChar();
        if (specialOperators.contains(String.valueOf(current + nextOperator))) {
            current += nextOperator;
        } else {
            resetBuffer();
        }
        operator.setValue(String.valueOf(current));
        operator.setRow(this.row);
        return operator;
    }

    private Token FindSymbols() throws IOException {
        Token symbol = new Token("symbol", "", this.row);
        String nextSymbol = String.valueOf(readChar());
        if (this.symbols.contains(nextSymbol)) {
            symbol.setValue(nextSymbol);
        }
        symbol.setRow(this.row);
        return symbol;
    }

    private Token FindString() throws IOException {
        Token symbol = new Token("", "", this.row);
        char nextSymbol = readChar();
        if(nextSymbol == '\''){
            String character = readString(nextSymbol);
            symbol.setValue('\'' + character + '\'');
            symbol.setAttribute("char");
        } else {
            String string = readString(nextSymbol);
            symbol.setValue('\'' + string + '\'');
            symbol.setAttribute("string");
        }
        symbol.setRow(this.row);
        return symbol;
    }

    private Token ErrorStatement(String word) throws IOException {
        Token error = new Token("", "", this.row);
        resetBuffer();
        error.setError("Character not defined");
        error.setValue(word);
        error.setRow(this.row);
        return  error;
    }
}