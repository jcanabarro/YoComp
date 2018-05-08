package com.yo;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class Main {

    private static String FILENAME;

    public static void main(String[] args) {
        if(args[0].equals("-yo")) {
            FILENAME = args[1];
            File file = new File(FILENAME);

            if(!file.exists()) {
                System.out.println("File does not exist!!!");
                return;
            } try {
                List<String> reservedWord = Arrays.asList("yoda", "god");
                List<String> symbols = Arrays.asList("{", "}");

                LexicalAnalyzer lexical = new LexicalAnalyzer(file, reservedWord, symbols);
                List<Token> tokens = lexical.analyser();
                System.out.println(tokens);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
