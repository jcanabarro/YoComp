package com.yo;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


// TODO: Need apply 3 end code to expressions
// TODO: Need apply type verification

class SemanticAnalyzer {

    private String code;
    private Integer label_counter;
    private Integer temp_counter;
    private List<String> variable_declaration;
    private List<String> variable_type;

    SemanticAnalyzer(List<String> variable_declaration, List<String> variable_type) {
        this.code = "";
        this.label_counter = 0;
        this.temp_counter = 0;
        this.variable_declaration = variable_declaration;
        this.variable_type = variable_type;
    }

    Token codeGenerator(String s, String prod, Stack<Token> pilha) {
        Token t_prod = new Token("nao_terminal", prod);

        Token token;
        switch (Integer.valueOf(s)) {
            case 1:
                if (this.variable_type.contains("later")) {
                    int index = this.variable_type.indexOf("later");
                    t_prod.setErro("Variavel " + this.variable_declaration.get(index) + " não foi declarada");
                } else {
                    System.out.println("Codigo intermediario gerado com sucesso");
                }
                break;
            case 2:
                Token token_type;
                token_type = pilha.pop();
                token = pilha.pop();
                if (this.variable_declaration.contains(token.getValor())) {
                    int index = this.variable_declaration.indexOf(token.getValor());
                    if (this.variable_type.get(index).equals("later")) {
                        this.variable_type.set(index, token_type.getTipo());
                    } else if (this.variable_type.get(index).equals(token_type.getTipo())) {
                        t_prod.setCodigo(token_type.getCodigo() + " " + token.getValor());
                    } else if (token_type.getTipo().equals("float") && this.variable_type.get(index).equals("int")) {
                        t_prod.setCodigo(token_type.getCodigo() + " " + token.getValor());
                    } else {
                        t_prod.setErro("Uma variavel do tipo " + token_type.getTipo() + " não pode receber um tipo " + this.variable_type.get(index));
                    }
                } else {
                    t_prod.setErro("Variavel " + token.getValor() + " não foi declarada");
                }
                break;
            case 3:
            case 4:
            case 5:
            case 6:
                token = pilha.pop();
                t_prod.setCodigo(token.getCodigo());
                t_prod.setTipo(token.getTipo());
                break;
            case 7: // attributions
            case 8: // attributions
                Token token_attr;
                token_attr = pilha.pop();
                pilha.pop();
                token = pilha.pop();
                code = token_attr.getValor() + " = " + token.getCodigo();
                List<String> assembly_expression = Arrays.asList(code.split(" "));
                if (assembly_expression.size() > 3) {
                    ArrayList<String> expression = new ArrayList<>(assembly_expression);
                    expression.remove(0);
                    expression.remove(0);
                    String tac = "";
                    List<String> formattedTac = format3End(expression, tac, token_attr.getValor());
                    code = String.join("\n ", formattedTac);
                }
                t_prod.setTipo(token.getTipo());
                if (!this.variable_declaration.contains(token_attr.getValor())) {
                    this.variable_declaration.add(token_attr.getValor());
                }
                int index = this.variable_declaration.indexOf(token_attr.getValor());
                if (this.variable_type.size() > index) {
                    this.variable_type.add(index, token.getTipo());
                } else {
                    this.variable_type.add(token.getTipo());
                }
                t_prod.setCodigo(code);
                break;
            case 9: // Reserved word yoint
                typeDeclaration(pilha, t_prod, "int");
                break;
            case 10: // Reserved word yofloat
                typeDeclaration(pilha, t_prod, "float");
                break;
            case 11: // Reserved word yochar
                typeDeclaration(pilha, t_prod, "char");
                break;
            case 12: // Reserved word yobool
                typeDeclaration(pilha, t_prod, "bool");
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
            case 37: // CASE
            case 38: // CASE
            case 39: // CASE
            case 40: // CASE
                break;
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
                passValueAndType(pilha, t_prod);
                break;
            case 46:
                passValueAndTypeID(pilha, t_prod);
                break;
            case 47:
                pilha.pop();
                token = pilha.pop();
                t_prod.setCodigo("(" + token.getCodigo() + ")");
                t_prod.setTipo(token.getTipo());
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
                passValueAndTypeID(pilha, t_prod);
                break;
            case 56:
                passValueAndType(pilha, t_prod);
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
                code += this.getTempCounter() + " = " + token.getValor() + " + 1\n";
                t_prod.setCodigo(token.getValor() + " = " + this.getSpecialTempCounter());
                break;
            case 71:
                break;
            case 72:
                break;
        }
//        // TODO remove this after works are finished
//        if(!t_prod.getCodigo().equals(""))
//            System.out.println(t_prod.getCodigo());
        return t_prod;
    }

