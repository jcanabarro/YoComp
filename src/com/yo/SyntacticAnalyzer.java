package com.yo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.logging.Logger;
import java.util.logging.Level;

class SyntacticAnalyzer {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private String[] tableHeader = null;
    private List<String[]> table;
    private String[] productionsHeader = null;
    private List<String[]> productions;
    private Stack<String> stack;

    private List<Integer> lambda_inst = null;
    private List<Integer> lambda_if = null;

    private List<TokenRegra> tokenRule = new ArrayList<>();

    public SyntacticAnalyzer(String csvTable, String csvProd) throws IOException {
        MyLogger.setup();
        LOGGER.setLevel(Level.INFO);
        this.table = readCSV(csvTable, true);
        LOGGER.finest("Table read successfully");
        this.productions = readCSV(csvProd, false);
        LOGGER.finest("Productions read successfully");
        this.stack = new Stack<>();
        LOGGER.finest("Initialized stack");
        pushInt(0);
        LOGGER.finest("Adding initial states");
//        this.lambda_inst = Arrays.asList(3,7, 59,39,4,6,27,10,111,55,53, 141, 5, 112, 114, 125,142,123,107, 88, 9,106);
//        this.lambda_if = Arrays.asList(106);
    }

    public String analyzer(List<Token> tokens) {

        int i = 0;
        LOGGER.info("Initializing analysis");
        while (i < tokens.size()) {

            LOGGER.info("<b>Check token " + i + "</b>");
            LOGGER.info("Stack: " + prodLog(this.stack.toString()));
            int s = popInt();
            pushInt(s);

            LOGGER.info("Top of the stack: " + s);
            String a = tokens.get(i).getAttribute();

            if (s == 43) {
                switch (a) {
                    case ("+"):
                        break;
                    case ("-"):
                        break;
                    case ("/"):
                        break;
                    case ("*"):
                        break;
                    case ("<"):
                        break;
                    case ("<="):
                        break;
                    case (">"):
                        break;
                    case (">="):
                        break;
                    case ("=="):
                        break;
                    case ("!="):
                        break;
                    default:
                        a = "lambda";
                        break;
                }
            }
            if (s == 11) {
                switch (a) {
                    case ("}"):
                        break;
                    case ("break"):
                        break;
                    default:
                        a = "$";
                        break;
                }
            }

            if (this.lambda_if.contains(s)) {
                switch (a) {
                    case ("else"):
                        break;
                    default:
                        a = "lambda";
                        break;
                }
            }

            if (this.lambda_inst.contains(s)) {
                switch (a) {
                    case ("if"):
                        break;
                    case ("while"):
                        break;
                    case ("for"):
                        break;
                    case ("switch"):
                        break;
                    case ("int"):
                        break;
                    case ("char"):
                        break;
                    case ("bool"):
                        break;
                    case ("id"):
                        break;
                    case ("read"):
                        break;
                    case ("printf"):
                        break;
                    case (")"):
                        break;
                    default:
                        a = "lambda";
                        break;
                }
            }
            LOGGER.info("Read token: " + a);
            String cell;
            String[] parser = null;
            String output;

            try {
                cell = cellValue(s, a, true);
                LOGGER.info("Value of the cell [" + s + ", " + a + "]: " + cell);
                parser = parserCell(cell);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Error");
            }
            if (parser == null) {
                LOGGER.warning("Null parser, arrive to the error state.");

                List<String> expectedValues = this.findExpected(s);
                if (a.equals("$")) {
                    output = "We did not find one of the following values: " + expectedValues.toString();
                } else {
                    output = "Error on line" + tokens.get(i).getRow();
                    output += " read vale: " + a + " when expected one of this values: ";
                    output += expectedValues.toString();
                }
                //buscar valores esperados.
                return output;
            }
            switch (parser[0]) {
                case "E":
                    LOGGER.info("Stacking Up " + a + " and " + parser[1]);
                    this.stack.push(a);
                    this.stack.push(parser[1]);
                    LOGGER.finest(a + " e " + parser[1] + " empilhado com sucesso");
                    if ((s != 43 && !this.lambda_inst.contains(s) && !this.lambda_if.contains(s)) || (!a.equals("lambda") && !a.equals("$"))) {
                        i++;
                    }
                    break;
                case "R":
                    LOGGER.info("Reduzindo pela produção " + parser[1]);
                    // Antes tinha essa celula declarada, pode dar ruim usar a mesma variavel
                    cell = cellValue(Integer.valueOf(parser[1]), "len", false);
                    LOGGER.finest("size of the production: " + cell);
                    String prod = cellValue(Integer.valueOf(parser[1]), "A", false);
                    LOGGER.info("Size of the production " + prodLog(Objects.requireNonNull(prod)) + " " + parser[1] + ": " + cell);
                    int tamanho = 2 * Integer.valueOf(Objects.requireNonNull(cell));
                    LOGGER.info("Number of elements who will be dispatched: " + tamanho);
                    for (int j = 0; j < tamanho; j++) {
                        String p = stack.pop();
                        if (j % 2 == 1 && tamanho == 2) {
                            switch (prod) {
                                case "<tipo>": {
                                    TokenRegra auxTK = new TokenRegra("tipo", p, p, tokenRule.size());
                                    tokenRule.add(auxTK);
                                    break;
                                }
                                case "<op_log>": {
                                    TokenRegra auxTK = new TokenRegra("op_log", p, p, tokenRule.size());
                                    tokenRule.add(auxTK);
                                    break;
                                }
                                case "<op_rel>": {
                                    TokenRegra auxTK = new TokenRegra("op_rel", p, p, tokenRule.size());
                                    tokenRule.add(auxTK);
                                    break;
                                }
                                case "<op_arit>": {
                                    TokenRegra auxTK = new TokenRegra("op_arit", p, p, tokenRule.size());
                                    tokenRule.add(auxTK);
                                    break;
                                }
                                case "<termo>": {
                                    TokenRegra auxTK = new TokenRegra("termo", p, p, tokenRule.size());
                                    tokenRule.add(auxTK);
                                    break;
                                }
                                case "<unaria_termo>": {
                                    TokenRegra auxTK = new TokenRegra("unaria_termo", p, p, tokenRule.size());
                                    tokenRule.add(auxTK);
                                    break;
                                }
//                            case "<unaria_termo>": {
//                                TokenRegra auxTK = new TokenRegra("print2", p, p, tokenRule.size());
//                                tokenRule.add(auxTK);
//                                break;
//                            }
                                case "<program>": {
                                    TokenRegra auxTK2 = tokenRule.remove(tokenRule.size() - 1);
                                    TokenRegra auxTK = new TokenRegra("program", auxTK2.getTipo(), "", tokenRule.size());
                                    auxTK.concatenarCodigo(auxTK2.getCodigo());
                                    tokenRule.add(auxTK);
                                    break;
                                }
                            }
                        } else if (j % 2 == 1 && tamanho > 2)
                            //ACABA MODIFICAÇÔES PARA A SEMANTICA
                            LOGGER.finest("Dispatched: " + p);
                    }
                    int s1 = popInt();
                    pushInt(s1);
                    LOGGER.info("Top of the stack: " + s1);
                    this.stack.push(prod);
                    LOGGER.info("Stacking up production: " + prodLog(prod));
                    String desvio = cellValue(s1, prod, true);
                    LOGGER.info("Desvio[" + s1 + ", " + prodLog(prod) + "]: " + desvio);
                    this.stack.push(desvio);
                    LOGGER.info("Empilhando desvio: " + desvio);
                    break;
                case "a":
                    LOGGER.info("analise terminada, string aceita");
                    return "aceita";
                default:
                    LOGGER.info("analise terminada, string recusada");
                    return "recusada";
            }
        }
        LOGGER.info("analise terminada, string recusada");
        return "recusada";
    }

