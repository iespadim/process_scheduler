public class App {
    public static void main(String[] args) throws Exception {
        boolean printDebug = false;
        Processo proc1 = new Processo("src/test/java/testCases/soma_de_1_a_n.txt", 2, 1,0,4);
        Processo proc = new Processo("src/test/java/testCases/prog2.txt", 1, 0,0,2);
        Processo proc3 = new Processo("src/test/java/testCases/colorado.txt", 3, 0,0,2);
        //proc.imprimeInstr();
        //proc.escreveDados();
        //proc.escreveLabels();
        //System.out.println(proc.dasdo);
        Heapprocess heap = new Heapprocess(3,printDebug);
        heap.insert(proc);
        heap.insert(proc1);
        //heap.insert(proc3);
        heap.executa();

    }
}
