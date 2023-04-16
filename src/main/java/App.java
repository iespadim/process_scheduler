public class App {
    public static void main(String[] args) throws Exception {
        boolean printDebug = true;
        Processo proc = new Processo("prog0.txt", 0, 0);
        proc.imprimeInstr();
        proc.escreveDados();
        proc.escreveLabels();
        //System.out.println(proc.dasdo);

        AssembleInterpreter interpretador = new AssembleInterpreter(proc,printDebug);
        interpretador.executa();
    }
}
