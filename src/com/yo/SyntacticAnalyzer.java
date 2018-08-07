package com.yo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.logging.Logger;
import java.util.logging.Level;

class SyntacticAnalyzer {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private String[] cabecalhoTabela = null;
    private List<String[]> tabela = null;
    String[] cabecalhoProducoes = null;
    List<String[]> producoes = null;
    Stack<String> pilha = null;

    List<Integer> lambda_inst = null;
    List<Integer> lambda_if = null;

    String codigoIntermediario = "";

    List<TokenRegra> tkRegra = new ArrayList<TokenRegra>();

    public SyntacticAnalyzer(String csvTable, String csvProd) throws IOException {
        MyLogger.setup();
        LOGGER.setLevel(Level.INFO);
        this.tabela = readCSV(csvTable, true);
        LOGGER.finest("Tabela LR lida com sucesso");
        this.producoes = readCSV(csvProd, false);
        LOGGER.finest("Produções lidas com sucesso");
        this.pilha = new Stack<>();
        LOGGER.finest("Pilha foi inicializada");
        pushInt(0);
        LOGGER.finest("Adicionado o estado inicial na pilha");
        this.lambda_inst = Arrays.asList(3,7, 59,39,4,6,27,10,111,55,53, 141, 5, 112, 114, 125,142,123,107, 88, 9,106);
        this.lambda_if = Arrays.asList(106);
    }

