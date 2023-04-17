package simulation;

import graph.GraphCpuWatcher;

import java.util.NoSuchElementException;

public class Heapprocess {
    private Processo[] heapArray;
    private int currentSize;
    private boolean debugMode;
    private AssembleInterpreter interpretador;
    private GraphCpuWatcher watcher;
    int cpuTickCounter;


    public Heapprocess(int maxSize,boolean debugMode) {
        heapArray = new Processo[maxSize];
        currentSize = 0;
        this.debugMode = debugMode;
        interpretador = new AssembleInterpreter(this.debugMode);
        watcher = GraphCpuWatcher.getInstance();
        cpuTickCounter = watcher.getCpuTickCounter();
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

    public void insert(Processo processo) {
        if (currentSize == heapArray.length) {
            System.out.println("Heap cheio, não é possível inserir mais elementos.");
            return;
        }

        heapArray[currentSize] = processo;
        int index = currentSize;
        currentSize++;

        shiftUp(index);
    }

    private void shiftUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;

            if (heapArray[index].getPrio() > heapArray[parentIndex].getPrio()) {
                break;
            } else if (heapArray[index].getPrio() == heapArray[parentIndex].getPrio()) {
                if (index > parentIndex) {
                    break;
                }
            }

            Processo temp = heapArray[index];
            heapArray[index] = heapArray[parentIndex];
            heapArray[parentIndex] = temp;
            index = parentIndex;
        }
    }

    public Processo removeMin() {
        if (isEmpty()) {
            System.out.println("Heap vazio, não é possível remover elementos.");
            return null;
        }

        Processo min = heapArray[0];
        heapArray[0] = heapArray[currentSize - 1];
        currentSize--;

        shiftDown(0);

        return min;
    }

    public void shiftDown(int index) {//Deixar esse publico, acaba o Quantum da o shiftDown, ele passa a ser o ultimo processo com essa prioridade a executar
        while (true) {
            int leftChildIndex = 2 * index + 1;
            int rightChildIndex = 2 * index + 2;
            int smallestChildIndex = index;

            if (leftChildIndex < currentSize && heapArray[leftChildIndex].getPrio() < heapArray[smallestChildIndex].getPrio()) {
                smallestChildIndex = leftChildIndex;
            }

            if (rightChildIndex < currentSize && heapArray[rightChildIndex].getPrio() < heapArray[smallestChildIndex].getPrio()) {
                smallestChildIndex = rightChildIndex;
            }

            if (smallestChildIndex == index) {
                break;
            }

            Processo temp = heapArray[index];
            heapArray[index] = heapArray[smallestChildIndex];
            heapArray[smallestChildIndex] = temp;
            index = smallestChildIndex;
        }
    }


    public Processo peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap vazio");
        }
        return heapArray[0];
    }

    public void executa(){
        //Executar uma linha do asm do primeiro da fila heapArray[0]
        //Se o processo acabar, remove da fila
        //Se não acabar, atualiza a prioridade e insere na fila novamente
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