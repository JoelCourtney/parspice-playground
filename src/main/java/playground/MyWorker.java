package playground;

import parspice.sender.DoubleArraySender;
import spice.basic.CSPICE;
import spice.basic.SpiceErrorException;
import parspice.worker.NoInputWorker;

public class MyWorker extends NoInputWorker<double[]> {
    public static void main(String[] args) throws Exception {
        new MyWorker().run(
                new DoubleArraySender(3),
                args
        );
    }

    @Override
    public void setup() {
        System.loadLibrary("JNISpice");
    }

    @Override
    public double[] task(int i) throws SpiceErrorException {
        return CSPICE.vhat(new double[]{1, 2, i});
    }
}