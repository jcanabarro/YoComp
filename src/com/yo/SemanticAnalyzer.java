package com.yo;

import java.util.Stack;

// TODO: Need apply 3 end code to expressions
// TODO: Need apply type verification
// TODO: Verify if productions with non-terminal END will case some problems

class SemanticAnalyzer {

    private int label_counter;
    private String codigo;

    Token codeGenerator(String s, String prod, Stack<Token> pilha) {
        Token t_prod = new Token("nao_terminal", prod);

        Token token;
        switch(Integer.valueOf(s)) {
            case 1:
                System.out.println("Codigo intermediario gerado com sucesso");
                break;
            case 2:
                Token token_type;
                token_type = pilha.pop();
                token = pilha.pop();
                t_prod.setCodigo(token_type.getCodigo() + " " + token.getValor());
                break;
            case 3:
            case 4:
            case 5:
            case 6:
                t_prod.setCodigo("\t" + pilha.pop().getCodigo());
                break;
            case 7: // attributions
            case 8: // attributions
                Token token_attr;
                token_attr = pilha.pop();
                pilha.pop();
                token = pilha.pop();
                t_prod.setCodigo(token_attr.getValor() + " = " + token.getCodigo());
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
            case 18:
            case 19:
                t_prod.setCodigo(pilha.pop().getCodigo());
                break;
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
                formatIf(pilha, t_prod);
                break;
            case 29: // IF ELSE
            case 30: // IF ELSE
                formatIfElse(pilha, t_prod);
                break;
            case 31: // WHILE
            case 32: // WHILE
                formatWhile(pilha, t_prod);
                break;
            case 33: // FOR
            case 34: // FOR
                formatFor(pilha, t_prod);
                break;
            case 35: // SWITCH
            case 36: // SWITCH
                formatSwitch(pilha, t_prod);
                break;
            case 37: // CASE
            case 38: // CASE
                formatCase(pilha, t_prod);
                break;
            case 41:
                token = pilha.pop();
                t_prod.setCodigo(token.getCodigo());
                break;
            case 42:
                token = pilha.pop();
                t_prod.setCodigo(token.getCodigo());
                break;
            case 43:
            case 44:
            case 45:
                token = pilha.pop();
                t_prod.setCodigo(token.getCodigo());
                break;
            case 46:
                token = pilha.pop();
                t_prod.setCodigo(token.getValor());
                break;
            case 47:
                pilha.pop();
                token = pilha.pop();
                t_prod.setCodigo("(" + token.getCodigo() + ")");
                break;
            case 48:
            case 49:
            case 50:
            case 51:
                formatExpression(pilha, t_prod);
                break;
            case 52:
                Token negative;
                negative = pilha.pop();
                token = pilha.pop();
                t_prod.setCodigo(negative.getValor() + token.getCodigo());
                break;
            case 53:
            case 54:
                formatExpression(pilha, t_prod);
                break;
            case 55:
                token = pilha.pop();
                t_prod.setCodigo(token.getValor());
                break;
            case 56:
                token = pilha.pop();
                t_prod.setCodigo(token.getCodigo());
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
            case 69:
            case 70:
                token = pilha.pop();
                t_prod.setCodigo(token.getValor() + pilha.pop().getValor());
                break;
            case 71: break;
            case 72: break;
        }
//        if(!t_prod.getCodigo().equals(""))
//            System.out.println(t_prod.getCodigo());
        return t_prod;
    }

    private void formatExpression(Stack<Token> pilha, Token t_prod) {
        Token first_value, expression, second_value;
        first_value = pilha.pop();
        expression = pilha.pop();
        second_value = pilha.pop();
        t_prod.setCodigo(first_value.getCodigo() + " " + expression.getCodigo() + " " + second_value.getCodigo());
    }

    private void formatWhile(Stack<Token> pilha, Token t_prod) {
        Token token_statement, token_expr;
        pilha.pop();
        pilha.pop();
        token_statement = pilha.pop();
        pilha.pop();
        pilha.pop();
        token_expr = pilha.pop();
        t_prod.setInicio(this.getLabelCounter());
        t_prod.setFim(this.getLabelCounter());
        codigo = t_prod.getInicio() + " : \n";
        codigo += "if " + token_statement.getCodigo() + " = false goto "+ t_prod.getFim() + "\n";
        codigo += token_expr.getCodigo() + "\n"+ "goto "+ t_prod.getInicio() + "\n" + t_prod.getFim()+" : ";
        t_prod.setCodigo(codigo);
    }

