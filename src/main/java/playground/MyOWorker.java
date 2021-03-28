package playground;

import parspice.sender.DoubleArraySender;
import parspice.sender.Sender;
import spice.basic.CSPICE;
import spice.basic.SpiceErrorException;
import parspice.worker.OWorker;

public class MyOWorker extends OWorker<double[]> {

    private static final double[][] mat = new double[][]{{1, 2, 3}, {-1, 2, 5}, {-4, 0, 3}};

    public MyOWorker() {
        super(new DoubleArraySender(3));
    }

    @Override
    public void setup() {
        System.loadLibrary("JNISpice");
    }

    @Override
    public double[] task(int i) throws SpiceErrorException {
        return CSPICE.mxv(mat, CSPICE.vhat(new double[]{1, 2, i}));
    }
}