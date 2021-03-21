package playground;

import parspice.sender.DoubleArraySender;
import parspice.worker.InputWorker;
import spice.basic.CSPICE;
import spice.basic.SpiceErrorException;

public class MySecondWorker extends InputWorker<double[], double[]> {
    public static void main(String[] args) throws Exception {
        new MySecondWorker().run(
                new DoubleArraySender(3),
                new DoubleArraySender(3),
                args
        );
    }

    @Override
    public void setup() {
        System.loadLibrary("JNISpice");
    }

    @Override
    public double[] task(double[] in) throws SpiceErrorException {
        return CSPICE.vhat(in);
    }
}