    private void typeDeclaration(Stack<Token> pilha, Token t_prod, String type) {
        Token token;
        token = pilha.pop();
        t_prod.setCodigo(token.getValor());
        t_prod.setTipo(type);
    }

    private void passValueAndTypeID(Stack<Token> pilha, Token t_prod) {
        Token token;
        token = pilha.pop();
        t_prod.setCodigo(token.getValor());
        this.variable_declaration.add(token.getValor());
        this.variable_type.add("later");
    }

    private void passValueAndType(Stack<Token> pilha, Token t_prod) {
        Token token;
        token = pilha.pop();
        t_prod.setCodigo(token.getCodigo());
        if (token.getTipo().isEmpty()) {
            String[] split_declaration = token.getCodigo().split(" ");
            if (!this.variable_declaration.contains(split_declaration[0]) && split_declaration.length <= 1) {
                this.variable_declaration.add(token.getValor());
                this.variable_type.add("later");
            }
        }
        t_prod.setTipo(token.getTipo());
    }

    private List<String> format3End(List<String> assembly_expression, String expr, String attribution) {
        if (assembly_expression.size() > 2) {
            String temp = this.getTempCounter();
            expr += temp + " = ";
            expr += assembly_expression.get(0) + " ";
            assembly_expression.remove(0);
            expr += assembly_expression.get(0) + " ";
            assembly_expression.remove(0);
            expr += assembly_expression.get(0) + " ";
            assembly_expression.remove(0);
            assembly_expression.add(0, temp);
            expr += "\n";
            return format3End(assembly_expression, expr, attribution);
        }
        expr += attribution + " = " + this.getSpecialTempCounter();
        return Collections.singletonList(expr);
    }

