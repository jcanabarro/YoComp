package com.yo;

import java.util.Stack;

// Verificar se o END vai dar erro nos casos de expressoes

class SemanticAnalyzer {

    Token codeGenerator(String s, String prod, Stack<Token> pilha) {
        Token t_prod = new Token("nao_terminal", prod);

        Token t_inst, token;
        System.out.print(prod + " " + s + " Pilha: ");
        System.out.println(pilha);
        switch(Integer.valueOf(s)) {
            case 1:
                t_inst = pilha.pop();
                System.out.println("Codigo intermediario gerado com sucesso");
                break;
            case 2:
                Token token_type;
                token_type = pilha.pop();
                token = pilha.pop();
                t_prod.setCodigo(token_type.getValor() + " " + token.getValor());
                break;
            case 3: break;
            case 4: break;
            case 5: break;
            case 6: break;
            case 7: // attributions
            case 8: // attributions
                Token token_attr, token_equals;
                token_attr = pilha.pop();
                t_prod.setOperador(pilha.pop().getValor());
                token = pilha.pop();
                t_prod.setCodigo(token_attr.getValor() + " " + token.getValor());
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
            case 18: break;
            case 19: break;
            case 20: // SCANF
            case 21: // PRINTF
            case 22: // SCANF
            case 23: // PRINTF
                inOutFormat(pilha, t_prod);
                break;
            case 24: // OUTPUTVALUE == id
            case 25: // OUTPUTVALUE == string
            case 26: // OUTPUTVALUE == TIPO
                token = pilha.pop();
                t_prod.setCodigo(token.getValor());
                break;
            case 27: // IF
            case 28: // IF
                formatExpression(pilha, t_prod);
                break;
            case 29: // IF ELSE
            case 30: // IF ELSE
                formatIfElse(pilha, t_prod);
                break;
            case 31: // WHILE
            case 32: // WHILE
                formatExpression(pilha, t_prod);
                break;
            case 33: // FOR
            case 34: // FOR
                formatFor(pilha, t_prod);
                break;
            case 35: // SWITCH
            case 36: // SWITCH
                formatExpression(pilha, t_prod);
                break;
            case 37: // CASE
            case 38: // CASE
                formatCase(pilha, t_prod);
                break;
            case 53:
                break;
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
                formatOperation(pilha, t_prod, "bool");
                break;
            case 63:
            case 64:
            case 65:
            case 66:
                formatOperation(pilha, t_prod, "int");
                break;
            case 67:
            case 68:
                formatOperation(pilha, t_prod, "bool");
                break;
        }
        return t_prod;
    }

    private void formatOperation(Stack<Token> pilha, Token t_prod, String type) {
        Token t_op;
        t_op = pilha.pop();
        t_prod.setCodigo(t_op.getValor());
        t_prod.setTipo(type);
        t_prod.setOperador(t_op.getValor());
    }

    private void formatIfElse(Stack<Token> pilha, Token t_prod) {
        Token token_if;
        Token token_statement;
        Token token_expr;
        Token token_else;
        Token token_else_expr;
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
        t_prod.setCodigo(token_if.getValor() + " " + token_statement.getValor() + "\n\t" + token_expr.getValor()
                + "\n" + token_else.getValor() + "\n\t" + token_else_expr.getValor());
    }

    private void formatFor(Stack<Token> pilha, Token t_prod) {
        Token token;
        Token token_for, token_equals, token_valor, token_oprelacional, token_valor1, token_unario;
        token_for = pilha.pop();
        pilha.pop();
        token = pilha.pop();
        token_equals = pilha.pop();
        token_valor = pilha.pop();
        pilha.pop();
        pilha.pop();
        token_oprelacional = pilha.pop();
        token_valor1 = pilha.pop();
        pilha.pop();
        token_unario = pilha.pop();

        t_prod.setCodigo(token_for.getValor() + " " + token.getValor() + " " + token_equals.getValor()
        + " " + token_valor.getValor() + " " + token.getValor() + " " + token_oprelacional.getValor() +
        " " +token_valor1.getValor() + " " + token_unario.getValor());
    }

    private void formatCase(Stack<Token> pilha, Token t_prod) {
        Token token;
        Token token_case, token_break;
        token_case = pilha.pop();
        token = pilha.pop();
        pilha.pop();
        pilha.pop();
        token_break = pilha.pop();
        t_prod.setCodigo(token_case.getValor() + " " + token.getValor() + " " + token_break.getValor());
    }

    private void inOutFormat(Stack<Token> pilha, Token t_prod) {
        Token token_in_out;
        Token token;
        token_in_out = pilha.pop();
        pilha.pop();
        token = pilha.pop();
        pilha.pop();
        t_prod.setCodigo(token_in_out.getValor() + " " + token.getValor());
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
        t_prod.setCodigo(token_if.getValor() + " " + token_statement.getValor() + "\n\t" + token_expr.getValor());
    }
}
