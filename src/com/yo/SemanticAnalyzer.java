package com.yo;

import java.util.Stack;

class SemanticAnalyzer {

    void codeGenerator(String s, String prod, Stack<Token> pilha_aux) {
        Token t_prod = new Token("nao_terminal", prod);

        Token t_if, t_inst;

        switch(Integer.valueOf(s)) {
            case 0:
                t_inst = pilha_aux.pop();
                System.out.println("Codigo intermediario gerado com sucesso:" + t_inst.getCode());
            case 1:
                t_if = pilha_aux.pop();
                t_inst = pilha_aux.pop();
                t_prod.setCode(t_if.getCode() + "\n" + t_inst.getCode());
                break;
            default:
                break;
        }
    }
}
