package graph;

import lombok.Getter;
import lombok.Setter;
import simulation.Processo;

import java.util.ArrayList;

class ProcessoGraphObj {
    @Getter
    @Setter
    private int id, prio, pc, acc, quantum, initTick, endTick,processingTime, waitingTime, turnaroundTime;
    @Getter
    ArrayList<ProcessoGraphObj> filhos;

    public ProcessoGraphObj(Processo p, int initTick, int endTick) {
        this.id = p.getId();
        this.prio = p.getPrio();
        this.pc = p.getPc();
        this.acc = p.getAcc();
        this.quantum = p.getQuantum();
        this.initTick = initTick;
        this.endTick = endTick;
        this.filhos = new ArrayList<ProcessoGraphObj>();
    }
}
