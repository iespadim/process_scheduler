package simulation;

public interface PoliticaDeEscalonamento {

    public Processo getNextProcess(Cpu cpu);

    int getTimeAllowance(Cpu cpu);


    int getTimeAllowance(Processo proc);
}
