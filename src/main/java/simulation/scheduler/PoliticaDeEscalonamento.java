package simulation.scheduler;

import simulation.Cpu;
import simulation.Processo;

public interface PoliticaDeEscalonamento {

    public Processo getNextProcess(Cpu cpu);

    int getTimeAllowance(Cpu cpu);


    int getTimeAllowance(Processo proc);
}
