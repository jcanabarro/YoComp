package com.yo;

import java.util.Stack;

class SemanticAnalyzer {

    void codeGenerator(String s, String prod, Stack<Token> pilha) {
        Token t_prod = new Token("nao_terminal", prod);

        Token token_statement, token_expr, token_end, t_inst, token, token_if, token_else, token_else_expr;

//        System.out.println(prod +" "+ s);
//        System.out.println(pilha);
        switch(Integer.valueOf(s)) {
            case 1:
                t_inst = pilha.pop();
//                System.out.println("Codigo intermediario gerado com sucesso:" + t_inst.getCodigo());
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                token = pilha.pop();
                t_prod.setCodigo(token.getValor());
                break;
            case 8:
                break;
            case 9: // Reserved word yoint
            case 10: // Reserved word yofloat
            case 11: // Reserved word yochar
            case 12: // Reserved word yochar
                token = pilha.pop();
                t_prod.setCodigo(token.getValor());
                break;
            case 13: // Integer value
                value_declaration(pilha, t_prod, "int");
                break;
            case 14: // Float value
                value_declaration(pilha, t_prod, "float");
                break;
            case 15: // Char value
                value_declaration(pilha, t_prod, "char");
                break;
            case 16: // True value
                value_declaration(pilha, t_prod, "bool");
                break;
            case 17: // False value
                value_declaration(pilha, t_prod, "bool");
                break;
            case 21:
                System.out.println(prod +" "+ s);
                System.out.println(pilha);
                break;
            case 22:
            case 23:
                break;
            case 25: // IF eoq(STMT) {EXPR} END
            case 26:
                formatExpression(pilha, t_prod);
                break;
            case 27:
            case 28:
                token_if = pilha.pop();
                pilha.pop();
                token_statement = pilha.pop();
                pilha.pop();
                pilha.pop();
                token_expr = pilha.pop();
                pilha.pop();
                token_else = pilha.pop();
                pilha.pop();
                token_else_expr = pilha.pop();
//                pilha.pop();
//                token_end = pilha.pop(); Maybe this will be necessary in near future
                t_prod.setCodigo(token_if.getValor() + " " + token_statement.getValor() + "\n\t" + token_expr.getValor()
                        + "\n" + token_else.getValor() + "\n\t" + token_else_expr.getValor());
                break;
            case 29:
            case 30:
                formatExpression(pilha, t_prod);
                break;
            case 31:
            case 32:
                break;
            case 33:
            case 34:
                formatExpression(pilha, t_prod);
                break;
        }
    }

    private void value_declaration(Stack<Token> pilha, Token t_prod, String type) {
        Token token;
        token = pilha.pop();
        t_prod.setTipo(type);
        t_prod.setCodigo(token.getValor());
    }

    private void formatExpression(Stack<Token> pilha, Token t_prod) {
        Token token_if;
        Token token_statement;
        Token token_expr;
        token_if = pilha.pop();
        pilha.pop();
        token_statement = pilha.pop();
        pilha.pop();
        pilha.pop();
        token_expr = pilha.pop();
//        pilha.pop();
//        token_end = pilha.pop(); Maybe this will be necessary in near future
        t_prod.setCodigo(token_if.getValor() + " " + token_statement.getValor() + "\n\t" + token_expr.getValor());
    }
}
