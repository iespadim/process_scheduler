package simulation;

import graph.GraphCpuWatcher;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Scanner;

@AllArgsConstructor
@NoArgsConstructor
public class AssembleInterpreter {
    //instrucoes
    /**
     Categoria          Mnemônico                                  Função
     Aritmético         ADD  op1                                   acc=acc+(op1)
     Aritmético         SUB  op1                                   acc=acc–(op1)
     Aritmético         MULT op1                                   acc=acc*(op1)
     Aritmético         DIV  op1                                   acc=acc/(op1)
     Memória            LOAD op1                                   acc=(op1)
     Memória            STORE op1                                  (op1)=acc
     Salto              BRANY label                                pc <- label
     Salto              BRPOS label                                Se acc > 0 então pc <- op1
     Salto              BRZERO label                               Se acc = 0 então pc <- op1
     Salto              BRNEG label                                Se acc < 0 então pc <- op1
     Sistema            SYSCALL index                              Chamada de sistema
     */
    private Processo processo;
    //public int pc;
    //public int acc;
    boolean printDebug;
    GraphCpuWatcher watcher;

    public AssembleInterpreter(Processo processo) {
        this.processo = processo;
        //this.pc = 0;
        //this.acc = 0;
        printDebug = false;
    }

    public AssembleInterpreter(boolean printDebug) {
        this.printDebug = printDebug;
        watcher = GraphCpuWatcher.getInstance();
    }

    public void loadProcess(Processo p){
        this.processo = p;
//        this.pc = p.getPc();
//        this.acc = p.getAcc();
    }

    public void loadProcess(int i) {
        if (i==-1){
            Processo p = Processo.idleProcess();
            loadProcess(p);
            executa(1,p);
            unload();
        }
    }

    public void unload(){
//        processo.setPc(pc);
//        processo.setAcc(acc);
        //zera
        processo= null;
//        this.pc = 0;
//        this.acc = 0;
    }

    public int executa(int ciclos, Processo processo) {
        int result = 10;
        //for ciclos
        for (int i =0; i<ciclos;i++){
            if(processo.getId()==-1){
                result=-1;
            }

            if(processo.getTimeRemaining()==1){
                //acabou o tempo de execução, ultima volta
                result=3;
            }else{
                processo.setTimeRemaining(processo.getTimeRemaining()-1);
            }

            if (processo.getPc() >= processo.getInstrucoes().size()){
                result= 0;
                return result;
            }
            String instrucao = processo.getInstrucoes().get(processo.getPc());
            String[] partes = instrucao.split("\\s+");

            switch (partes[0].toUpperCase()) {
                case "ADD":
                    add(partes[1]);
                    break;
                case "SUB":
                    sub(partes[1]);
                    break;
                case "MULT":
                    mult(partes[1]);
                    break;
                case "DIV":
                    div(partes[1]);
                    break;
                case "LOAD":
                    load(partes[1]);
                    break;
                case "STORE":
                    store(partes[1]);
                    break;
                case "BRANY":
                    brany(partes[1]);
                    break;
                case "BRPOS":
                    brpos(partes[1]);
                    break;
                case "BRZERO":
                    brzero(partes[1]);
                    break;
                case "BRNEG":
                    brneg(partes[1]);
                    break;
                case "SYSCALL":
                    result = syscall(partes[1]);
                    break;
                case "IDLE":
                    if (printDebug) System.out.println("IDLE");
                    break;
                default:
                    System.out.println("Instrução desconhecida: " + partes[0]);
                    break;
            }
            processo.setPc(processo.getPc()+1);
            watcher.incrementTickCounter();
        }
        return result;
    }

    private int syscall(String parte){
        //Chamada de sistema
        //index = 0: halt.
        //index = 1: impressão tela + bloqueio de execução (8 a 10 unidades de tempo).
        //index = 2: leitura teclado + bloqueio de execução (8 a 10 unidades de tempo).
        int i = -1;

        if (printDebug) System.out.println("SYSCALL: " + parte);
        switch (parte) {
            case "-1":
                //System.out.println("idle");
                break;
            case "0":
                if (printDebug) System.out.println("syscall halt");
                return 0;
            case "1":
                if (printDebug) System.out.println("Impressão tela + bloqueio de execução (8 a 10 unidades de tempo).");
                return 1;
            case "2":
                if (printDebug) System.out.println("Leitura teclado + bloqueio de execução (8 a 10 unidades de tempo).");
                System.out.println("Digite um valor: ");
                Scanner scanner = new Scanner(System.in);
                processo.setPc(scanner.nextInt());
                return 2;
            default:
                System.out.println("Erro: SYSCALL não reconhecido: " + parte);
                return -2;
        }
        return i;
    }

    private void brneg(String parte) {
        //Se acc < 0 então pc <- op1
        if (printDebug) {
            System.out.println("BRNEG: " + parte);
        }
        if (processo.getAcc() < 0) {
            if(!processo.getLabels().containsKey(parte)){
                System.out.println("Erro: Label não encontrada: " + parte);
                return;
            }
            processo.setPc(processo.getLabels().get(parte)-1);
            //pc = processo.getLabels().get(parte)-1;
        }
    }

    private void brpos(String parte) {
        //Se acc > 0 então pc <- op1
        if (printDebug) {
            System.out.println("BRPOS: " + parte);
        }
        if (processo.getAcc() > 0) {
            if(!processo.getLabels().containsKey(parte)){
                System.out.println("Erro: Label não encontrada: " + parte);
                return;
            }
            processo.setPc(processo.getLabels().get(parte)-1);
            //pc = processo.getLabels().get(parte)-1;
        }
    }

