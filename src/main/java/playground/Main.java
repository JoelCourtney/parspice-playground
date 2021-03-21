package playground;

import parspice.Dispatcher;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Throwable {
        int iterations = 10000;
        long start = System.currentTimeMillis();
        List<double[]> results = Dispatcher.run(
                "/Users/joel/repos/parspice-playground/build/libs/vhatWorker.jar",
                new MyReturner(), iterations, 6
        );
        System.out.println("ParSPICE: " + (System.currentTimeMillis() - start));
    }
}
