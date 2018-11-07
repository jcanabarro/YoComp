package com.yo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import java.util.logging.Level;
import java.util.logging.Logger;

class SyntacticAnalyzer {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private String[] cabecalhoTabela = null;
    private List<String[]> tabela;
    private String[] cabecalhoProducoes = null;
    private List<String[]> producoes;
    private Stack<Token> pilha;
    private List<String> reversed_values;


    public SyntacticAnalyzer(String csvTable, String csvProd) throws IOException {
        MyLogger.setup();
        LOGGER.setLevel(Level.INFO);
        this.tabela = readCSV(csvTable, true);
        LOGGER.finest("Tabela LR lida com sucesso");
        this.producoes = readCSV(csvProd, false);
        LOGGER.finest("Producões lidas com sucesso");
        this.pilha = new Stack<>();
        this.reversed_values = Arrays.asList("string", "char", "int", "float", "id");
        LOGGER.finest("Pilha foi inicializada");
        pushInt(0);
    }

    public boolean Analyzer(List<Token> tokens) {
        int i = 0;
        tokens.add(new Token("vazio", "$", 0));
        LOGGER.info("Inicializando Analise");
        while (i < tokens.size()) {
            LOGGER.info("<b>Verificando token " + i + "</b>");
            LOGGER.info("Pilha: " + prodLog(this.pilha.toString()));
            int s = popInt();
            pushInt(s);
            String a;
            LOGGER.info("Topo da pilha: " + s);
            a = tokens.get(i).getValor();
            if(this.reversed_values.contains(tokens.get(i).getAtributo())){
                a = tokens.get(i).getAtributo();
            }

            LOGGER.info("Token lido: " + a);
            String celula = cellValue(s, a, true);
            if (celula == null) {
                return false;
            }
            LOGGER.info("Valor na celula [" + s + ", " + a + "]: " + celula);
            String[] parser = parserCell(celula, s, a);
            if (parser == null) {
                return false;
            }
            switch (parser[0]) {
                case "E":
                    LOGGER.info("Empilhando " + a + " e " + parser[1]);
                    this.pilha.push(tokens.get(i));
                    this.pilha.push(new Token("", parser[1]));
                    LOGGER.finest(a + " e " + parser[1] + " empilhado com sucesso");
                    i++;
                    break;
                case "R":
                    LOGGER.info("Reduzindo pela producao " + parser[1]);
                    String cell = cellValue(Integer.valueOf(parser[1]), "len", false);
                    LOGGER.finest("tamanho da producao lida: " + cell);
                    String prod = cellValue(Integer.valueOf(parser[1]), "A", false);
                    LOGGER.info("Tamanho da producao " + prodLog(Objects.requireNonNull(prod)) + " " + parser[1] + ": " + cell);
                    int tamanho = 2 * Integer.valueOf(Objects.requireNonNull(cell));
                    LOGGER.info("quantidade de elementos a serem desempilhados: " + tamanho);

                    Stack<Token> pilha_aux = new Stack<>();
                    Token p;
                    for (int j = 0; j < tamanho; j++) {
                        p = pilha.pop();
                        LOGGER.finest("desempilhando: " + p);
                        if(j%2==1) {
                            pilha_aux.push(p);
                        }
                    }
                    LOGGER.finest("tamanho pilha auxiliar: "+ pilha_aux.size());

                    LOGGER.info("pilha auxiliar: "+ prodLog(pilha_aux.toString()));

                    // Here we apply the semantic analysis
                    SemanticAnalyzer semantic = new SemanticAnalyzer();
                    semantic.codeGenerator(parser[1], prod, pilha_aux);

                    int s1 = popInt();
                    pushInt(s1);
                    LOGGER.info("Topo da pilha: " + s1);
                    this.pilha.push(new Token("", prod));
                    LOGGER.info("Empilhando producao: " + prodLog(prod));
                    String desvio = cellValue(s1, prod, true);
                    LOGGER.info("Desvio[" + s1 + ", " + prodLog(prod) + "]: " + desvio);
                    this.pilha.push(new Token("", desvio));
                    LOGGER.info("Empilhando desvio: " + desvio);
                    break;
                case "a":
                    LOGGER.info("analise terminada, string aceita");
                    return true;
                default:
                    LOGGER.info("analise terminada, string recusada");
                    return false;
            }
        }
        LOGGER.info("analise terminada, string recusada");
        return false;
    }

