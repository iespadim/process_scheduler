package simulation;

import graph.GraphCpuWatcher;

public class SJF {
    private boolean debugMode;
    private AssembleInterpreter interpretador;
    private GraphCpuWatcher watcher;
    int watcherCpuTickCounter;
    public SJF(boolean printDebug) {
        this.debugMode = debugMode;
        interpretador = new AssembleInterpreter(this.debugMode);
        watcher = GraphCpuWatcher.getInstance();
        watcherCpuTickCounter = watcher.getCpuTickCounter();
    }
}
