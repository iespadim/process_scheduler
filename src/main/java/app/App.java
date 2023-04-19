package app;

import graph.GraphCpu;
import simulation.Cpu;
import simulation.Processo;
import simulation.RoundRobin;
import simulation.SJF;

import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        boolean printDebug = true;
        int escalonador = 1;

        Processo smallproc = new Processo("src/test/java/testCases/prog3.txt", 1, 0,0,4);
        Processo proc1 = new Processo("src/test/java/testCases/soma_de_1_a_5.txt", 1, 1,0,4);
        Processo proc2 = new Processo("src/test/java/testCases/soma_de_1_a_n.txt", 2, 2,0,4);
        Processo proc3 = new Processo("src/test/java/testCases/soma_de_1_a_5.txt", 3, 1,0,4);
        Processo proc4 = new Processo("src/test/java/testCases/soma_de_1_a_10.txt", 4, 1,0,4);
        Processo proc5 = new Processo("src/test/java/testCases/soma_de_1_a_10.txt", 5, 1,0,4);
        Processo proc6 = new Processo("src/test/java/testCases/soma_de_1_a_10.txt", 6, 1,0,4);
        Processo proc7 = new Processo(
                "src/test/java/testFiles/a_igual_b_mais_c.txt",
                1,1,0,4);

        //select escalonador
        RoundRobin robin = new RoundRobin(printDebug);
        SJF sjf = new SJF(printDebug);

        Cpu cpu = new Cpu(robin, printDebug,20);
        cpu.agendarProcesso(proc7);
        //cpu.agendarProcesso(proc5);
           // cpu.agendarProcesso(proc3);
         // cpu.agendarProcesso(proc3);
        cpu.executa();

        //gera grafico
        SwingUtilities.invokeLater(() -> {
            GraphCpu graph = new GraphCpu("Grafico de uso de CPU por processo",true);
            graph.setSize(800, 400);
            graph.setLocationRelativeTo(null);
            graph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            graph.setVisible(true);
        });

    }
}
