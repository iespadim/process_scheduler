import java.util.NoSuchElementException;

public class Heapprocess {
    private Processo[] heapArray;
    private int currentSize;

    public Heapprocess(int maxSize) {
        heapArray = new Processo[maxSize];
        currentSize = 0;
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

    }
    
}
