package playground;

import parspice.sender.DoubleArraySender;
import parspice.sender.Sender;
import parspice.worker.OWorker;
import spice.basic.CSPICE;
import spice.basic.SpiceErrorException;

public class SpkezrWorker implements OWorker<double[]> {

    public static void main(String[] args) throws Exception {
        OWorker.run(new SpkezrWorker(), args);
    }

    @Override
    public Sender<double[]> getOutputSender() {
        return new DoubleArraySender(6);
    }

    @Override
    public void setup() {
        System.loadLibrary("JNISpice");
        try {
            CSPICE.furnsh("scvel.tm");
        } catch (SpiceErrorException e) {
            e.printStackTrace();
        }
    }

    @Override
    public double[] task(int i) throws SpiceErrorException {
        double[] state = new double[6];
        double[] ltime = new double[1];
        String utc = "2004-06-11T19:32:00";
        double et = CSPICE.str2et(utc) + i/2.;

        CSPICE.spkezr("CASSINI", et, "ECLIPJ2000", "NONE", "SUN", state, ltime);
        return state;
    }
}
