import parspice.ParSPICE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import spice.basic.CSPICE;

public class Main {
    public static void main(String[] args) throws Throwable {
        ParSPICE par = new ParSPICE(
                "/Users/joel/repos/parspice/build/libs/parspice-1.0-SNAPSHOT-worker.jar",
                50050,
                3,
                50000
        );
        par.start();
        long start = System.currentTimeMillis();


        var vhat = par.vhat();
        for (int i = 0; i < 10000000; i++) {
            vhat.call(new Double[]{1., 2. + i / 10., 3.});
        }
        vhat.run();

        long end = System.currentTimeMillis();
        System.out.println("parspice: " + (end - start) + "ms");

        par.stop();

        start = System.currentTimeMillis();
        ArrayList<double[]> results = new ArrayList<double[]>();
        for (int i = 0; i < 10000000; i++) {
            results.add(CSPICE.vhat(new double[]{1., 2. + i/10., 3.}));
        }
        end = System.currentTimeMillis();
        System.out.println("direct: " + (end - start) + "ms");
    }
}