    private String[] parserCell(String celula) {
        if (celula == null || celula.equals("")) {
            LOGGER.warning("celula vazia, parser retornando nulo");
            return null;
        }
        String[] res = new String[2];
        res[0] = celula.substring(0, 1);
        res[1] = celula.substring(1);
        LOGGER.finest("parser: [" + res[0] + ", " + res[1] + "]");
        return res;
    }

    private int popInt() {
        LOGGER.finest("desempilhando valor inteiro");
        int value = Integer.valueOf(stack.pop());
        LOGGER.finest("valor inteiro desempilhado: " + value);
        return value;
    }

    private void pushInt(int value) {
        LOGGER.finest("empilhando valor inteiro");
        stack.push(String.valueOf(value));
        LOGGER.finest("valor inteiro empilhado: " + value);
    }

    private int getIndexTable(String chave, boolean isTable) {
        String str = !isTable ? "produções" : "estados";
        LOGGER.finest("Buscando indice no cabeçalho de " + str);
        int len = this.tableHeader.length;
        LOGGER.finest("tamanho do cabeçalho: " + len);
        String[] tabela;
        if (isTable) {
            tabela = this.tableHeader;
        } else {
            tabela = this.productionsHeader;
        }
        for (int i = 0; i < len; i++) {
            LOGGER.finest("comparando valor desejado com indice: " + i);
            if (tabela[i].equals(chave)) {
                LOGGER.finest("cabeçalho encontrado, indice: " + i);
                return i;
            }
        }
        LOGGER.finest("cabeçalho não encontrado encontrado, indice: -1");
        return -1;
    }

