package playground;

import parspice.ParSPICE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import spice.basic.CSPICE;

public class Main {
    private static final int numIterations = 10000000;
    private static final int numWorkers = 6;
    private static ParSPICE par;

    private static final double[][] mat = new double[][]{{1, 2, 3}, {-1, 2, 5}, {-4, 0, 3}};

    public static void main(String[] args) throws Exception {
//        System.out.println("\nRunning with " + iterations + " iterations\n");
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


//        long start = System.currentTimeMillis();
//        List<double[]> r1 = par.run(new SpkezrWorker(), numIterations, numWorkers);
//        System.out.println("Spkezr ParSPICE: " + (System.currentTimeMillis() - start));
//
//        start = System.currentTimeMillis();
//        List<double[]> r2 = directSpkezr();
//        System.out.println("Spkezr direct: " + (System.currentTimeMillis() - start));

//        long start = System.currentTimeMillis();
//        List<double[]> r1 = par.run(new SincptWorker(), numIterations, numWorkers);
//        System.out.println("Sincpt ParSPICE: " + (System.currentTimeMillis() - start));
//
//        start = System.currentTimeMillis();
//        List<double[]> r2 = directSincpt();
//        System.out.println("Sincpt direct: " + (System.currentTimeMillis() - start));
//
//        start = System.currentTimeMillis();
//        List<double[]> r3 = threadSincpt();
//        System.out.println("Sincpt threaded: " + (System.currentTimeMillis() - start));

        List<Integer> badIndices = new ArrayList<Integer>();
        for (int i = 0; i < numIterations; i++) {
            boolean fail = false;
            for (int j = 0; j < 3; j++) {
                if (r1.get(i)[j] != r2.get(i)[j] || r1.get(i)[j] != r3.get(i)[j]) {
                    fail = true;
                    break;
                }
            }
            if (fail) {
                badIndices.add(i);
                System.out.println("\nr1: " + Arrays.toString(r1.get(i)));
                System.out.println("r2: " + Arrays.toString(r2.get(i)));
                System.out.println("r3: " + Arrays.toString(r3.get(i)));
            }
        }
        System.out.println(badIndices);
    }


    /*
    Run the task with inputs and outputs phase.
     */
    public static List<double[]> withInput() throws Exception {
        List<double[]> inputs = new ArrayList<>(numIterations);
        for (int i = 0; i < numIterations; i++) {
            inputs.add(new double[]{1, 2, i});
        }
        return par.run(
                new MyIOWorker(),
                inputs,
                numWorkers
        );
    }

    /*
    Run the task with no inputs phase.
     */
    public static List<double[]> noInput() throws Exception {
        return par.run(
                new MyOWorker(),
                numIterations,
                numWorkers
        );
    }

    /*
    Run the task directly through JNISpice.
     */
    public static List<double[]> direct() throws Exception {
        System.loadLibrary("JNISpice");
        List<double[]> results = new ArrayList<>(numIterations);
        for (int i = 0; i < numIterations; i++) {
            results.add(CSPICE.mxv(mat, CSPICE.vhat(new double[]{1, 2, i})));
        }
        return results;
    }

    /*
    Run the task directly through JNISpice.
     */
    public static List<double[]> directSpkezr() throws Exception {
        System.loadLibrary("JNISpice");
        CSPICE.furnsh("scvel.tm");
        List<double[]> results = new ArrayList<>(numIterations);

        String utc = "2004-06-11T19:32:00";

        for (int i = 0; i < numIterations; i++) {
            double et = CSPICE.str2et(utc) + i/2.;

            double[] state = new double[6];
            double[] ltime = new double[1];
            CSPICE.spkezr("CASSINI", et, "ECLIPJ2000", "NONE", "SUN", state, ltime);
            results.add(state);
        }
        return results;
    }

    /*
    Run the task directly through JNISpice.
     */
    public static List<double[]> directSincpt() throws Exception {
        System.loadLibrary("JNISpice");
        CSPICE.furnsh("fovint.tm");

        List<double[]> results = new ArrayList<>(numIterations);
        String[] shape = new String[1];
        String[] insfrm = new String[1];
        double[] bsight = new double[3];
        int[] n = new int[1];
        double[] bounds = new double[12];
        double[] trgepc = new double[1];
        double[] srfvec = new double[3];
        boolean[] found = new boolean[1];

        for (int i = 0; i < numIterations; i++) {
            double et = CSPICE.str2et("2004 jun 11 19:32:00")- i/200.;

            double[] point = new double[3];
            int nacid = CSPICE.bodn2c("CASSINI_ISS_NAC");
            CSPICE.getfov(nacid, shape, insfrm, bsight, n, bounds);
            CSPICE.sincpt(
                    "Ellipsoid", "PHOEBE", et, "IAU_PHOEBE",
                    "LT+S", "CASSINI", insfrm[0], bounds,
                    point, trgepc, srfvec, found
            );
            results.add(point);
        }
        return results;
    }

    public static List<double[]> threadSincpt() throws Exception {
        List<double[]> results = new ArrayList<>(numIterations);
        int batchSize = numIterations/numWorkers;

        SincptThread[] tasks = new SincptThread[numWorkers];
        for (int i = 0; i < numWorkers; i++) {
            tasks[i] = new SincptThread(i, batchSize);
            tasks[i].start();
        }
        for (int i = 0; i < numWorkers; i++) {
            tasks[i].join();
            results.addAll(tasks[i].getOutputs());
        }
        return results;
    }
}
