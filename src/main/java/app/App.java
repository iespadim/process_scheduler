package app;

import graph.GraphCpu;
import simulation.Cpu;
import simulation.Processo;
import simulation.scheduler.RoundRobin;
import simulation.scheduler.SJF;

import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class App {
    static boolean printDebug;
    static int escalonador;
    static int maxIdleCycles;
    static int maxSJFTimeAllowed;
    static Cpu cpu;
    static ArrayList<Processo> processosParaRegistrar;
    static ArrayList<ArrayList<String>> listaDeArquivosEArgumentos;

    public static void main(String[] args) throws Exception {
        processosParaRegistrar = new ArrayList<>();
        listaDeArquivosEArgumentos = new ArrayList<>();


        desenhaJanelaInput();
        processoArgumentosInput();
        registrarProcessos();
        cpu.executa();
        gerarGrafico();
    }

    private static void gerarGrafico() {
        SwingUtilities.invokeLater(() -> {
            GraphCpu graph = new GraphCpu("Grafico de uso de CPU por processo",true);
            graph.setSize(800, 400);
            graph.setLocationRelativeTo(null);
            graph.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            graph.setVisible(true);
        });
    }

    private static void registrarProcessos() {
        for(Processo processo : processosParaRegistrar) {
            cpu.agendarProcesso(processo);
        }
    }

    private static void processoArgumentosInput() {
        //argumentos da cpu
        escalonador = Integer.parseInt(listaDeArquivosEArgumentos.get(0).get(0));
        maxIdleCycles = Integer.parseInt(listaDeArquivosEArgumentos.get(0).get(1));
        maxSJFTimeAllowed = Integer.parseInt(listaDeArquivosEArgumentos.get(0).get(2));

        //argumentos de processos
        for (int i = 1; i < listaDeArquivosEArgumentos.size(); i++) {
            String narq = listaDeArquivosEArgumentos.get(i).get(0);
            Path path = Paths.get(narq);

            if(Files.exists(path) && !narq.equals("")) {
                ArrayList<String> linha = listaDeArquivosEArgumentos.get(i);

                for (int j = 0; j < linha.size(); j++) {
                    System.out.println(linha.get(j));
                    if (linha.get(j).equals("null") || linha.get(j) == null){
                        System.out.println("erro lendo argumento "+ j + " do arquivo " + i);
                        break;
                    }
                }

                int prioridade = Integer.parseInt(linha.get(1));
                int tempoDeEntrada = Integer.parseInt(linha.get(2));
                int quantum = Integer.parseInt(linha.get(3));

                System.out.println(narq);
                processosParaRegistrar.add(new Processo(narq,i,prioridade,0,tempoDeEntrada,quantum));
            }else{
                System.out.println("Arquivo "+ narq + " não encontrado");
            }
        }

        //select escalonador
        if(escalonador == 1) {
            cpu = new Cpu(new RoundRobin(printDebug), printDebug, maxIdleCycles);
        }else{
            //alterar para outro escalonador
            cpu = new Cpu(new SJF(printDebug, maxSJFTimeAllowed), printDebug, maxIdleCycles);
        }
    }

    private static void desenhaJanelaInput() {
        //cria janela de argumentos de entrada
        CountDownLatch latch = new CountDownLatch(1);


        javax.swing.SwingUtilities.invokeLater(() -> {
            FileSelector fileSelector = new FileSelector(result -> {
                listaDeArquivosEArgumentos.addAll(result);
                latch.countDown();
            });
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
