package yukon;

import yukon.service.ServiceDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by vitalii on 01/02/2017.
 */
public class DemoCaller implements Caller {

    private final static int POLLING_FREQUENCY = 10000;

    public static void main(String[] args) {
        Monitor monitor = new Monitor();

        List<Caller> callers = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            callers.add(new DemoCaller());
        }

        for (int i = 1234; i < 1248; i++) {
            for (Caller caller : callers) {
                monitor.register(caller, new ServiceDTO("localhost", i), POLLING_FREQUENCY, POLLING_FREQUENCY);
            }
        }

        Timer timer = new Timer();
        TimerTask finishTask = new TimerTask() {
            @Override
            public void run() {
                for (int i = 1234; i < 1248; i++) {
                    for (Caller caller : callers) {
                        monitor.unregister(caller, new ServiceDTO("localhost", i));
                    }
                }
                System.out.println("There should be end.");
                monitor.terminate();
                timer.cancel();
            }
        };


        timer.schedule(finishTask, POLLING_FREQUENCY * 3);
    }
}