    // TODO remove this, because this it's only for debug, just only for the final version
    private void printAttr(Token token_attr) {
        System.out.println(token_attr.getCodigo());
        System.out.println(token_attr.getAtributo());
        System.out.println(token_attr.getValor());
    }

    private void formatOperation(Stack<Token> pilha, Token t_prod, String type) {
        Token t_op;
        t_op = pilha.pop();
        t_prod.setCodigo(t_op.getValor());
        t_prod.setTipo(type);
        t_prod.setOperador(t_op.getValor());
    }

    private void formatFor(Stack<Token> pilha, Token t_prod) {
        Token iter_var,op_relational, stop_value, unary_value, expr;
        pilha.pop();
        pilha.pop();
        iter_var = pilha.pop();
        pilha.pop();
        pilha.pop();
        pilha.pop();
        pilha.pop();
        op_relational = pilha.pop();
        stop_value = pilha.pop();
        pilha.pop();
        unary_value = pilha.pop();
        pilha.pop();
        pilha.pop();
        expr = pilha.pop();
        String stop_condition = iter_var.getValor() + " " + op_relational.getCodigo() + " " + stop_value.getCodigo();
        t_prod.setInicio(this.getLabelCounter());
        t_prod.setFim(this.getLabelCounter());
        codigo = "";
        codigo += t_prod.getInicio() + " : \n";
        codigo += expr.getCodigo() + "\n";
        codigo += "if " + stop_condition + " = false goto " + t_prod.getFim() + "\n";
        codigo += unary_value.getCodigo() + "\n"+ "goto "+ t_prod.getInicio() + "\n" + t_prod.getFim()+" : ";
        t_prod.setCodigo(codigo);
        System.out.println(t_prod.getCodigo());
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

    private void value_declaration(Stack<Token> pilha, Token t_prod, String type) {
        Token token;
        token = pilha.pop();
        t_prod.setTipo(type);
        t_prod.setCodigo(token.getValor());
    }

    private void inOutFormat(Stack<Token> pilha, Token t_prod) {
        Token token_in_out;
        Token token;
        token_in_out = pilha.pop();
        pilha.pop();
        token = pilha.pop();
        pilha.pop();
        if (token_in_out.getValor().equals("printfi"))
            t_prod.setCodigo("print " + token.getCodigo());
        else
            t_prod.setCodigo("read " + token.getValor());
    }

    private void formatSwitch(Stack<Token> pilha, Token t_prod) {

    }

    private void formatIf(Stack<Token> pilha, Token t_prod) {
        Token token_statement, token_expr;
        pilha.pop();
        pilha.pop();
        token_statement = pilha.pop();
        pilha.pop();
        pilha.pop();
        token_expr = pilha.pop();
        t_prod.setVerdadeiro(this.getLabelCounter());
        t_prod.setFalso(this.getLabelCounter());
        codigo = token_statement.getCodigo() + "\n";
        codigo += "if " + token_statement.getCodigo() + " = false goto " + t_prod.getFalso()+"\n";
        codigo += token_expr.getCodigo() + "\n";
        codigo += t_prod.getFalso() + " : \n";
        t_prod.setCodigo(codigo);
    }


    private void formatIfElse(Stack<Token> pilha, Token t_prod) {
        Token token_statement;
        Token token_expr;
        Token token_else_expr;
        pilha.pop();
        pilha.pop();
        token_statement = pilha.pop();
        pilha.pop();
        pilha.pop();
        token_expr = pilha.pop();
        pilha.pop();
        pilha.pop();
        pilha.pop();
        token_else_expr = pilha.pop();
        t_prod.setVerdadeiro(this.getLabelCounter());
        t_prod.setFalso(this.getLabelCounter());
        String str_aux = this.getLabelCounter();
        codigo = token_statement.getCodigo() + "\n";
        codigo += "if " + token_statement.getCodigo() + " false goto " + t_prod.getFalso()+"\n";
        codigo += token_expr.getCodigo() + "\n";
        codigo += "goto " + str_aux + "\n";
        codigo += t_prod.getFalso() + " : \n";
        codigo += token_else_expr.getCodigo() + "\n";
        codigo += str_aux + " : \n";
        t_prod.setCodigo(codigo);
    }

    private String getLabelCounter(){
        label_counter++;
        return "Label " + this.label_counter;
    }
}
