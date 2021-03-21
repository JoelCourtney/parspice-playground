package playground;

import parspice.sender.DoubleArraySender;
import parspice.sender.Sender;
import parspice.worker.InputOutputWorker;
import spice.basic.CSPICE;
import spice.basic.SpiceErrorException;

public class MySecondWorker extends InputOutputWorker<double[], double[]> {
    public static void main(String[] args) throws Exception {
        new MySecondWorker().run(args);
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
        return CSPICE.vhat(in);
    }
}