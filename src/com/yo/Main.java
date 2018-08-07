package com.yo;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class Main {

    private static String FILENAME;
    private static final String csvTable = "SRL/SRL.csv";
    private static final String csvProduction = "SRL/SRL.csv";

    public static void main(String[] args) {
        if (args[0].equals("-yo")) {
            FILENAME = args[1];
            File file = new File(FILENAME);

            if (!file.exists()) {
                System.out.println("File does not exist!!!");
                return;
            }
            try {
                List<String> reservedWord = Arrays.asList(  "yoint", "yofloat", "yochar", "lag", "sehloiro", "yobool",
                                                            "eoq", "trab", "holy", "fon", "yogod", "circo", "soled",
                                                            "break", "true", "false", "printfi", "solado", "scanfi");
                List<String> symbols = Arrays.asList("{", "}", ";", "?", "\"", "(", ")", "=", ":", "[", "]");
                List<String> specialOperators = Arrays.asList("++", "--", "&&", "||", "==", "<=", "<", ">=", ">", "!=");

                LexicalAnalyzer lexical = new LexicalAnalyzer(file, reservedWord, symbols, specialOperators);
                List<Token> tokens = lexical.analyser();
                System.out.println(tokens);
                SyntacticAnalyzer syntactical = new SyntacticAnalyzer(csvTable, csvProduction);
                syntactical.analisar(tokens);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
