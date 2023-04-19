package testCases;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import simulation.Processo;
import testRunner.TestRunner;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class Teste_resultado_programa {

    @Test
    public void teste_roda_1_programa_sem_loop_a_b_c_SJF(){
        TestRunner testRunner = new TestRunner();
        ArrayList<Processo> testData = new ArrayList<Processo>();

        testData.add(new Processo("src/test/java/testFiles/a_igual_b_mais_c.txt", 1,1,0,0,4));

        ArrayList<Processo> testResuts = testRunner.run(testData, true, 0, 20,20, false);

        //para cada processo nos resultados, verificar se o valor esperado é igual ao valor obtido no  processo
        for(Processo processo : testResuts) {
            Assertions.assertEquals(18, processo.getAcc());
        }
    }

    @Test
    public void teste_roda_1_programa_sem_loop_a_b_c_RR(){
        TestRunner testRunner = new TestRunner();
        ArrayList<Processo> testData = new ArrayList<Processo>();

        testData.add(new Processo("src/test/java/testFiles/a_igual_b_mais_c.txt", 1,1,0,0,4));

        ArrayList<Processo> testResuts = testRunner.run(testData, true, 1, 20,20, false);

        //para cada processo nos resultados, verificar se o valor esperado é igual ao valor obtido no  processo
        for(Processo processo : testResuts) {
            Assertions.assertEquals(18, processo.getAcc());
        }
    }

    @Test
    public void teste_roda_1_programa_sem_loop_out_igual_entrada_vezes_10_RR(){
        TestRunner testRunner = new TestRunner();
        ArrayList<Processo> testData = new ArrayList<Processo>();

        String input = "2";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        testData.add(new Processo("src/test/java/testFiles/out_igual_entrada_vezes_10.txt", 1,1,0,0,4));

        ArrayList<Processo> testResuts = testRunner.run(testData, true, 1, 20,20, false);

        //para cada processo nos resultados, verificar se o valor esperado é igual ao valor obtido no  processo
        for(Processo processo : testResuts) {
            Assertions.assertEquals(20, processo.getAcc());
        }
    }

    @Test
    public void teste_roda_1_programa_sem_loop_out_igual_entrada_vezes_10_SJF(){
        TestRunner testRunner = new TestRunner();
        ArrayList<Processo> testData = new ArrayList<Processo>();

        String input = "2";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        testData.add(new Processo("src/test/java/testFiles/out_igual_entrada_vezes_10.txt", 1,1,0,0,4));

        ArrayList<Processo> testResuts = testRunner.run(testData, true, 0, 20,20, false);

        //para cada processo nos resultados, verificar se o valor esperado é igual ao valor obtido no  processo
        for(Processo processo : testResuts) {
            Assertions.assertEquals(20, processo.getAcc());
        }
    }

    @Test
    public void teste_roda_1_programa_com_loop_soma_de_1_a_5_RR(){
        TestRunner testRunner = new TestRunner();
        ArrayList<Processo> testData = new ArrayList<Processo>();

        testData.add(new Processo(
                "src/test/java/testFiles/soma_de_1_a_5.txt",
                1,1,0,0,4));

        ArrayList<Processo> testResuts = testRunner.run(testData, true, 1, 20,20, false);

        //para cada processo nos resultados, verificar se o valor esperado é igual ao valor obtido no  processo
        for(Processo processo : testResuts) {
            Assertions.assertEquals(15, processo.getAcc());
        }
    }

    @Test
    public void teste_roda_1_programa_com_loop_soma_de_1_a_5_SJF(){
        TestRunner testRunner = new TestRunner();
        ArrayList<Processo> testData = new ArrayList<Processo>();

        testData.add(new Processo(
                "src/test/java/testFiles/soma_de_1_a_5.txt",
                1,1,0,0,4));

        ArrayList<Processo> testResuts = testRunner.run(testData, true, 0, 20,20, false);

        //para cada processo nos resultados, verificar se o valor esperado é igual ao valor obtido no  processo
        for(Processo processo : testResuts) {
            Assertions.assertEquals(15, processo.getAcc());
        }
    }

    @Test
    public void teste_roda_1_programa_com_loop_soma_de_1_a_n_RR(){
        TestRunner testRunner = new TestRunner();
        ArrayList<Processo> testData = new ArrayList<Processo>();

        testData.add(new Processo(
                "src/test/java/testFiles/soma_de_1_a_n_ver2.txt",
                1,1,0,0,4));

        String input = "5";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ArrayList<Processo> testResuts = testRunner.run(testData, true, 1, 20,20, false);

        //para cada processo nos resultados, verificar se o valor esperado é igual ao valor obtido no  processo
        for(Processo processo : testResuts) {
            Assertions.assertEquals(15, processo.getAcc());
        }
    }

    @Test
    public void teste_roda_1_programa_com_loop_soma_de_1_a_n_SJF(){
        TestRunner testRunner = new TestRunner();
        ArrayList<Processo> testData = new ArrayList<Processo>();

        testData.add(new Processo(
                "src/test/java/testFiles/soma_de_1_a_n_ver2.txt",
                1,1,0,0,4));

        String input = "5";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ArrayList<Processo> testResuts = testRunner.run(testData, true, 0, 20,20, false);

        //para cada processo nos resultados, verificar se o valor esperado é igual ao valor obtido no  processo
        for(Processo processo : testResuts) {
            Assertions.assertEquals(15, processo.getAcc());
        }
    }



}
