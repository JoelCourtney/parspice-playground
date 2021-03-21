package playground;

import parspice.ParSPICE;
import java.util.List;
import parspice.sender.DoubleArraySender;

public class Main {
    public static void main(String[] args) throws Exception {
        int iterations = 10000000;
        long start = System.currentTimeMillis();
        List<double[]> results = ParSPICE.run(
                "build/libs/worker-1.0-SNAPSHOT.jar",
                "playground.MyWorker",
                new DoubleArraySender(3), iterations, 6
        );
        System.out.println("ParSPICE: " + (System.currentTimeMillis() - start));
    }
}
