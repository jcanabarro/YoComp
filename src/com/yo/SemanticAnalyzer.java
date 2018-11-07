package com.yo;

import java.util.Stack;

class SemanticAnalyzer {

    void codeGenerator(String s, String prod, Stack<Token> pilha_aux) {
        Token t_prod = new Token("nao_terminal", prod);

        Token t_if, t_inst;
        System.out.println(prod + ' ' + s);
        switch(Integer.valueOf(s)) {
            case 1: // YOGOD
                t_inst = pilha_aux.pop();
                System.out.println("Codigo intermediario gerado com sucesso:" + t_inst.getCodigo());
            case 24:
                t_if = pilha_aux.pop();
                t_inst = pilha_aux.pop();
                t_prod.setCodigo(t_if.getCodigo() + "\n" + t_inst.getCodigo());
                break;
            case 9:
                break;
        }
    }
}
