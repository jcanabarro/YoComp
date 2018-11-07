package com.yo;

import java.util.Stack;

class SemanticAnalyzer {

    void codeGenerator(String s, String prod, Stack<Token> pilha) {
        Token t_prod = new Token("nao_terminal", prod);

        Token t_inst, token, token_id;
        System.out.println(prod +" "+ s);
        System.out.println(pilha);
        switch(Integer.valueOf(s)) {
            case 1: // YOGOD,yogod ( ) { EXPR },6
                t_inst = pilha.pop();
                System.out.println("Codigo intermediario gerado com sucesso:" + t_inst.getCodigo());
                break;
            case 2: // EXPR,TIPO id EXPR,3
                break;
            case 3: // EXPR,DE,1
                break;
            case 4: // EXPR,OP,1
                break;
            case 5:// EXPR,INOUT,1
                break;
            case 6: // EXPR,ATR,1
                break;
            case 7: // ATR,id = STMT END,4
                token = pilha.pop();
                t_prod.setCodigo(token.getValor());
                break;
            case 8:// ATR,id = STMT,3
                break;
            case 9: // Reserved word yoint
            case 10: // Reserved word yofloat
            case 11: // Reserved word yochar
                token = pilha.pop();
                t_prod.setCodigo(token.getValor());
                break;
            case 12: // Integer value
                token = pilha.pop();
                t_prod.setTipo("int");
                t_prod.setCodigo(token.getValor());
                break;
            case 13: // Float value
                token = pilha.pop();
                t_prod.setTipo("float");
                t_prod.setCodigo(token.getValor());
                break;
            case 14: // Char value
                token = pilha.pop();
                t_prod.setTipo("char");
                t_prod.setCodigo(token.getValor());
                break;
            case 15: // True value
                token = pilha.pop();
                t_prod.setTipo("bool");
                t_prod.setCodigo(token.getValor());
                break;
            case 16: // False value
                token = pilha.pop();
                t_prod.setTipo("bool");
                t_prod.setCodigo(token.getValor());
                break;
            case 17: //
                break;
            case 18: //
                break;
            case 19: //
                break;
            case 20: //
                break;
            case 21: //
                break;
            case 22: //
                break;
            case 23: //
                break;
            case 24: //
                break;
            case 25: //
                break;
            case 26: //
                break;
            case 27: //
                break;
            case 28: //
                break;
            case 29: //
                break;
            case 30: //
                break;
            case 31: //
                break;
            case 32: //
                break;
            case 33: //
                break;
            case 34: //
                break;
            case 35: //
                break;
            case 36: //
                break;
            case 37: //
                break;
            case 38: //
                break;
            case 39: //
                break;
            case 40: //
                break;
            case 41: //
                break;
            case 42: //
                break;
            case 43: //
                break;
            case 44: //
                break;
            case 45: //
                break;
            case 46: //
                break;
            case 47: //
                break;
            case 48: //
                break;
            case 49: //
                break;

            case 67: // Reserved word solado
                token = pilha.pop();
                t_prod.setCodigo(token.getValor());
                break;
        }
//        System.out.println(t_prod.getCodigo());
    }
}
