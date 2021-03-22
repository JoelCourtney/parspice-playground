package playground;

import parspice.sender.DoubleArraySender;
import parspice.sender.Sender;
import spice.basic.CSPICE;
import spice.basic.SpiceErrorException;
import parspice.worker.OWorker;

public class MyOWorker implements OWorker<double[]> {

    private static final double[][] mat = new double[][]{{1, 2, 3}, {-1, 2, 5}, {-4, 0, 3}};

    public static void main(String[] args) throws Exception {
        OWorker.run(new MyOWorker(), args);
    }

    @Override
    public Sender<double[]> getOutputSender() {
        return new DoubleArraySender(3);
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