import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Processo {

    private ArrayList<String> instrucoes;
    private HashMap<String, Integer> dados;//A string do dado com seu valor em Integer
    private HashMap<String, Integer> labels;//Quando ler o arquivo a label vai ser o tamanho(ou seja, o indice do proximo a ser escrito)
    private int id, prio;

    public Processo(String narq, int id, int prio){
        dados = new HashMap<>();
        labels = new HashMap<>();
        instrucoes = new ArrayList<>();
        criarProcesso(narq);
        this.id = id;
        this.prio = prio;
       
    };
  
    public int getPrio(){
        return this.prio;
    }
    public void imprimeInstr(){
        for (String string : instrucoes) {
            System.out.println(string);
        }
    }

    private void criarProcesso (String naqr){
        boolean data = false;
        boolean code = false;
        String currDir = Paths.get("").toAbsolutePath().toString();
        String nameComplete = currDir+"\\"+naqr;
        Path path2 = Paths.get(nameComplete);
            try (Scanner sc = new Scanner(Files.newBufferedReader(path2, Charset.defaultCharset()))){
                while(sc.hasNextLine()) {
                    String lines = sc.nextLine();
                    String line = lines;
                    if (lines.contains("#")){
                    int commentPos = lines.indexOf("#");
                    line = lines.substring(0, commentPos);
                    } 
                    if(line.equals(".code")){
                        code = true;
                    }
                    if(code == true){
                        if (line.endsWith(":") == true){
                            labels.put(line.substring(0, line.length() -1), instrucoes.size());
                        } else{
                            instrucoes.add(line);
                        }
                    }
                    if(line.equals(".endcode")){
                        code = false;
                    }
                    if(line.equals(".data")){
                        data = true;
                    }
                    if(data == true){
                        String[] tokens = line.split(" ");
                        dados.put(tokens[0], Integer.parseInt(tokens[1]));
                    }
                    if(line.equals(".enddata")){
                        data = false;
                    }
                }
            }catch (IOException x){
                   System.err.format("Erro de E/S: %s%n", x);
            }
    
        }    
    }