    private void formatExpression(Stack<Token> pilha, Token t_prod) {
        Token first_value, expression, second_value;
        first_value = pilha.pop();
        expression = pilha.pop();
        second_value = pilha.pop();
        String fv_type = first_value.getTipo();
        String sv_type = second_value.getTipo();

        if (!fv_type.equals("bool") && !sv_type.equals("bool") && !expression.getTipo().equals("bool")) {
            if (fv_type.equals(sv_type) || (!fv_type.equals("char") && !sv_type.equals("char"))) {
                if (fv_type.equals("char") && sv_type.equals("char")) {
                    t_prod.setTipo("char");
                } else if (fv_type.equals("int") && sv_type.equals("int")) {
                    t_prod.setTipo("int");
                } else if (fv_type.equals("") && !sv_type.equals("")) {
                    t_prod.setTipo(sv_type);
                    if (!this.variable_declaration.contains(first_value.getCodigo())) {
                        this.variable_declaration.add(first_value.getCodigo());
                    }
                    int index = this.variable_declaration.indexOf(first_value.getCodigo());
                    this.variable_type.add(index, sv_type);
                } else if (sv_type.equals("") && !fv_type.equals("")) {
                    t_prod.setTipo(fv_type);
                    if (!this.variable_declaration.contains(second_value.getCodigo())) {
                        this.variable_declaration.add(second_value.getCodigo());
                    }
                    int index = this.variable_declaration.indexOf(second_value.getCodigo());
                    this.variable_type.add(index, fv_type);
                } else {
                    t_prod.setTipo("float");
                }
                t_prod.setCodigo(first_value.getCodigo() + " " + expression.getCodigo() + " " + second_value.getCodigo());
            } else {
                t_prod.setCodigo("");
                t_prod.setErro("Uma variável do tipo " + fv_type + " não pode operar com uma do tipo " + sv_type);
            }
        } else if (fv_type.equals("bool") && sv_type.equals("bool") && !expression.getTipo().equals("bool")) {
            t_prod.setErro("Operador não é booleano");
        } else if ((fv_type.equals("bool") && sv_type.equals("bool") || fv_type.equals("") || sv_type.equals("")) && expression.getTipo().equals("bool")) {
            if (fv_type.equals("") && !sv_type.equals("")) {
                t_prod.setTipo(sv_type);
                if (!this.variable_declaration.contains(first_value.getCodigo())) {
                    this.variable_declaration.add(first_value.getCodigo());
                }
                int index = this.variable_declaration.indexOf(first_value.getCodigo());
                this.variable_type.add(index, sv_type);
            } else if (sv_type.equals("") && !fv_type.equals("")) {
                t_prod.setTipo(fv_type);
                if (!this.variable_declaration.contains(second_value.getCodigo())) {
                    this.variable_declaration.add(second_value.getCodigo());
                }
                int index = this.variable_declaration.indexOf(second_value.getCodigo());
                this.variable_type.add(index, fv_type);
            } else {
                t_prod.setTipo(expression.getTipo());
            }
            t_prod.setCodigo(first_value.getCodigo() + " " + expression.getCodigo() + " " + second_value.getCodigo());
        }  else if (!fv_type.equals("bool") && !sv_type.equals("bool") && expression.getTipo().equals("bool")) {
            t_prod.setErro("Operador booleano suporta apenas operadores booleanos");
        }
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
        code = t_prod.getInicio() + " : \n";
        code += "if " + token_statement.getCodigo() + " = false goto " + t_prod.getFim() + "\n";
        code += token_expr.getCodigo() + "\n" + "goto " + t_prod.getInicio() + "\n" + t_prod.getFim() + " : ";
        t_prod.setCodigo(code);
    }

    private void formatOperation(Stack<Token> pilha, Token t_prod, String type) {
        Token t_op;
        t_op = pilha.pop();
        t_prod.setCodigo(t_op.getValor());
        t_prod.setTipo(type);
        t_prod.setOperador(t_op.getValor());
    }

    private void formatFor(Stack<Token> pilha, Token t_prod) {
        Token iter_var, op_relational, stop_value, unary_value, expr;
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
        code = "";
        code += t_prod.getInicio() + " : \n";
        code += expr.getCodigo() + "\n";
        code += "if " + stop_condition + " = false goto " + t_prod.getFim() + "\n";
        code += unary_value.getCodigo() + "\n" + "goto " + t_prod.getInicio() + "\n" + t_prod.getFim() + " : ";
        t_prod.setCodigo(code);
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
        code = token_statement.getCodigo() + "\n";
        code += "if " + token_statement.getCodigo() + " = false goto " + t_prod.getFalso() + "\n";
        code += token_expr.getCodigo() + "\n";
        code += t_prod.getFalso() + " : \n";
        t_prod.setCodigo(code);
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
        code = token_statement.getCodigo() + "\n";
        code += "if " + token_statement.getCodigo() + " = false goto " + t_prod.getFalso() + "\n";
        code += token_expr.getCodigo() + "\n";
        code += "goto " + str_aux + "\n";
        code += t_prod.getFalso() + " : \n";
        code += token_else_expr.getCodigo() + "\n";
        code += str_aux + " : \n";
        t_prod.setCodigo(code);
    }

    private String getLabelCounter() {
        label_counter++;
        return "Label " + this.label_counter;
    }

    private String getTempCounter() {
        temp_counter++;
        return "T" + this.temp_counter;
    }

    private String getSpecialTempCounter() {
        return "T" + this.temp_counter;
    }
}
