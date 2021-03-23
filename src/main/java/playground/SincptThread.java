package playground;

import spice.basic.CSPICE;
import spice.basic.IDCodeNotFoundException;
import spice.basic.SpiceErrorException;

import java.util.ArrayList;
import java.util.List;

public class SincptThread implements Runnable {
    private Thread thread;
    private final int threadID;
    private final int batchSize;

    protected final List<double[]> outputs;

    public SincptThread(int threadID, int batchSize) {
        this.threadID = threadID;
        this.batchSize = batchSize;
        outputs = new ArrayList<>(batchSize);
    }

    @Override
    public void run() {
        int startI = threadID * batchSize;
        int endI = startI + batchSize;


        String[] shape = new String[1];
        String[] insfrm = new String[1];
        double[] bsight = new double[3];
        int[] n = new int[1];
        double[] bounds = new double[12];
        double[] trgepc = new double[1];
        double[] srfvec = new double[3];
        boolean[] found = new boolean[1];
        int nacid;
        double et;

        try {
            nacid = CSPICE.bodn2c("CASSINI_ISS_NAC");
            for (int i = startI; i < endI; i++) {
                double[] point = new double[3];
                et = CSPICE.str2et("2004 jun 11 19:32:00") - i /200.;
                CSPICE.getfov(nacid, shape, insfrm, bsight, n, bounds);
                CSPICE.sincpt(
                        "Ellipsoid", "PHOEBE", et, "IAU_PHOEBE",
                        "LT+S", "CASSINI", insfrm[0], bounds,
                        point, trgepc, srfvec, found
                );
                outputs.add(point);
            }
        } catch (SpiceErrorException | IDCodeNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this, "task " + threadID);
            thread.start();
        }
    }

    public void join() throws Exception {
        thread.join();
    }

    public List<double[]> getOutputs() {
        return outputs;
    }
}
