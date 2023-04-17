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

    public GraphCpu(String title) {
        super(title);
        this.watcher = watcher.getInstance();

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
            Task t = new Task("Processo " + processid, new SimpleTimePeriod(processos.get(i).getInitTick(), processos.get(i).getEndTick()));

            //filhos
            for (int j = 0; j < processos.get(i).getFilhos().size(); j++) {
                ProcessoGraphObj p = processos.get(i).getFilhos().get(j);
                Task subt = new Task("Processo " + processid, new SimpleTimePeriod(p.getInitTick(),p.getEndTick()));
                t.addSubtask(subt);
            }

            series.add(t);

        }
        TaskSeriesCollection dataset = new TaskSeriesCollection();
        dataset.add(series);
        return dataset;
    }
}
