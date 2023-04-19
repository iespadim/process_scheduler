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
        int escalonador = 0;
        int maxIdleCycles = 15;
        int maxSJFTimeAllowed = 30;
        Cpu cpu;

        Processo proc2 = new Processo("src/test/java/testFiles/prog3.txt", 1, 0,0,0,4);
        //Processo proc1 = new Processo("src/test/java/testFiles/soma_de_1_a_5.txt", 1, 0,0,40,4);
        //Processo proc2 = new Processo("src/test/java/testFiles/soma_de_1_a_n_ver2.txt", 2, 0,0,0,4);
        Processo proc3 = new Processo("src/test/java/testFiles/soma_de_1_a_5.txt", 3, 1,0,0,4);
        Processo proc4 = new Processo("src/test/java/testFiles/soma_de_1_a_10.txt", 4, 1,0,0,4);
        Processo proc5 = new Processo("src/test/java/testFiles/soma_de_1_a_10.txt", 5, 1,0,0,4);
//        Processo proc6 = new Processo("src/test/java/testFiles/soma_de_1_a_10.txt", 6, 1,0,0,4);
//        Processo proc7 = new Processo("src/test/java/testFiles/a_igual_b_mais_c.txt",1,1,0,0,4);


        //select escalonador
        if(escalonador == 1) {
            cpu = new Cpu(new RoundRobin(printDebug), printDebug, maxIdleCycles);
        }else{
            //alterar para outro escalonador
            cpu = new Cpu(new SJF(printDebug, maxSJFTimeAllowed), printDebug, maxIdleCycles);
        }

        cpu.agendarProcesso(proc4);
        cpu.agendarProcesso(proc5);
        //cpu.agendarProcesso(proc4);

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
