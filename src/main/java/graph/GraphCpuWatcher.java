package graph;

import lombok.Getter;
import simulation.Processo;

import java.util.ArrayList;

public class GraphCpuWatcher {
    private static GraphCpuWatcher instance = null;
    @Getter
    ArrayList<ProcessoGraphObj> processosGraph;
    @Getter
    ArrayList<int[]> tempos;
    @Getter
    private int cpuTickCounter;


    public void registrarProcesso(Processo p, int cicloInicial, int cicloFinal){
        if(processosGraph.isEmpty()){
            processosGraph.add(new ProcessoGraphObj(p, cicloInicial, cicloFinal));
            return;
        }

        boolean contem = false;
        int index = 0;
        for (int i = 0; i < processosGraph.size(); i++) {
            if (processosGraph.get(i).getId() == p.getId()) {
                contem = true;
                index = i;
            }
        }

        if(contem){
           processosGraph.get(index).getFilhos().add(new ProcessoGraphObj(p, cicloInicial, cicloFinal));
        }else{
            processosGraph.add(new ProcessoGraphObj(p, cicloInicial, cicloFinal));
        }
    }

    //singleton
    private GraphCpuWatcher() {
        processosGraph = new ArrayList<ProcessoGraphObj>();
        tempos = new ArrayList<int[]>();
    }
    public static GraphCpuWatcher getInstance() {
        if(instance == null) {
            instance = new GraphCpuWatcher();
        }
        return instance;
    }

    public void incrementTickCounter() {
        cpuTickCounter++;
    }

}

