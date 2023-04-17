package simulation;

import graph.GraphCpuWatcher;

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
    int cpuTickCounter;


    public RoundRobin(boolean debugMode) {
        this.debugMode = debugMode;
        interpretador = new AssembleInterpreter(this.debugMode);
        watcher = GraphCpuWatcher.getInstance();
        cpuTickCounter = watcher.getCpuTickCounter();
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
        while(!isEmpty()){
            Processo proc = removeMin();
            interpretador.load(proc);
            int initialTick = watcher.getCpuTickCounter();
            result=interpretador.executa(proc.getQuantum());
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
