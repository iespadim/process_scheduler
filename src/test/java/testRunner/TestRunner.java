package testRunner;

import graph.GraphCpu;
import simulation.Cpu;
import simulation.Processo;
import simulation.RoundRobin;
import simulation.SJF;

import javax.swing.*;
import java.util.ArrayList;


public class TestRunner {

    public ArrayList<Processo> run(ArrayList<Processo> processos, boolean printDebug, int escalonador, int maxCycles, int maxSJFTimeAllowed,boolean drawGraph) {
        Cpu cpu;

        //select escalonador
        if(escalonador == 1) {
            cpu = new Cpu(new RoundRobin(printDebug), printDebug, maxCycles);
        }else{
            //alterar para outro escalonador
            cpu = new Cpu(new SJF(printDebug,maxSJFTimeAllowed), printDebug, maxCycles);
        }

        for(Processo processo : processos) {
            cpu.agendarProcesso(processo);
        }
        cpu.executa();

        //gera grafico
        if (drawGraph){
            SwingUtilities.invokeLater(() -> {
                GraphCpu graph = new GraphCpu("Grafico de uso de CPU por processo",true);
                graph.setSize(800, 400);
                graph.setLocationRelativeTo(null);
                graph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                graph.setVisible(true);
            });
        }

        return cpu.getFilaTerminados();
    }
}
