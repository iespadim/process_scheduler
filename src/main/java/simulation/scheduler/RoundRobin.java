package simulation.scheduler;

import graph.GraphCpuWatcher;
import simulation.AssembleInterpreter;
import simulation.Cpu;
import simulation.Processo;
import simulation.scheduler.PoliticaDeEscalonamento;

import java.util.*;


public class RoundRobin implements PoliticaDeEscalonamento {
    private boolean debugMode;
    private AssembleInterpreter interpretador;
    private GraphCpuWatcher watcher;
    int watcherCpuTickCounter;
//    private Node head;


    public RoundRobin(boolean debugMode) {
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

    public int getTimeAllowance(Cpu cpu) {
        return cpu.getExecutando().getQuantum();
    }

    public int getTimeAllowance(Processo p) {
        return p.getQuantum();
    }

    public void organizaFilas(Cpu cpu) {

        ArrayList<Processo> filaProntos = (ArrayList<Processo>) cpu.getFilaProntos().clone();
        ArrayList<Processo> filaEspera = cpu.getFilaEspera();
        ArrayList<Processo> filaBloqueados = cpu.getFilaBloqueados();

        cpu.getFilaProntos().clear();
        //ArrayList newFilaProntos = new ArrayList<Processo>();
        for (int i = 0; i < filaProntos.size(); i++) {
            insereNaArrayListComPrioridade(cpu,filaProntos.get(i));
        }

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

    }
    public void insereNaArrayListComPrioridade(Cpu cpu, Processo processoInserido) {
        int posicaoInsercao = 0;
        ArrayList<Processo> lista = cpu.getFilaProntos();

        // Encontrar a posição correta de inserção com base na prioridade
        for (int i = 0; i < lista.size(); i++) {
            if (processoInserido.getPrio() < lista.get(i).getPrio()) {
                posicaoInsercao = i;
                break;
            }
            posicaoInsercao = i + 1;
        }
        // Inserir o processo na posição correta
        lista.add(posicaoInsercao, processoInserido);
    }

//    private class Node {
//        Processo processo;
//        Node next;
//
//        Node(Processo processo2) {
//            this.processo = processo2;
//        }
//    }
//
//    public void insert(Processo processo) {
//        head = insertRecursive(head, new Node(processo));//recursao inicial com a cabeça e novo nodo
//        //filaProntos.add(processo);
//    }
//
//    private Node insertRecursive(Node atualComparado, Node nodoInserido) {
//        if (atualComparado == null || nodoInserido.processo.getPrio() < atualComparado.processo.getPrio() ) {//Se a cabeça é nula ou o processo tem maior prioridade
//            nodoInserido.next = atualComparado;//Nodo inserido.next = cabeça
//            return nodoInserido;//Cabeça agora é o nodo inserido
//        }else {
//            atualComparado.next = insertRecursive(atualComparado.next, nodoInserido);//segue a lista
//            return atualComparado;
//        }
//    }
//
//    public Processo removeMin() {
//        if (head == null) {
//            return null;
//        }
//
//        Processo minProcess = head.processo;
//        head = head.next;
//        return minProcess;
//    }
//
//    private boolean isEmpty() {
//        if(this.head == null){
//            return true;
//        }
//        return false;
//    }
//
//
//
//    public void executa(){
//        int result;
//
//        while(!isEmpty()){
//            Processo proc = removeMin();
//            interpretador.loadProcess(proc);
//            int initialTick = watcher.getCpuTickCounter();
//            result=interpretador.executa(proc.getQuantum(),proc);
//
//            if(result==1){
//                if(debugMode) System.out.println("Processo "+proc.getId()+" executou mais uma vez");
//                interpretador.unload();
//                insert(proc);
//            }else{
//                if(debugMode) System.out.println("Processo "+proc.getId()+" terminou");
//                interpretador.unload();
//            }
//            watcher.registrarProcesso(proc, initialTick, watcher.getCpuTickCounter());
//            System.out.println("Processo "+proc.getId()+" executou de "+initialTick+" a "+watcher.getCpuTickCounter());
//            if(debugMode) System.out.println("Processo "+proc.getId()+" executou "+(watcher.getCpuTickCounter()-initialTick)+" ticks de CPU");
//        }
//    }
}
