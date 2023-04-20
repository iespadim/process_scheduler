package graph;

import lombok.Getter;
import lombok.Setter;
import simulation.Processo;

import java.util.ArrayList;

public class GraphCpuWatcher {
    private static GraphCpuWatcher instance = null;
    @Getter
    ArrayList<ProcessoGraphObj> processosGraph;
    @Getter
    ArrayList<int[]> tempos;
    @Getter
    @Setter
    private int cpuTickCounter;


    public void registrarProcesso(Processo p, int cicloInicial, int cicloFinal){
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
            processosGraph.get(processosGraph.size()-1).getFilhos().add(new ProcessoGraphObj(p, cicloInicial, cicloFinal));
        }
    }

    public void registrarEspera(Processo p, int qnt){
        boolean contem = false;
        int index = 0;
        for (int i = 0; i < processosGraph.size(); i++) {
            if (processosGraph.get(i).getId() == p.getId()) {
                contem = true;
                index = i;
            }
        }

        if(contem){
            processosGraph.get(index).setWaitingTime(processosGraph.get(index).getWaitingTime() + qnt);
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

