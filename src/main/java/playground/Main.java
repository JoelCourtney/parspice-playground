package playground;

import parspice.ParSPICE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import spice.basic.CSPICE;

public class Main {
    private static final int iterations = 10000000;
    private static ParSPICE par;

    private static final double[][] mat = new double[][]{{1, 2, 3}, {-1, 2, 5}, {-4, 0, 3}};

    /*
    Runs the same task three times:

    1. 6 workers, just outputs in ParSPICE
    2. 6 workers, both inputs and outputs in ParSPICE
    3. Single-threaded through direct JNISpice

    The task is to call vhat `iterations` times with the input `new double[]{1, 2, i}`
    where i increments up from 0 to `iterations`.

    The timing results are printed to stdout.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("\nRunning with " + iterations + " iterations\n");
        par = new ParSPICE("build/libs/worker-1.0-SNAPSHOT.jar", 50050);

        long start = System.currentTimeMillis();
        List<double[]> r1 = noInput();
        System.out.println("Output only ParSPICE: " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        List<double[]> r2 = withInput();
        System.out.println("Input + Output ParSPICE: " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        List<double[]> r3 = direct();
        System.out.println("Direct JNISpice: " + (System.currentTimeMillis() - start));


        for (int i = 0; i < iterations; i++) {
            for (int j = 0; j < 3; j++) {
                if (r1.get(i)[j] != r2.get(i)[j] || r1.get(i)[j] != r3.get(i)[j]) {
                    System.out.println("\nr1: " + Arrays.toString(r1.get(i)));
                    System.out.println("r2: " + Arrays.toString(r2.get(i)));
                    System.out.println("r3: " + Arrays.toString(r3.get(i)));
                }
            }
        }
    }

    /*
    Run the task with no inputs phase.
     */
    public static List<double[]> withInput() throws Exception {
        List<double[]> inputs = new ArrayList<>(iterations);
        for (int i = 0; i < iterations; i++) {
            inputs.add(new double[]{1, 2, i});
        }
        return par.run(
                new MyIOWorker(),
                inputs,
                6
        );
    }

    /*
    Run the task with inputs and outputs phase.
     */
    public static List<double[]> noInput() throws Exception {
        return par.run(
                new MyOWorker(),
                iterations,
                6
        );
    }

    /*
    Run the task directly through JNISpice.
     */
    public static List<double[]> direct() throws Exception {
        System.loadLibrary("JNISpice");
        List<double[]> results = new ArrayList<>(iterations);
        for (int i = 0; i < iterations; i++) {
            results.add(CSPICE.mxv(mat, CSPICE.vhat(new double[]{1, 2, i})));
        }
        return results;
    }
}
