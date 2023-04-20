package simulation;

import graph.GraphCpuWatcher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import simulation.scheduler.PoliticaDeEscalonamento;

import java.util.ArrayList;
import java.util.HashMap;

@AllArgsConstructor
public class Cpu {
    public int idleTime;
    AssembleInterpreter interpretador;
    PoliticaDeEscalonamento scheduler;

    @Getter
    ArrayList<Processo> filaBloqueados;
    @Getter
    ArrayList<Processo> filaProntos;
    @Getter
    ArrayList<Processo> filaTerminados;
    @Getter
    ArrayList<Processo> filaEspera;
    @Getter
    HashMap<String,ArrayList> filas;
    @Getter
    Processo executando, executandoWatcher;

    @Getter
    boolean debugMode;

    @Getter
    @Setter
    int maxCycles, cpuTickCounter;

    private GraphCpuWatcher watcher;

    public Cpu(PoliticaDeEscalonamento s, boolean debugMode, int maxCycles) {
        scheduler = s;
        interpretador = new AssembleInterpreter(debugMode);
        this.maxCycles = maxCycles;
        this.debugMode = debugMode;

        this.filaBloqueados = new ArrayList<>();
        this.filaProntos = new ArrayList<>();
        this.filaTerminados = new ArrayList<>();
        this.filaEspera = new ArrayList<>();

        this.filas = new HashMap<>();
        filas.put("filaBloqueados", filaBloqueados);
        filas.put("filaProntos", filaProntos);
        filas.put("filaTerminados", filaTerminados);
        filas.put("filaEspera", filaEspera);


        cpuTickCounter= 0;
        watcher = GraphCpuWatcher.getInstance();
        watcher.setCpuTickCounter(cpuTickCounter);
    }

    public void executa() {
        int initialTick;
        // Sempre roda
        while (true) {
            // Verifica se chegou ao máximo de ciclos
            if (idleTime >= maxCycles && filaEspera.isEmpty() && filaBloqueados.isEmpty()) {
                break;
            }

            // Seleciona o próximo processo a ser executado
            executando = null;
            executandoWatcher = null;
            Processo p = scheduler.getNextProcess(this);

            executando = p;

            // Carrega o processo a ser executado
            int result = -2;

            if (p != null) {
                executandoWatcher = p;
                filaProntos.remove(p);
                interpretador.loadProcess(p);
                // Executa o processo
                initialTick = cpuTickCounter;
                int timesToRun = scheduler.getTimeAllowance(this);
                executando.setTimeRemaining(timesToRun);
                for (int i = 0; i < timesToRun; i++) {
                    result = interpretador.executa(1,p);
                    if (p.getId() == -1) {
                        idleTime++;
                        cpuTickCounter++;
                    }else{
                        cpuTickCounter++;
                        idleTime = 0;
                    }

                    // Processa o resultado da execução
                    if (result == -1) {
                        //processo em idle
                        if (debugMode) System.out.println("nao rodou devido a idle");
                    } else if (result == 0) {
                        //processo terminado
                        terminar(executando);
                        watcher.registrarProcesso(executandoWatcher, initialTick, cpuTickCounter);
                        break;
                    } else if (result == 1) {
                        // Processo bloqueado devido a saida
                        System.out.println(p.getAcc());
                        bloquear(executando);
                        watcher.registrarProcesso(executandoWatcher, initialTick, cpuTickCounter);
                        break;
                    } else if (result == 2) {
                        // Processo bloqueado devido a entrada
                        bloquear(executando);
                        watcher.registrarProcesso(executandoWatcher, initialTick, cpuTickCounter);
                        break;
                    }else if (result == 3) {
                        // acabou o tempo de execução
                        if(p.getPc() == p.getInstrucoes().size()){
                            terminar(executando);
                        }else{
                            agendarProcesso(executando);
                        }
                        executandoWatcher= executando;
                        interpretador.unload();
                        executando = null;
                        watcher.registrarProcesso(executandoWatcher, initialTick, cpuTickCounter);
                        break;
                    } else { // Processo pronto para continuar
                        //caso 10 - ok
                    }
                    if(i==timesToRun){
                        watcher.registrarProcesso(executandoWatcher, initialTick, cpuTickCounter);
                    }
                }
            }
        }
    }

    private void terminar(Processo executando) {
        if(executando.getId()!=-1){
            filaTerminados.add(executando);
        }
        interpretador.unload();
    }

    private void bloquear(Processo executando) {
        //random between 8 and 10
        int blockTime = (int) (Math.random() * 3) + 8;
        executando.setBlockedUntil(cpuTickCounter + blockTime);
        filaBloqueados.add(executando);
        interpretador.unload();
    }

    public void agendarProcesso(Processo proc) {
        //if processo.tempoDeEntrada < cpuTickCounter = add to fila espera
        if(proc.getTempoDeEntrada() > cpuTickCounter){
            filaEspera.add(proc);
        }else {
            filaProntos.add(proc);
        }
        proc.setTimeRemaining(scheduler.getTimeAllowance(proc));
    }
}
