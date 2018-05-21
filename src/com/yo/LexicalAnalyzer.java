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
    private int column, row;
    private List<String> reservedWords, symbols, specialOperators;

    LexicalAnalyzer(File file, List<String> reservedWords, List<String> symbols, List<String> specialOperators)
            throws FileNotFoundException {
        this.reservedWords = reservedWords;
        this.symbols = symbols;
        this.bufferInputStream = new BufferedInputStream(new FileInputStream(file));
        this.column = 0;
        this.row = 0;
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
        this.column--;
    }

    private char readChar() throws IOException {
        this.bufferInputStream.mark(2);
        char current = (char) this.bufferInputStream.read();
        this.column++;
        if (current == '\n')
            return ' ';
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
        } else if(word.matches("[?]")) {
            resetBuffer();
            readString('?');
        } else {
            token = ErrorStatement(word);
        }
        return token;
    }

    private Token FindNumber() throws IOException {
        Token number = new Token("num", "", this.column, this.row);
        String value = FindPattern("[0-9]");
        resetBuffer();
        number.setValue(value);
        if (readChar() == '.') {
            value += '.';
            String validation = FindPattern("[0-9]");
            value += validation;
            number.setValue(value);
            if(validation.equals("")){
                number.setError("Wrong float definition");
            }
        } else {
            resetBuffer();
        }
        return number;
    }

    private Token FindReservedWord() throws IOException {
        Token reservedWord = new Token("", "", this.column, this.row);
        String value = FindPattern("[a-zA-Z0-9]");
        resetBuffer();
        if (this.reservedWords.contains(value)) {
            reservedWord.setValue(value);
            reservedWord.setAttribute("reserved");
        } else {
            reservedWord.setAttribute("ID");
            reservedWord.setValue(value);
        }
        resetBuffer();
        return reservedWord;
    }

    private Token FindOperator(String attribute) throws IOException {
        Token operator = new Token(attribute, "", this.column, this.row);
        String current = String.valueOf(readChar());
        char nextOperator = readChar();
        if (specialOperators.contains(String.valueOf(current + nextOperator))) {
            current += nextOperator;
        } else {
            resetBuffer();
        }
        operator.setValue(String.valueOf(current));
        return operator;
    }

    private Token FindSymbols() throws IOException {
        Token symbol = new Token("symbol", "", this.column, this.row);
        String nextSymbol = String.valueOf(readChar());
        if (this.symbols.contains(nextSymbol)) {
            symbol.setValue(nextSymbol);
        } else {
            symbol.setError("Character not defined");
        }
        return symbol;
    }

    private Token FindString() throws IOException {
        Token symbol = new Token("", "", this.column, this.row);
        char nextSymbol = readChar();
        if(nextSymbol == '\''){
            String character = readString(nextSymbol);
            symbol.setAttribute("character");
            symbol.setValue('\'' + character + '\'');
        } else {
            String string = readString(nextSymbol);
            symbol.setAttribute("string");
            symbol.setValue('\"' + string + '\"');
        }
        return symbol;
    }

    private Token ErrorStatement(String word) throws IOException {
        Token error = new Token("", "", this.column, this.row);
        resetBuffer();
        error.setError("Character not defined");
        error.setValue(word);
        return  error;
    }
}