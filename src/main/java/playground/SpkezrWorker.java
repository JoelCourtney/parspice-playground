package playground;

import parspice.sender.DoubleArraySender;
import parspice.worker.OWorker;
import spice.basic.CSPICE;
import spice.basic.SpiceErrorException;

public class SpkezrWorker extends OWorker<double[]> {

    double[] state = new double[6];
    double[] ltime = new double[1];

    String utc = "2004-06-11T19:32:00";

    public SpkezrWorker() {
        super(new DoubleArraySender(6));
    }

    @Override
    public void setup() throws SpiceErrorException {
        System.loadLibrary("JNISpice");
        CSPICE.furnsh("scvel.tm");
    }

    @Override
    public double[] task(int i) throws SpiceErrorException {
        double et = CSPICE.str2et(utc) + i/2.;

        CSPICE.spkezr("CASSINI", et, "ECLIPJ2000", "NONE", "SUN", state, ltime);
        return state;
    }
}
