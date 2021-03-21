package playground;

import parspice.ParSPICE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import parspice.sender.DoubleArraySender;
import spice.basic.CSPICE;

public class Main {
    private static final int iterations = 10000000;

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        List<double[]> r1 = noInput();
        System.out.println("NoInput ParSPICE: " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        List<double[]> r2 = withInput();
        System.out.println("Input ParSPICE: " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        System.loadLibrary("JNISpice");
        List<double[]> r3 = new ArrayList<>(iterations);
        for (int i = 0; i < iterations; i++) {
            r3.add(CSPICE.vhat(new double[]{1, 2, i}));
        }
        System.out.println("Direct JNISpice: " + (System.currentTimeMillis() - start));


        for (int i = 0; i < iterations; i++) {
            for (int j = 0; j < 3; j++) {
                if (r1.get(i)[j] != r2.get(i)[j] || r1.get(i)[j] != r3.get(i)[j]) {
                    System.out.println(Arrays.toString(r1.get(i)));
                    System.out.println(Arrays.toString(r2.get(i)));
                    System.out.println(Arrays.toString(r3.get(i)));
                }
            }
        }
    }

    public static List<double[]> withInput() throws Exception {
        List<double[]> inputs = new ArrayList<>(iterations);
        for (int i = 0; i < iterations; i++) {
            inputs.add(new double[]{1, 2, i});
        }
        ParSPICE<double[], double[]> par2 = new ParSPICE<>(
                "build/libs/worker-1.0-SNAPSHOT.jar",
                "playground.MySecondWorker",
                new DoubleArraySender(3),
                new DoubleArraySender(3)
        );
        return par2.run(inputs, 6);
    }

    public static List<double[]> noInput() throws Exception {
        ParSPICE<Void, double[]> par = new ParSPICE<>(
                "build/libs/worker-1.0-SNAPSHOT.jar",
                "playground.MyWorker",
                new DoubleArraySender(3)
        );
        return par.run(iterations, 6);
    }
}