    private void brzero(String parte) {
        //Se acc = 0 então pc <- op1
        if (printDebug) {
            System.out.println("BRZERO: " + parte);
        }
        if (processo.getAcc() == 0) {
            if(!processo.getLabels().containsKey(parte)){
                System.out.println("Erro: Label não encontrada: " + parte);
                return;
            }
            processo.setPc(processo.getLabels().get(parte)-1);
            //pc = processo.getLabels().get(parte)-1;
        }
    }

    private void brany(String parte) {
        //pc <- label
        if (printDebug) {
            System.out.println("BRANY: pc<- " + parte);
        }
        if(!processo.getLabels().containsKey(parte)){
            System.out.println("Erro: Label não encontrada: " + parte);
            return;
        }
        processo.setPc(processo.getLabels().get(parte)-1);
        //pc = processo.getLabels().get(parte)-1;
    }

    private void store(String parte) {
        if (parte.charAt(0) == '#'){
            System.out.println("Erro: Tentativa de armazenar em uma constante");
            return;
        }
        if (printDebug) {
            System.out.print("store(op,acc):" + parte + " = " + processo.getAcc());
        }
        processo.getDados().put(parte, processo.getAcc());
        if (printDebug) {
            System.out.println(": " + processo.getDados().get(parte));
        }
    }

    private void load(String parte) {
        //acc=(op1)
        if (parte.charAt(0) == '#') {
            if (printDebug) {
                System.out.print("load(acc=op):acc " + processo.getAcc() + " = " + Integer.parseInt(parte.substring(1)));
            }
            processo.setAcc(Integer.parseInt(parte.substring(1)));
            //acc = Integer.parseInt(parte.substring(1));
            if (printDebug) {
                System.out.println(": " + processo.getAcc());
            }
        } else {
            if (printDebug) System.out.print("load(acc=op):acc " + processo.getAcc() + " = " + processo.getDados().get(parte));
            processo.setAcc(processo.getDados().get(parte));
            //acc = processo.getDados().get(parte);
            if (printDebug) System.out.println(": " + processo.getAcc());
        }
    }

    private void div(String parte) {
        if (parte.charAt(0) == '#') {
            if (printDebug) {
                System.out.print("div(acc,op):acc " + processo.getAcc() + " / " + Integer.parseInt(parte.substring(1)));
            }
            processo.setAcc(processo.getAcc() / Integer.parseInt(parte.substring(1)));
            //acc = acc / Integer.parseInt(parte.substring(1));
            if (printDebug) {
                System.out.println(": " + processo.getAcc());
            }
        } else {
            if (printDebug) {
                System.out.print("div(acc,op):acc " + processo.getAcc() + " / " + processo.getDados().get(parte));
            }
            processo.setAcc(processo.getAcc() / processo.getDados().get(parte));
            //acc = acc / processo.getDados().get(parte);
            if (printDebug) {
                System.out.println(": " + processo.getAcc());
            }
        }
    }

    private void mult(String parte) {
        if (parte.charAt(0) == '#') {
            if (printDebug) {
                System.out.print("mult(acc,op):acc " + processo.getAcc() + " * " + Integer.parseInt(parte.substring(1)));
            }
            processo.setAcc(processo.getAcc() * Integer.parseInt(parte.substring(1)));
            //acc = acc * Integer.parseInt(parte.substring(1));
            if (printDebug) {
                System.out.println(": " + processo.getAcc());
            }
        } else {
            if (printDebug) {
                System.out.print("mult(acc,op):acc " + processo.getAcc() + " * " + processo.getDados().get(parte));
            }
            processo.setAcc(processo.getAcc() * processo.getDados().get(parte));
            //acc = acc * processo.getDados().get(parte);
            if (printDebug) {
                System.out.println(": " + processo.getAcc());
            }
        }
    }

    private void sub(String parte) {
        if (parte.charAt(0) == '#') {
            if (printDebug) {
                System.out.print("sub(acc-op):acc " + processo.getAcc() + " - " + Integer.parseInt(parte.substring(1)));
            }
            processo.setAcc(processo.getAcc() - Integer.parseInt(parte.substring(1)));
            //acc = acc - Integer.parseInt(parte.substring(1));
            if (printDebug) {
                System.out.println(": " + processo.getAcc());
            }
        } else {
            if (printDebug) {
                System.out.print("sub(acc-op):acc " + processo.getAcc() + " - " + processo.getDados().get(parte));
            }
            processo.setAcc(processo.getAcc() - processo.getDados().get(parte));
            //acc = acc - processo.getDados().get(parte);
            if (printDebug) {
                System.out.println(": " + processo.getAcc());
            }
        }
    }

    private void add(String parte) {
        if (parte.charAt(0) == '#') {
            //literal
            if(printDebug){
                System.out.print("add(acc,op):acc " + processo.getAcc() + " + " + Integer.parseInt(parte.substring(1)));
            }
            processo.setAcc(processo.getAcc() + Integer.parseInt(parte.substring(1)));
            //acc = acc + Integer.parseInt(parte.substring(1));
            if(printDebug){
                System.out.println(": " + processo.getAcc());
            }
        } else {
            if(printDebug){
                System.out.print("add(acc,op):acc " + processo.getAcc() + " + " + processo.getDados().get(parte));
            }
            processo.setAcc(processo.getAcc() + processo.getDados().get(parte));
            //acc = acc + processo.getDados().get(parte);
            if(printDebug){
                System.out.println(": " + processo.getAcc());
            }
        }
    }
}
