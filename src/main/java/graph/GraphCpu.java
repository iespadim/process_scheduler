package graph;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import simulation.Processo;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;

public class GraphCpu extends JFrame {
    private static GraphCpuWatcher watcher;
    private final boolean debugMode;

    public GraphCpu(String title, boolean debugMode) {
        super(title);
        this.watcher = watcher.getInstance();
        this.debugMode = debugMode;

        IntervalCategoryDataset dataset = createDataset();

        JFreeChart chart = ChartFactory.createGanttChart(
                "Uso de CPU por processo",
                "Processo",
                "Tempo",
                dataset
        );
        (chart.getCategoryPlot()).setRangeAxis(new NumberAxis("Tempo (unidades)"));
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);

    }

    private IntervalCategoryDataset createDataset() {
        watcher = watcher.getInstance();
        ArrayList<ProcessoGraphObj> processos = watcher.getProcessosGraph();

        TaskSeries series = new TaskSeries("Processos");

        for (int i = 0; i < processos.size(); i++) {
            int processid = processos.get(i).getId();

            //calcula largura em unidades de tempo
            //minInitTick in processos.get(i).getInitTick()
            //maxEndTick in processos.get(i).getEndTick()
            int minInitTick = processos.get(i).filhos.get(0).getInitTick();
            int qtFilhos = processos.get(i).getFilhos().size();
            int maxEndTick = processos.get(i).filhos.get(qtFilhos-1).getEndTick();

            Task t;
            if(processid ==-1){
                // desenhar idle
                t = new Task("Idle", new SimpleTimePeriod(minInitTick,maxEndTick));
            } else{
                // desenhar processo normal
                t = new Task("Processo " + processid, new SimpleTimePeriod(minInitTick,maxEndTick));
                System.out.println("processo " + processid + " iniciou em " + minInitTick + " e terminou em " + maxEndTick);
            }

            //filhos
            for (int j = 0; j < processos.get(i).getFilhos().size(); j++) {
                ProcessoGraphObj p = processos.get(i).getFilhos().get(j);
                System.out.println("etapa de " + processid + " iniciou em " + p.getInitTick() + " e terminou em " + p.getEndTick());
                Task subt = new Task("Processo " + processid, new SimpleTimePeriod(p.getInitTick(),p.getEndTick()));
                t.addSubtask(subt);
            }
            series.add(t);

        }
        //calcula processingTime
        System.out.println("calculando processingTime");
        for (ProcessoGraphObj pai : processos) {
            int duracaoTotal = 0;
            for (ProcessoGraphObj filho: pai.getFilhos()) {
                int duracaoParticial = filho.getEndTick() - filho.getInitTick();
                duracaoTotal += duracaoParticial;
            }
            pai.setProcessingTime(duracaoTotal);
            if(pai.getId()==-1){
                System.out.println("tempo idle foi de " + duracaoTotal);
            }else{
                System.out.println("processingTime de " + pai.getId() + " foi de " + duracaoTotal);
            }
        }
        System.out.println();
        System.out.println("calculando turnaroundTime");
        //calcula turnaroundTime
        for (ProcessoGraphObj pai : processos) {
            int minInitTick = pai.filhos.get(0).getInitTick();
            int maxEndTick = pai.filhos.get(pai.getFilhos().size()-1).getEndTick();

            int duracaoTotal = maxEndTick - minInitTick;

            if(pai.getId()!=-1){
                pai.setTurnaroundTime(duracaoTotal);
                System.out.println("turnaroundTime de " + pai.getId() + " foi de " + duracaoTotal);
            }else{
                pai.setTurnaroundTime(duracaoTotal);
            }
        }

        //nao deu tempo de terminar
//        System.out.println();
//        System.out.println("calculando tempo de espera");
//        for (ProcessoGraphObj pai : processos) {
//            if(pai.getId()!=-1){
//                int tempoDeEspera = pai.getWaitingTime();
//                System.out.println("tempo de espera de " + pai.getId() + " foi de " + tempoDeEspera);
//            }
//        }

        TaskSeriesCollection dataset = new TaskSeriesCollection();
        dataset.add(series);
        return dataset;
    }
}
