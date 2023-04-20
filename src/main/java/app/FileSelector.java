package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.function.Consumer;

public class FileSelector extends JFrame {
    private JTextField schedulerInput;
    private JTextField maxIdleCyclesInput;
    private JTextField SJF_cpuTimeInput;
    private JTextField[][] inputs;

    public FileSelector(Consumer<ArrayList<ArrayList<String>>> callback) {
        setTitle("File Selector");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(9, 6));
        setSize(1000, 300);

        String currDir = Paths.get("").toAbsolutePath().toString();
        String folderPath = currDir + "\\input";

        File folder = new File(folderPath);
        FilenameFilter txtFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".txt");
            }
        };

        File[] txtFiles = folder.listFiles(txtFilter);

        inputs = new JTextField[6][4];

        //scheduler selector
        JLabel schedulerSelector = new JLabel("Scheduler (0 SJF,1 RR): ",SwingConstants.RIGHT);
        add(schedulerSelector);
        schedulerInput = new JTextField(5);
        schedulerInput.setText("0");
        add(schedulerInput);

        //idle cycles
        add(new JLabel("Max cpu Idle Cycles: ",SwingConstants.RIGHT));
        maxIdleCyclesInput = new JTextField(10);
        maxIdleCyclesInput.setText("100");
        add(maxIdleCyclesInput);

        //sjf cpu time
        add(new JLabel("SJF CPU Time",SwingConstants.RIGHT));
        SJF_cpuTimeInput = new JTextField(10);
        SJF_cpuTimeInput.setText("25");
        add(SJF_cpuTimeInput);

        //add line break
        for (int i = 0; i < 2; i++) {
            add(new JLabel());
        }

        //rotulos
        add(new JLabel("Nome arquivo",SwingConstants.CENTER));
        add(new JLabel("Prioridade",SwingConstants.CENTER));
        add(new JLabel("Tempo de Entrada",SwingConstants.CENTER));
        add(new JLabel("Quantum",SwingConstants.CENTER));
        add(new JLabel());
        add(new JLabel());


        //inputs
        for (int i = 0; i < 6; i++) {
            add(new JLabel("Processo ID " + (i + 1)+": ",SwingConstants.RIGHT));
            for (int j = 0; j < 4; j++) {
                if (j == 0) {
                    if (txtFiles != null) {
                        Vector<String> fileNames = new Vector<>();
                        fileNames.add("");
                        Arrays.stream(txtFiles).forEach(file -> fileNames.add(file.getName()));
                        JComboBox<String> comboBox = new JComboBox<>(fileNames);
                        inputs[i][j] = new JTextField(comboBox.getSelectedItem().toString());
                        comboBox.addActionListener(createComboBoxListener(i, j));
                        add(comboBox);
                    } else {
                        System.err.println("Error: The specified folder does not exist or cannot be read.");
                    }
                } else {
                    JTextField jtf = new JTextField(10);
                    jtf.setText("0");
                    inputs[i][j] = jtf;
                    add(inputs[i][j]);
                }
            }
            add(new JLabel());
            add(new JLabel());
        }


        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<ArrayList<String>> fileListAndArgs = new ArrayList<>();

                ArrayList<String> schedulerArgs = new ArrayList<>();
                schedulerArgs.add(schedulerInput.getText());
                schedulerArgs.add(maxIdleCyclesInput.getText());
                schedulerArgs.add(SJF_cpuTimeInput.getText());
                fileListAndArgs.add(schedulerArgs);

                for (int i = 0; i < 6; i++) {
                    ArrayList<String> lineArgs = new ArrayList<>();
                    for (int j = 0; j < 4; j++) {
                        lineArgs.add(inputs[i][j].getText());
                    }
                    fileListAndArgs.add(lineArgs);
                }
                callback.accept(fileListAndArgs);
                dispose();
            }
        });

        for (int i = 0; i < 5; i++) {
            add(new JLabel());
        }
        add(startButton);
        setVisible(true);
    }

    private ActionListener createComboBoxListener(int i, int j) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> source = (JComboBox<String>) e.getSource();
                inputs[i][j].setText(source.getSelectedItem().toString());
            }
        };
    }
}