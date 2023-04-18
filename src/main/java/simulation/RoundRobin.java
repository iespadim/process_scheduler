package simulation;

import graph.GraphCpuWatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class RoundRobin {

    private class Node {
        Processo processo;
        Node next;

        Node(Processo processo2) {
            this.processo = processo2;
        }
    }

    private Node head;
    private boolean debugMode;
    private AssembleInterpreter interpretador;
    private GraphCpuWatcher watcher;
    private ArrayList<Processo> finalizados;
    private HashMap<Integer, Processo> meusProcessos;
    int cpuTickCounter;

    
    public RoundRobin(boolean debugMode) {
        this.debugMode = debugMode;
        meusProcessos = new HashMap<Integer, Processo>();
        finalizados = new ArrayList<Processo>();
        interpretador = new AssembleInterpreter(this.debugMode);
        watcher = GraphCpuWatcher.getInstance();
        cpuTickCounter = watcher.getCpuTickCounter();
    }

    public void recebeProc(Integer tempExec, Processo proc) {
        this.meusProcessos.put(tempExec, proc);
    }

    private void finalizaProc(Integer chave){
        this.finalizados.add(meusProcessos.get(chave));
    }

    public void insert(Processo processo) {
        head = insertRecursive(head, new Node(processo));//recursao inicial com a cabeça e novo nodo
    }
    
    private Node insertRecursive(Node atualComparado, Node nodoInserido) {
        if (atualComparado == null || nodoInserido.processo.getPrio() < atualComparado.processo.getPrio() ) {//Se a cabeça é nula ou o processo tem maior prioridade
            nodoInserido.next = atualComparado;//Nodo inserido.next = cabeça
            return nodoInserido;//Cabeça agora é o nodo inserido
        }else {
            atualComparado.next = insertRecursive(atualComparado.next, nodoInserido);//segue a lista
            return atualComparado;
        }
    }


    public Processo removeMin() {
        if (head == null) {
            return null;
        }
        
        Processo minProcess = head.processo;
        head = head.next;
        return minProcess;
    }

    private boolean isEmpty() {
        if(this.head == null){
            return true;
        }
        return false;
    }

    

    public void executa(){
        //Executar uma linha do asm do primeiro da fila head
        //Se o processo acabar, remove da fila
        //Se não acabar, atualiza e insere na fila novamente
        int result;
        while(finalizados.size() != meusProcessos.size()){//Enquanto a lista de processos finalizados tem tamanho diferente dos processos carregados
            if(meusProcessos.containsKey(watcher.getCpuTickCounter())){//Se há processo que inicia neste tempo
                insert(meusProcessos.get(watcher.getCpuTickCounter()));//Ele é inserido na fila de prontos
            }
            //int initialTick = watcher.getCpuTickCounter();
            if(!isEmpty()){//Se a fila não está vazia
                Processo proc = removeMin();//proc recebe cabeça da fila
                interpretador.load(proc);//Interpreta proc
                int initialTick = watcher.getCpuTickCounter();
                result=interpretador.executa(proc.getQuantum());
            }
            //result=interpretador.executa(proc.getQuantum());
            if(result==1){
                if(debugMode) System.out.println("Processo "+proc.getId()+" executou mais uma vez");
                proc.setPrio(proc.getPrio()+1);
                interpretador.unload();
                insert(proc);
            }else{
                if(debugMode) System.out.println("Processo "+proc.getId()+" terminou");
                interpretador.unload();
            }
            watcher.registrarProcesso(proc, initialTick, watcher.getCpuTickCounter());
            System.out.println("Processo "+proc.getId()+" executou de "+initialTick+" a "+watcher.getCpuTickCounter());
            if(debugMode) System.out.println("Processo "+proc.getId()+" executou "+(watcher.getCpuTickCounter()-initialTick)+" ticks de CPU");
        }
    }

   
    
}
