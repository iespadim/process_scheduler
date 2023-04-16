import lombok.AllArgsConstructor;
import lombok.Builder;
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
    public int pc;
    public int acc;
    boolean printDebug;

    public AssembleInterpreter(Processo processo) {
        this.processo = processo;
        this.pc = 0;
        this.acc = 0;
        printDebug = false;
    }

    public AssembleInterpreter(boolean printDebug) {
        this.printDebug = printDebug;
    }

    public void load(Processo p){
        this.processo = p;
        this.pc = p.getPc();
        this.acc = p.getAcc();
    }

    public void unload(){
        processo.setPc(pc);
        processo.setAcc(acc);
    }

    public int executa(int ciclos) {
        int result = 1;
        //for ciclos
        for (int i =0; i<ciclos;i++){
            if (pc >= processo.getInstrucoes().size()) {
                System.out.println("Fim do programa");
                System.out.println("PC: " + pc);
                System.out.println("ACC: " + acc);
                result= 0;
                return result;
            }

            String instrucao = processo.getInstrucoes().get(pc);
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
                    syscall(partes[1]);
                    break;
                default:
                    System.out.println("Instrução desconhecida: " + partes[0]);
                    break;
            }
            pc++; // Atualizar o PC (Program Counter)
        }
        return result;
    }

    private void syscall(String parte){
        //Chamada de sistema
        //index = 0: halt.
        //index = 1: impressão tela + bloqueio de execução (8 a 10 unidades de tempo).
        //index = 2: leitura teclado + bloqueio de execução (8 a 10 unidades de tempo).
        if (printDebug) System.out.println("SYSCALL: " + parte);
        switch (parte) {
            case "0":
                System.out.println("Fim do programa");
                System.out.println("PC: " + pc);
                System.out.println("ACC: " + acc);
                break;
            case "1":
                if (printDebug) System.out.println("Impressão tela + bloqueio de execução (8 a 10 unidades de tempo).");
                System.out.println(acc);
                //todo bloqueio de execução
                break;
            case "2":
                if (printDebug) System.out.println("Leitura teclado + bloqueio de execução (8 a 10 unidades de tempo).");
                System.out.println("Digite um valor: ");
                Scanner scanner = new Scanner(System.in);
                acc = scanner.nextInt();
                //todo bloqueio de execução
                break;
            default:
                if (printDebug) System.out.println("Erro: SYSCALL não reconhecido: " + parte);
                break;
        }
    }

    private void brneg(String parte) {
        //Se acc < 0 então pc <- op1
        if (printDebug) {
            System.out.println("BRNEG: " + parte);
        }
        if (acc < 0) {
            if(!processo.getLabels().containsKey(parte)){
                System.out.println("Erro: Label não encontrada: " + parte);
                return;
            }
            pc = processo.getLabels().get(parte)-1;
        }
    }

    private void brpos(String parte) {
        //Se acc > 0 então pc <- op1
        if (printDebug) {
            System.out.println("BRPOS: " + parte);
        }
        if (acc > 0) {
            if(!processo.getLabels().containsKey(parte)){
                System.out.println("Erro: Label não encontrada: " + parte);
                return;
            }
            pc = processo.getLabels().get(parte)-1;
        }
    }

    private void brzero(String parte) {
        //Se acc = 0 então pc <- op1
        if (printDebug) {
            System.out.println("BRZERO: " + parte);
        }
        if (acc == 0) {
            if(!processo.getLabels().containsKey(parte)){
                System.out.println("Erro: Label não encontrada: " + parte);
                return;
            }
            pc = processo.getLabels().get(parte)-1;
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
        pc = processo.getLabels().get(parte)-1;
    }

    private void store(String parte) {
        if (parte.charAt(0) == '#'){
            System.out.println("Erro: Tentativa de armazenar em uma constante");
            return;
        }
        if (printDebug) {
            System.out.print("store(op,acc):" + parte + " = " + acc);
        }
        processo.getDados().put(parte, acc);
        if (printDebug) {
            System.out.println(": " + processo.getDados().get(parte));
        }
    }

    private void load(String parte) {
        //acc=(op1)
        if (parte.charAt(0) == '#') {
            if (printDebug) {
                System.out.print("load(acc=op):acc " + acc + " = " + Integer.parseInt(parte.substring(1)));
            }
            acc = Integer.parseInt(parte.substring(1));
            if (printDebug) {
                System.out.println(": " + acc);
            }
        } else {
            if (printDebug) {
                System.out.print("load(acc=op):acc " + acc + " = " + processo.getDados().get(parte));
            }
            acc = processo.getDados().get(parte);
            if (printDebug) {
                System.out.println(": " + acc);
            }
        }
    }

    private void div(String parte) {
        if (parte.charAt(0) == '#') {
            if (printDebug) {
                System.out.print("div(acc,op):acc " + acc + " / " + Integer.parseInt(parte.substring(1)));
            }
            acc = acc / Integer.parseInt(parte.substring(1));
            if (printDebug) {
                System.out.println(": " + acc);
            }
        } else {
            if (printDebug) {
                System.out.print("div(acc,op):acc " + acc + " / " + processo.getDados().get(parte));
            }
            acc = acc / processo.getDados().get(parte);
            if (printDebug) {
                System.out.println(": " + acc);
            }
        }
    }

    private void mult(String parte) {
        if (parte.charAt(0) == '#') {
            if (printDebug) {
                System.out.print("mult(acc,op):acc " + acc + " * " + Integer.parseInt(parte.substring(1)));
            }
            acc = acc * Integer.parseInt(parte.substring(1));
            if (printDebug) {
                System.out.println(": " + acc);
            }
        } else {
            if (printDebug) {
                System.out.print("mult(acc,op):acc " + acc + " * " + processo.getDados().get(parte));
            }
            acc = acc * processo.getDados().get(parte);
            if (printDebug) {
                System.out.println(": " + acc);
            }
        }
    }

    private void sub(String parte) {
        if (parte.charAt(0) == '#') {
            if (printDebug) {
                System.out.print("sub(acc-op):acc " + acc + " - " + Integer.parseInt(parte.substring(1)));
            }
            acc = acc - Integer.parseInt(parte.substring(1));
            if (printDebug) {
                System.out.println(": " + acc);
            }
        } else {
            if (printDebug) {
                System.out.print("sub(acc-op):acc " + acc + " - " + processo.getDados().get(parte));
            }
            acc = acc - processo.getDados().get(parte);
            if (printDebug) {
                System.out.println(": " + acc);
            }
        }
    }

    private void add(String parte) {
        if (parte.charAt(0) == '#') {
            //literal
            if(printDebug){
                System.out.print("add(acc,op):acc " + acc + " + " + Integer.parseInt(parte.substring(1)));
            }
            acc = acc + Integer.parseInt(parte.substring(1));
            if(printDebug){
                System.out.println(": " + acc);
            }
        } else {
            if(printDebug){
                System.out.print("add(acc,op):acc " + acc + " + " + processo.getDados().get(parte));
            }
            acc = acc + processo.getDados().get(parte);
            if(printDebug){
                System.out.println(": " + acc);
            }
        }
    }


}
