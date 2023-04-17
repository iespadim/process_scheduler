package graph; /**
 *
 * @author yunus sharum
 * Modified: 13 April 2018
 * Version : 0.1
 *
 * About: Demonstration of CPU scheduling simulation using RR Scheduling 
 *        algorithm. 
 *
 *        The simulation chart is generated using JFreeChart library.
 *
 *
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;


public class RRchart extends JFrame {

    class GraphProcess {
        public String id;
        public int burstTime;
        public int arrivalTime;
        public int waitingTime;
        public int turnaroundTime;
        public int startTime;
        public int numOfTimesStopped;
        public ArrayList<GraphProcess> subtask;

        public GraphProcess(String id, int burstTime, int arrivalTime)
        {
            this.id = id;
            this.burstTime = burstTime;
            this.arrivalTime = arrivalTime;
            this.waitingTime = 0;
            this.turnaroundTime = 0;
            this.startTime = -1;            // -1, process has never started
            this.numOfTimesStopped = 0;
            this.subtask = new ArrayList(); // to keep track list of subtasks
        }
    }

    public RRchart(String title) {

        super(title);

        // Simulation data
        GraphProcess[] simulationData = new GraphProcess[5];

        simulationData[0] = new GraphProcess("A", 6, 0);  // id, burst time, arrival time,
        simulationData[1] = new GraphProcess("B", 4, 0);
        simulationData[2] = new GraphProcess("C", 8, 0);
        simulationData[3] = new GraphProcess("D", 3, 0);
        simulationData[4] = new GraphProcess("E", 5, 0);

        // Run simulation with quantum=4
        final int QUANTUM_SLICE = 3;
        simulationData = simulateRR(simulationData, QUANTUM_SLICE);

        // Create dataset for chart
        IntervalCategoryDataset dataset = buildChartDataset(simulationData, "FCFS");

        // Create chart
        JFreeChart chart = ChartFactory.createGanttChart(
                "CPU Scheduling",    // title
                "Process",           // X-Axis label
                "Timeline",          // Y-Axis label
                dataset);

        // set x-axis range from 0
        ( chart.getCategoryPlot() ).setRangeAxis(new NumberAxis());

        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);


        int totWaitTime = 0;
        System.out.printf( "%10s %10s %10s %10s %10s %10s %10s\n",
                "id","arrival/t","burst/t","start/t","waiting/t",
                "t'round/t","stops(n)");
        for(int i=0; i < simulationData.length; i++) {
            totWaitTime += simulationData[i].waitingTime;
            System.out.printf("%10s %10s %10s %10s %10s %10s %10s\n",
                    simulationData[i].id,
                    simulationData[i].arrivalTime,
                    simulationData[i].burstTime,
                    simulationData[i].startTime,
                    simulationData[i].waitingTime,
                    simulationData[i].turnaroundTime,
                    simulationData[i].numOfTimesStopped);
        }

        System.out.println("\nTime quantum = " + QUANTUM_SLICE);
        System.out.print("Avg waiting time (t) = ");
        System.out.printf("%.2f\n", (float)totWaitTime / simulationData.length);


    }


    private GraphProcess[] simulateRR(GraphProcess[] pt, int quantum)
    {

        // RR-queue, to manage the round-robin scheduling
        class RRQItem {
            public GraphProcess process;
            public int remaining;
            public int stopped;
        }
        Queue<RRQItem> rrQ = new LinkedList();   // Round-Robin queue

        int clk_tick = 0;                        // reset clk_tick

        int cpu_tick = 0;             // timer for running process
        boolean cpu_idle = true;      // if not idle, then CPU is busy

        int totDone = 0;
        while(totDone < pt.length) {

            // is it time to execute (submit to queue) ?
            for(int p=0; p < pt.length; p++ ) {
                if (clk_tick == pt[p].arrivalTime) {

                    // put inside RR-queue
                    RRQItem item = new RRQItem();
                    item.process = pt[p];
                    item.remaining = pt[p].burstTime;
                    item.stopped = pt[p].arrivalTime;
                    rrQ.add(item);
                }
            }
            if(!cpu_idle) {
                cpu_tick--;           // deplete cpu burst time
                if(cpu_tick <= 0) {   // if timeout
                    // remove currently running process
                    RRQItem running = rrQ.remove();
                    cpu_idle = true;

                    if(running.remaining > 0) {       // if not yet finish
                        running.stopped = clk_tick;   // record when this process
                        //   is stopped
                        running.process.numOfTimesStopped++;   // record stoppage
                        //   count
                        rrQ.add(running);             // put back into queue

                    } else {
                        // process finish, calculate turnaround time
                        running.process.turnaroundTime =
                                clk_tick - running.process.arrivalTime;
                        totDone++;

                    }
                }
            }
            // if CPU idle, and no process is running
            if(cpu_idle) {
                // submit next remaining process in queue
                if(!rrQ.isEmpty()) {
                    RRQItem front = rrQ.peek();            // check who is at
                    //   the front of Q

                    if(front.process.startTime < 0) {      // record the first
                        //   time process start
                        front.process.startTime = clk_tick;  // record starting
                        //   time
                    }
                    front.process.waitingTime += clk_tick - front.stopped;

                    // submit and set for how long is the execution time
                    cpu_idle = false;
                    cpu_tick = (front.remaining >= quantum ? quantum :
                            front.remaining);

                    GraphProcess part = new GraphProcess( "" + front.process.id + "_" +
                                                front.process.subtask.size(), cpu_tick, clk_tick);
                                        part.startTime = clk_tick;
                                        part.waitingTime = clk_tick - front.stopped;

                    // add new part (sub-process)
                    front.process.subtask.add( part );

                    // update remaining time for this process
                    front.remaining -= cpu_tick;

                }
            }
            clk_tick++;                     // update clk_tick
        }
        return pt;
    }



    private IntervalCategoryDataset buildChartDataset(GraphProcess[] pt, String label) {

        TaskSeries series = new TaskSeries(label);
        for(int i=0; i < pt.length; i++) {

            Task t = new Task( pt[i].id,
                    new SimpleTimePeriod( pt[i].startTime,
                            pt[i].turnaroundTime));

            // show subtasks (if any)
            for(int j=0; j < pt[i].subtask.size(); j++) {
                GraphProcess p = pt[i].subtask.get(j);
                Task subt = new Task( p.id,
                        new SimpleTimePeriod( p.startTime, p.startTime +
                                p.burstTime));
                t.addSubtask(subt);
            }

            series.add( t );
        }

        TaskSeriesCollection dataset = new TaskSeriesCollection();
        dataset.add(series);
        return dataset;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            RRchart chart = new RRchart("CPU Scheduling Algorithm");
            chart.setSize(800, 400);
            chart.setLocationRelativeTo(null);
            chart.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            chart.setVisible(true);
        });

    }

}