    public String cellValue(int state, int index, boolean isTable) {
        if (index < 0 || state < 0)
            return null;
        if (isTable) {
            return this.table.get(state)[index];
        } else {
            return this.productions.get(state)[index];
        }
    }

    private String cellValue(int state, String name, boolean isTable) throws ArrayIndexOutOfBoundsException {
        int index = getIndexTable(name, isTable);
        if (index < 0 || state < 0)
            return null;
        if (isTable) {
            return this.table.get(state)[index];
        } else {
            return this.productions.get(state)[index];
        }
    }

    private List<String> findExpected(int state) {
        List<String> expectedValues = new ArrayList();
        String[] vetor = this.table.get(state);
        for (int i = 1; i < vetor.length; i++) {
            if (vetor[i] != null && !this.tableHeader[i].equals("lambda")) {
                expectedValues.add(this.tableHeader[i]);
            }
        }
        return expectedValues;
    }

    private String prodLog(String prod) {
        String str = prod.replaceAll("<", "&lt;");
        return str.replaceAll(">", "&gt;");
    }

    private List<String[]> readCSV(String filename, boolean isTable) {

        BufferedReader br = null;
        String linha;
        String csvDivisor = ",";
        List<String[]> Tabela = null;
        try {

            br = new BufferedReader(new FileReader(filename));
            LOGGER.info("Arquivo " + filename + " aberto com sucesso");
            Tabela = new ArrayList<>();
            LOGGER.finest("Tabela inicializada");
            //GAMBIARRA LIXO PQ JAVA É LIXO
            if (isTable) {
                this.tableHeader = br.readLine().split(csvDivisor);
                LOGGER.finest("Lido o cabeçalho da table");
            } else {
                this.productionsHeader = br.readLine().split(csvDivisor);
                LOGGER.finest("Lido o cabeçalho das produções");
            }
            while ((linha = br.readLine()) != null) {

                String[] line = linha.split(csvDivisor);
                String str = "Linha lida: [";
                for (String s : line) {
                    str += s + ",";
                }
                str += "]";
                LOGGER.finest(str);
                Tabela.add(line);
                LOGGER.finest("Linha adicionada na table de estados");

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LOGGER.severe("Arquivo: " + filename + " não encontrado");
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.severe("Falha na abertura do arquivo: " + filename);
        } finally {
            if (br != null) {
                try {
                    br.close();
                    LOGGER.finest("Arquivo fechado com sucesso");
                } catch (IOException e) {
                    e.printStackTrace();
                    LOGGER.warning("Falha no fechamento do arquivo");
                }
            }
        }
        return Tabela;
    }
}
