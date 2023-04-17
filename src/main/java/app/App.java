package app;

import graph.GraphCpu;
import simulation.Heapprocess;
import simulation.Processo;

import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        boolean printDebug = false;
        Processo proc1 = new Processo("src/test/java/testCases/soma_de_1_a_5.txt", 1, 1,0,4);
        Processo proc2 = new Processo("src/test/java/testCases/soma_de_1_a_10.txt", 2, 2,0,6);
        Processo proc3 = new Processo("src/test/java/testCases/prog2.txt", 3, 0,0,4);
        Processo proc4 = new Processo("src/test/java/testCases/colorado.txt", 4, 0,0,4);

        //proc.imprimeInstr();
        //proc.escreveDados();
        //proc.escreveLabels();
        //System.out.println(proc.dasdo);

        Heapprocess heap = new Heapprocess(3,printDebug);
        heap.insert(proc1);
        heap.insert(proc2);
        heap.executa();

        //gera grafico
        SwingUtilities.invokeLater(() -> {
            GraphCpu graph = new GraphCpu("Grafico de uso de CPU por processo");
            graph.setSize(800, 400);
            graph.setLocationRelativeTo(null);
            graph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            graph.setVisible(true);
        });

    }
}