    public String analisar(List<Token> tokens){

        int i = 0;
        LOGGER.info("Inicializando Analise");
        while(i < tokens.size()){

            LOGGER.info("<b>Verificando token "+ i+"</b>");
            LOGGER.info("Pilha: "+ prodLog(this.pilha.toString()));
            int s = popInt();
            pushInt(s);

            LOGGER.info("Topo da pilha: "+ s);
            String a = tokens.get(i).getAttribute();

            if(s == 43){
                switch(a){
                    case("+")   : break;
                    case("-")   : break;
                    case("/")   : break;
                    case("*")   : break;
                    case("<")   : break;
                    case("<=")  : break;
                    case(">")   : break;
                    case(">=")  : break;
                    case("==")  : break;
                    case("!=")  : break;
                    default: a = "lambda"; break;
                }
            }
            if(s == 11){
                switch(a){
                    case("}"): break;
                    case("break"): break;
                    default: a = "$"; break;
                }
            }

            if(this.lambda_if.contains(s)){
                switch(a){
                    case("else"): break;
                    default: a="lambda"; break;
                }
            }

            if(this.lambda_inst.contains(s)){
                switch(a){
                    case("if"): break;
                    case("while"): break;
                    case("for"): break;
                    case("switch"): break;
                    case("int"): break;
                    case("char"): break;
                    case("bool"): break;
                    case("id"): break;
                    case("read"): break;
                    case("printf"): break;
                    case(")"): break;
                    default: a = "lambda"; break;
                }
            }
            LOGGER.info("Token lido: "+ a);
            String celula = null;
            String[] parser = null;
            String saida = null;
            try{
                celula = cellValue(s, a, true);
                LOGGER.info("Valor na celula ["+ s + ", "+ a+"]: "+ celula);
                parser = parserCell(celula);
            } catch(ArrayIndexOutOfBoundsException e){
                System.out.println("Não sei o erro");
            }
            if(parser == null){
                LOGGER.warning("Parser nulo, chegamos em um estado de erro.");

                List<String> expectedValues = this.findExpected(s, true);
                if(a.equals("$")){
                    saida = "Chegamos ao final do seu arquivo e não encontramos um dos valores a seguir: " + expectedValues.toString();
                }else {
                    saida = "Erro na linha " + tokens.get(i).getRow();
                    saida += " valor lido: " + a + " quando se esperava um dos valores: ";
                    saida += expectedValues.toString();
                }

                //buscar valores esperados.
                return saida;
            }
            if (parser[0].equals("E")){
                LOGGER.info("Empilhando "+ a + " e " + parser[1]);
                this.pilha.push(a);
                this.pilha.push(parser[1]);
                LOGGER.finest(a + " e " + parser[1] + " empilhado com sucesso");
                if((s != 43 && !this.lambda_inst.contains(s) && !this.lambda_if.contains(s)) || (!a.equals("lambda") && !a.equals("$"))){
                    i++;
                }
            } else if(parser[0].equals("R")){
                LOGGER.info("Reduzindo pela produção " + parser[1]);
                String cell = cellValue(Integer.valueOf(parser[1]), "len", false);
                LOGGER.finest("tamanho da produção lida: " + cell);
                String prod = cellValue(Integer.valueOf(parser[1]), "A", false);
                LOGGER.info("Tamanho da produção "+ prodLog(prod) + " "+ parser[1] + ": " + cell);
                int tamanho = 2 * Integer.valueOf(cell);
                LOGGER.info("quantidade de elementos a serem desempilhados: "+ tamanho);
                for(int j=0; j<tamanho; j++){
                    String p = pilha.pop();
                    if(j%2 == 1 && tamanho == 2){
                        switch (prod) {
                            case "<tipo>": {
                                TokenRegra auxTK = new TokenRegra("tipo", p, p, tkRegra.size());
                                tkRegra.add(auxTK);
                                break;
                            }
                            case "<op_log>": {
                                TokenRegra auxTK = new TokenRegra("op_log", p, p, tkRegra.size());
                                tkRegra.add(auxTK);
                                break;
                            }
                            case "<op_rel>": {
                                TokenRegra auxTK = new TokenRegra("op_rel", p, p, tkRegra.size());
                                tkRegra.add(auxTK);
                                break;
                            }
                            case "<op_arit>": {
                                TokenRegra auxTK = new TokenRegra("op_arit", p, p, tkRegra.size());
                                tkRegra.add(auxTK);
                                break;
                            }
                            case "<termo>": {
                                TokenRegra auxTK = new TokenRegra("termo", p, p, tkRegra.size());
                                tkRegra.add(auxTK);
                                break;
                            }
                            case "<unaria_termo>": {
                                TokenRegra auxTK = new TokenRegra("unaria_termo", p, p, tkRegra.size());
                                tkRegra.add(auxTK);
                                break;
                            }
//                            case "<unaria_termo>": {
//                                TokenRegra auxTK = new TokenRegra("print2", p, p, tkRegra.size());
//                                tkRegra.add(auxTK);
//                                break;
//                            }
                            case "<program>": {
                                TokenRegra auxTK2 = tkRegra.remove(tkRegra.size() - 1);
                                TokenRegra auxTK = new TokenRegra("program", auxTK2.getTipo(), "", tkRegra.size());
                                auxTK.concatenarCodigo(auxTK2.getCodigo());
                                tkRegra.add(auxTK);
                                break;
                            }
                        }
                    }else if(j%2 == 1 && tamanho > 2)
                        //ACABA MODIFICAÇÔES PARA A SEMANTICA
                        LOGGER.finest("desempilhando: "+ p);
                }
                int s1 = popInt();
                pushInt(s1);
                LOGGER.info("Topo da pilha: "+ s1);
                this.pilha.push(prod);
                LOGGER.info("Empilhando produção: "+ prodLog(prod));
                String desvio = cellValue(s1, prod, true);
                LOGGER.info("Desvio["+ s1+", "+prodLog(prod)+"]: "+ desvio);
                this.pilha.push(desvio);
                LOGGER.info("Empilhando desvio: "+ desvio);
            } else if(parser[0].equals("a")) {
                LOGGER.info("analise terminada, string aceita");
                return "aceita";
            } else {
                LOGGER.info("analise terminada, string recusada");
                return "recusada";
            }
        }
        LOGGER.info("analise terminada, string recusada");
        return "recusada";
    }

