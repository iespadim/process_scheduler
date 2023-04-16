import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

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
    private int pc;
    private int acc;
    boolean printDebug;

    public AssembleInterpreter(Processo processo) {
        this.processo = processo;
        this.pc = 0;
        this.acc = 0;
        printDebug = false;
    }

    public AssembleInterpreter(Processo processo, boolean printDebug) {
        this.processo = processo;
        this.pc = 0;
        this.acc = 0;
        this.printDebug = printDebug;
    }

    public void executa() {
        while (pc < processo.getInstrucoes().size()) {
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
                    break;
                case "BRPOS":
                    break;
                case "BRZERO":
                    break;
                case "BRNEG":
                    break;
                case "SYSCALL":
                    break;
                default:
                    System.out.println("Instrução desconhecida: " + partes[0]);
                    break;
            }
            pc++; // Atualizar o PC (Program Counter)
        }
        if (pc >= processo.getInstrucoes().size()) {
            System.out.println("Fim do programa");
            System.out.println("PC: " + pc);
            System.out.println("ACC: " + acc);
        }
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