    private String[] parserCell(String celula, int position, String name) {
        if (celula.equals("ERROR")) {
            List<String> list = get_expected_types(position);
            LOGGER.warning("Celula vazia, era esperado: " + String.valueOf(list) + " mas foi lido: " + name);
            return null;
        }
        String[] res = new String[2];
        res[0] = celula.substring(0, 1);
        res[1] = celula.substring(1);
        LOGGER.finest("parser: [" + res[0] + ", " + res[1] + "]");
        return res;
    }

    private String cellValue(int state, String name, boolean isTable) {
        int index = getIndexTable(name, isTable);
        if (index < 0 || state < 0)
            return null;
        if (isTable) {
            if (this.tabela.get(state)[index].equals("ERROR")) {
                List<String> list = get_expected_types(state);
                LOGGER.warning("celula vazia, era esperado: " + String.valueOf(list) + " mas foi lido: " + name);
                return null;
            }
            return this.tabela.get(state)[index];

        } else {
            return this.producoes.get(state)[index];
        }
    }

    private List<String> get_expected_types(int position) {
        List<String> list = new ArrayList<>();
//        Use 44 because is the max size of non-null terminal
        for(int i = 1; i <= 44; i++){
            if (!this.tabela.get(position)[i].equals("ERROR")){
                list.add(this.cabecalhoTabela[i]);
            }
        }
        return list;
    }

    private int popInt() {
        LOGGER.finest("desempilhando valor inteiro");
        Token aux = pilha.pop();
        int value = Integer.valueOf(aux.getValor());
        LOGGER.finest("valor inteiro desempilhado: "+ value);
        return value;
    }

    private void pushInt(int value) {
        Token aux = new Token("", String.valueOf(value));
        LOGGER.finest("empilhando valor inteiro");
        pilha.push(aux);
        LOGGER.finest("valor inteiro empilhado: "+ value);
    }

    private int getIndexTable(String chave, boolean isTable) {
        String str = !isTable ? "producões" : "estados";
        int len = 0;
        LOGGER.finest("Buscando indice no cabecalho de " + str);
        LOGGER.finest("tamanho do cabecalho: " + len);
        String[] tabela;
        if (isTable) {
            tabela = this.cabecalhoTabela;
            len = this.cabecalhoTabela.length;
        } else {
            tabela = this.cabecalhoProducoes;
            len = this.cabecalhoProducoes.length;
        }
        for (int i = 0; i < len; i++) {
            LOGGER.finest("comparando valor desejado com indice: " + i);
            if (tabela[i].equals(chave)) {
                LOGGER.finest("cabecalho encontrado, indice: " + i);
                return i;
            }
        }
        LOGGER.finest("cabecalho nao encontrado encontrado, indice: -1");
        return -1;
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
            if (isTable) {
                this.cabecalhoTabela = br.readLine().split(csvDivisor);
                LOGGER.finest("Lido o cabecalho da tabela");
            } else {
                this.cabecalhoProducoes = br.readLine().split(csvDivisor);
                LOGGER.finest("Lido o cabecalho das producões");
            }
            while ((linha = br.readLine()) != null) {
                String[] line = linha.split(csvDivisor);
                StringBuilder str = new StringBuilder("Linha lida: [");
                for (String s : line) {
                    str.append(s).append(",");
                }
                str.append("]");
                LOGGER.finest(str.toString());
                Tabela.add(line);
                LOGGER.finest("Linha adicionada na tabela de estados");

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LOGGER.severe("Arquivo: " + filename + " nao encontrado");
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
