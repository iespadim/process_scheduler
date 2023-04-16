import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Processo {

    @Getter
    public ArrayList<String> instrucoes;
    @Getter
    private HashMap<String, Integer> dados;//A string do dado com seu valor em Integer
    @Getter
    private HashMap<String, Integer> labels;//Quando ler o arquivo a label vai ser o tamanho(ou seja, o indice do proximo a ser escrito)
    @Getter
    @Setter
    private int id, prio, pc, acc, quantum;

    public Processo(String narq, int id, int prio, int pc, int quantum){
        dados = new HashMap<>();
        labels = new HashMap<>();
        instrucoes = new ArrayList<>();
        criarProcesso(narq);
        this.id = id;
        this.prio = prio;
        this.pc = pc;
        this.quantum = quantum;
    };
  
    public int getPrio(){
        return this.prio;
    }
    public void imprimeInstr(){
        System.out.println("------Instrucoes-------");
        for (String string : instrucoes) {
            System.out.println(string);
        }
        System.out.println("-----------------------");
    }
    public void escreveDados(){//So pra verificar
        System.out.println("--------Dados----------");
        for (Map.Entry<String,Integer> entry : dados.entrySet()) {
            System.out.println(entry.getKey()+" "+entry.getValue());  
        }
        System.out.println("-----------------------");
    }
    public void escreveLabels(){//So pra verificar
        System.out.println("-------Labels----------");
        for (Map.Entry<String,Integer> entry : labels.entrySet()) {
            System.out.println(entry.getKey()+" "+entry.getValue());  
        }
        System.out.println("------------------------");
    }
    private void criarProcesso (String naqr){
        boolean data = false;
        boolean code = false;
        String currDir = Paths.get("").toAbsolutePath().toString();
        String nameComplete = currDir+"\\"+naqr;
        Path path2 = Paths.get(nameComplete);
            try (Scanner sc = new Scanner(Files.newBufferedReader(path2, Charset.defaultCharset()))){
                while(sc.hasNextLine()) {
                    String line = sc.nextLine();
                    if (line.contains("#")){
                        line = limpaComentario2(line);
                    }
                    if(line.equals(".data")){
                        data = true;
                    }
                    if(line.equals(".enddata")){
                        data = false;
                    }
                    if(line.equals(".code")){
                        code = true;
                    }
                    if(line.equals(".endcode")){
                        code = false;
                    }
                    if (code == true) {
                        int labelPos = line.indexOf(":");
                        if (labelPos != -1) {
                            labels.put(line.substring(0, labelPos), instrucoes.size());
                            if (labelPos + 1 < line.length()) {
                                String remainingLine = line.substring(labelPos + 1).trim();
                                if (!remainingLine.isEmpty()) {
                                    instrucoes.add(remainingLine);
                                }
                            }
                        } else if (!line.contains(".code")) {
                            instrucoes.add(line.trim());
                        }
                    }


                    if(data == true && !line.contains(".data")){
                        String[] tokens = line.trim().split("\\s+");
                        dados.put(tokens[0], Integer.parseInt(tokens[1]));
                    }
                    
                }
            }catch (IOException x){
                   System.err.format("Erro de E/S: %s%n", x);
            }
    
        }

    public String limpaComentario(String line) {
        line = line.trim();
        int count = 0;
        int index = 0;

        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '#') {
                count++;
                if (count == 2) {
                    index = i;
                    break;
                }
            }
        }

        if (count == 1) {
            int commentPos = line.indexOf("#");
            line = line.substring(0, commentPos);
        } else if (count == 2) {
            line = line.substring(0, index);
        }

        String regex = "^\\s*(\\S+)?\\s*(\\S+)?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            if (matcher.group(1) != null && matcher.group(1).endsWith(":")) {
                return (matcher.group(2) != null ? matcher.group(2) : "") + " " + (matcher.group(3) != null ? matcher.group(3) : "");
            } else {
                return (matcher.group(1) != null ? matcher.group(1) : "") + " " + (matcher.group(2) != null ? matcher.group(2) : "");
            }
        }

        return "";
    }

    public String limpaComentario2(String line){
            line = line.trim();
            // count number of "#"
            // save the index of the second #
            int count = 0;
            int index = 0;
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == '#') {
                    count++;
                    if (count == 2) {
                        index = i;
                        break;
                    }
                }
            }
            //if count == 1, then it is a simple comment
            if (count == 1) {
                String regex = "^\\s*(\\S+)?\\s*(\\S+)?";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(line);

                if (matcher.find()) {
                    if (matcher.group(1) != null && matcher.group(1).endsWith(":")) {
                        return (matcher.group(2) != null ? matcher.group(2) : "") + " " + (matcher.group(3) != null ? matcher.group(3) : "");
                    } else {
                        return (matcher.group(1) != null ? matcher.group(1) : "") + " " + (matcher.group(2) != null ? matcher.group(2) : "");
                    }
                }

                String[] operations = {"ADD", "SUB", "MULT", "DIV", "LOAD", "STORE"};
                for (String operation : operations) {
                    if (line.toUpperCase().contains(operation)) {
                        return line;
                    }
                }
                int commentPos = line.indexOf("#");
                line = line.substring(0, commentPos);
            } else if (count == 2) {
                line = line.substring(0, index);
            }
            return line;
        }
    }

