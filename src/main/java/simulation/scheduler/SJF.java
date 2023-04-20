package simulation.scheduler;

import graph.GraphCpuWatcher;
import simulation.AssembleInterpreter;
import simulation.Cpu;
import simulation.Processo;
import simulation.scheduler.PoliticaDeEscalonamento;

import java.util.ArrayList;

public class SJF implements PoliticaDeEscalonamento {
    private boolean debugMode;
    private AssembleInterpreter interpretador;
    private GraphCpuWatcher watcher;
    int watcherCpuTickCounter;
    public SJF(boolean printDebug, int timeAllowed) {
        this.debugMode = debugMode;
        interpretador = new AssembleInterpreter(this.debugMode);
        watcher = GraphCpuWatcher.getInstance();
        watcherCpuTickCounter = watcher.getCpuTickCounter();
    }

    public Processo getNextProcess(Cpu cpu) {
        organizaFilas(cpu);

        int size = cpu.getFilaProntos().size();
        if (size > 0) {
            Processo processo = cpu.getFilaProntos().get(0);
            cpu.getFilaProntos().remove(0);
            return processo;
        } else{
            return Processo.idleProcess();
        }
    }

    private void organizaFilas(Cpu cpu) {
        ArrayList<Processo> filaProntos = (ArrayList<Processo>) cpu.getFilaProntos().clone();
        ArrayList<Processo> filaEspera = cpu.getFilaEspera();
        ArrayList<Processo> filaBloqueados = cpu.getFilaBloqueados();

        cpu.getFilaProntos().clear();

        if (!filaBloqueados.isEmpty()){
            for (int i = 0; i < filaBloqueados.size(); i++) {
                if (cpu.getCpuTickCounter() >= filaBloqueados.get(i).getBlockedUntil()){
                    filaBloqueados.get(i).setBlockedUntil(0);
                    insereNaArrayListComPrioridade(cpu,filaBloqueados.get(i));
                    filaBloqueados.remove(filaBloqueados.get(i));
                }
            }
        }

        if (!filaEspera.isEmpty()){
            for (int i = 0; i < filaEspera.size(); i++) {
                if (cpu.getCpuTickCounter() >= filaEspera.get(i).getTempoDeEntrada()){
                    insereNaArrayListComPrioridade(cpu,filaEspera.get(i));
                    filaEspera.remove(filaEspera.get(i));
                }
            }
        }

        //ArrayList newFilaProntos = new ArrayList<Processo>();
        for (int i = 0; i < filaProntos.size(); i++) {
            insereNaArrayListComPrioridade(cpu,filaProntos.get(i));
        }
    }

    public int getTimeAllowance(Cpu cpu) {
        return 20;
    }

    public int getTimeAllowance(Processo p) {
        return 20;
    }

    public void insereNaArrayListComPrioridade(Cpu cpu, Processo processoInserido) {
        int posicaoInsercao = 0;
        ArrayList<Processo> lista = cpu.getFilaProntos();

        // Encontrar a posição correta de inserção com base na prioridade
        //O número de posições de um programa é então definido pelo número de instruções somado ao
        //número de variáveis
        for (int i = 0; i < lista.size(); i++) {
            if ((processoInserido.getInstrucoes().size()+processoInserido.getDados().size()) <
                    (lista.get(i).getInstrucoes().size()+ lista.get(i).getDados().size())) {
                posicaoInsercao = i;
                break;
            }
            posicaoInsercao = i + 1;
        }
        // Inserir o processo na posição correta
        lista.add(posicaoInsercao, processoInserido);
    }


}
