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
    private Stack<String> pilha;

    public SyntacticAnalyzer(String csvTable, String csvProd) throws IOException {
        MyLogger.setup();
        LOGGER.setLevel(Level.INFO);
        this.tabela = readCSV(csvTable, true);
        LOGGER.finest("Tabela LR lida com sucesso");
        this.producoes = readCSV(csvProd, false);
        LOGGER.finest("Producões lidas com sucesso");
        this.pilha = new Stack<>();
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

            LOGGER.info("Topo da pilha: " + s);
            String a = tokens.get(i).getValue();

            LOGGER.info("Token lido: " + a);
            String celula = cellValue(s, a, true);
            LOGGER.info("Valor na celula [" + s + ", " + a + "]: " + celula);
            String[] parser = parserCell(celula);
            if (parser == null) {
                LOGGER.warning("Parser nulo, chegamos em um estado de erro.");
                return false;
            }
            switch (parser[0]) {
                case "E":
                    LOGGER.info("Empilhando " + a + " e " + parser[1]);
                    this.pilha.push(a);
                    this.pilha.push(parser[1]);
                    LOGGER.finest(a + " e " + parser[1] + " empilhado com sucesso");
                    i++;
                    break;
                case "R":
                    LOGGER.info("Reduzindo pela producao " + parser[1]);
                    String cell = cellValue(Integer.valueOf(parser[1]), "len", false);
                    LOGGER.finest("tamanho da producao lida: " + cell);
                    String prod = cellValue(Integer.valueOf(parser[1]), "A", false);
                    LOGGER.info("Tamanho da producao " + prodLog(prod) + " " + parser[1] + ": " + cell);
                    int tamanho = 2 * Integer.valueOf(cell);
                    LOGGER.info("quantidade de elementos a serem desempilhados: " + tamanho);
                    for (int j = 0; j < tamanho; j++) {
                        String p = pilha.pop();
                        LOGGER.finest("desempilhando: " + p);
                    }
                    int s1 = popInt();
                    pushInt(s1);
                    LOGGER.info("Topo da pilha: " + s1);
                    this.pilha.push(prod);
                    LOGGER.info("Empilhando producao: " + prodLog(prod));
                    String desvio = cellValue(s1, prod, true);
                    LOGGER.info("Desvio[" + s1 + ", " + prodLog(prod) + "]: " + desvio);
                    this.pilha.push(desvio);
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
        int value = Integer.valueOf(pilha.pop());
        LOGGER.finest("valor inteiro desempilhado: " + value);
        return value;
    }

    private void pushInt(int value) {
        LOGGER.finest("empilhando valor inteiro");
        pilha.push(String.valueOf(value));
        LOGGER.finest("valor inteiro empilhado: " + value);
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

    private String cellValue(int state, String name, boolean isTable) {
        int index = getIndexTable(name, isTable);
        if (index < 0 || state < 0)
            return null;
        if (isTable) {
            return this.tabela.get(state)[index];
        } else {
            return this.producoes.get(state)[index];
        }
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
