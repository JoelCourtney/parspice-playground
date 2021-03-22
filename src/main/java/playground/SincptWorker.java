package playground;

import parspice.sender.DoubleArraySender;
import parspice.sender.Sender;
import parspice.worker.OWorker;
import spice.basic.CSPICE;
import spice.basic.IDCodeNotFoundException;
import spice.basic.SpiceErrorException;

import java.io.IOException;

public class SincptWorker implements OWorker<double[]> {

    String[] shape = new String[1];
    String[] insfrm = new String[1];
    double[] bsight = new double[3];
    int[] n = new int[1];
    double[] bounds = new double[12];
    double[] point = new double[3];
    double[] trgepc = new double[1];
    double[] srfvec = new double[3];
    boolean[] found = new boolean[1];
    int nacid;
    double et;

    public static void main(String[] args) throws IOException {
        OWorker.run(new SincptWorker(), args);
    }
    @Override
    public Sender<double[]> getOutputSender() {
        return new DoubleArraySender(3);
    }

    @Override
    public void setup() throws SpiceErrorException, IDCodeNotFoundException {
        System.loadLibrary("JNISpice");
        CSPICE.furnsh("fovint.tm");
        nacid = CSPICE.bodn2c("CASSINI_ISS_NAC");
    }

    @Override
    public double[] task(int i) throws Exception {
        et = CSPICE.str2et("2004 jun 11 19:32:00") - i /200.;
        CSPICE.getfov(nacid, shape, insfrm, bsight, n, bounds);
        CSPICE.sincpt(
                "Ellipsoid", "PHOEBE", et, "IAU_PHOEBE",
                "LT+S", "CASSINI", insfrm[0], bounds,
                point, trgepc, srfvec, found
        );
        return point;
    }
}