    public String[] parserCell(String celula){
        if(celula == null || celula.equals("")){
            LOGGER.warning("celula vazia, parser retornando nulo");
            return null;
        }
        String[] res = new String[2];
        res[0] = celula.substring(0, 1);
        res[1] = celula.substring(1);
        LOGGER.finest("parser: ["+ res[0]+ ", "+ res[1]+"]");
        return res;
    }

    public int popInt(){
        LOGGER.finest("desempilhando valor inteiro");
        int value = Integer.valueOf(pilha.pop());
        LOGGER.finest("valor inteiro desempilhado: "+ value);
        return value;
    }

    public void pushInt(int value){
        LOGGER.finest("empilhando valor inteiro");
        pilha.push(String.valueOf(value));
        LOGGER.finest("valor inteiro empilhado: "+ value);
    }

    public int getIndexTable(String chave, boolean isTable){
        String str = !isTable ?"produções" : "estados";
        LOGGER.finest("Buscando indice no cabeçalho de " + str);
        int len = this.cabecalhoTabela.length;
        LOGGER.finest("tamanho do cabeçalho: "+ len);
        String[] tabela = null;
        if(isTable){
            tabela = this.cabecalhoTabela;
        } else {
            tabela = this.cabecalhoProducoes;
        }
        for(int i=0; i<len; i++){
            LOGGER.finest("comparando valor desejado com indice: "+ i);
            if(tabela[i].equals(chave)){
                LOGGER.finest("cabeçalho encontrado, indice: "+ i);
                return i;
            }
        }
        LOGGER.finest("cabeçalho não encontrado encontrado, indice: -1");
        return -1;
    }

    public String cellValue(int state, int index, boolean isTable){
        if(index < 0 || state < 0)
            return null;
        if(isTable){
            return this.tabela.get(state)[index];
        } else {
            return this.producoes.get(state)[index];
        }
    }

    public String cellValue(int state, String name, boolean isTable) throws ArrayIndexOutOfBoundsException{
        int index = getIndexTable(name, isTable);
        if(index < 0 || state < 0)
            return null;
        if(isTable){
            return this.tabela.get(state)[index];
        } else {
            return this.producoes.get(state)[index];
        }
    }

    public List<String> findExpected(int state, boolean isTable){
        List<String> expectedValues = new ArrayList();
        String[] vetor = this.tabela.get(state);
        for(int i=1; i< vetor.length; i++){
            if(vetor[i] != null && !this.cabecalhoTabela[i].equals("lambda")){
                expectedValues.add(this.cabecalhoTabela[i]);
            }
        }
        return expectedValues;
    }

    public String prodLog(String prod){
        String str = prod.replaceAll("<", "&lt;");
        return str = str.replaceAll(">", "&gt;");
    }

    public List<String[]> readCSV(String filename, boolean isTable){


        BufferedReader br = null;
        String linha = "";
        String csvDivisor = ",";
        List<String[]> Tabela = null;
        try {

            br = new BufferedReader(new FileReader(filename));
            LOGGER.info("Arquivo "+ filename + " aberto com sucesso");
            Tabela = new ArrayList<>();
            LOGGER.finest("Tabela inicializada");
            //GAMBIARRA LIXO PQ JAVA É LIXO
            if(isTable){
                this.cabecalhoTabela = br.readLine().split(csvDivisor);
                LOGGER.finest("Lido o cabeçalho da tabela");
            } else {
                this.cabecalhoProducoes = br.readLine().split(csvDivisor);
                LOGGER.finest("Lido o cabeçalho das produções");
            }
            while ((linha = br.readLine()) != null) {

                String[] line = linha.split(csvDivisor);
                String str = "Linha lida: [";
                for(String s : line){
                    str += s + ",";
                }
                str += "]";
                LOGGER.finest(str);
                Tabela.add(line);
                LOGGER.finest("Linha adicionada na tabela de estados");

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
