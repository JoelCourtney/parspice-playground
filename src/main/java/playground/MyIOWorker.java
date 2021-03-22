package playground;

import parspice.sender.DoubleArraySender;
import parspice.sender.Sender;
import parspice.worker.IOWorker;
import spice.basic.CSPICE;
import spice.basic.SpiceErrorException;

public class MyIOWorker implements IOWorker<double[], double[]> {
    private static final double[][] mat = new double[][]{{1, 2, 3}, {-1, 2, 5}, {-4, 0, 3}};

    public static void main(String[] args) throws Exception {
        IOWorker.run(new MyIOWorker(), args);
    }

    @Override
    public Sender<double[]> getInputSender() {
        return new DoubleArraySender(3);
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
    public double[] task(double[] in) throws SpiceErrorException {
        return CSPICE.mxv(mat, CSPICE.vhat(in));
    }
}