package yukon;

import yukon.service.ServiceDTO;
import yukon.service.ServiceWorker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by vitalii on 01/02/2017.
 */
public class Monitor {

    private final Map<ServiceDTO, ServiceWorker> activeServices = new HashMap<>();

    private final Map<ServiceDTO, List<ScheduledTask>> activeSchedulers = new HashMap<>();

    private final Timer timer = new Timer();

    /**
     * @param caller
     * @param service          host and port of service
     * @param pollingFrequency milliseconds
     * @param graceTime        milliseconds
     */
    public void register(Caller caller, ServiceDTO service, int pollingFrequency, int graceTime) {
        pollingFrequency = Math.min(pollingFrequency, graceTime);

        ServiceWorker worker = activeServices.get(service);
        if (worker == null) {
            worker = new ServiceWorker(service);
            activeServices.put(service, worker);
            activeSchedulers.put(service, new ArrayList<>());
            new Thread(worker).start();
        }

        if (!worker.getCallers().contains(caller)) {
            ScheduledTask scheduledTask = new ScheduledTask(service, worker, caller);
            activeSchedulers.get(service).add(scheduledTask);
            timer.schedule(scheduledTask, 0, pollingFrequency);
        }
    }

    public void unregister(Caller caller, ServiceDTO service) {
        List<ScheduledTask> scheduledTasks = activeSchedulers.get(service);

        if (scheduledTasks != null) {
            for (ScheduledTask scheduledTask : new ArrayList<>(scheduledTasks)) {
                if (scheduledTask.getCaller().equals(caller)) {
                    scheduledTask.cancel();
                    scheduledTasks.remove(scheduledTask);
                    break;
                }
            }

            if (scheduledTasks.isEmpty()) {
                activeSchedulers.remove(service);
                activeServices.remove(service).terminate();
            }
        }
    }

    private final static class ScheduledTask extends TimerTask {

        private final ServiceDTO service;
        private final ServiceWorker worker;
        private final Caller caller;

        public ScheduledTask(ServiceDTO service, ServiceWorker worker, Caller caller) {
            this.worker = worker;
            this.caller = caller;
            this.service = service;
        }

        @Override
        public void run() {
            worker.getCallers().add(caller);
        }


        public ServiceWorker getWorker() {
            return worker;
        }

        public Caller getCaller() {
            return caller;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            ScheduledTask that = (ScheduledTask) o;

            if (service != null ? !service.equals(that.service) : that.service != null) {
                return false;
            }
            return caller != null ? caller.equals(that.caller) : that.caller == null;
        }

        @Override
        public int hashCode() {
            int result = service != null ? service.hashCode() : 0;
            result = 31 * result + (caller != null ? caller.hashCode() : 0);
            return result;
        }
    }

    public void terminate() {
        for (ServiceWorker worker : activeServices.values()) {
            worker.terminate();
        }

        for (List<ScheduledTask> scheduledTasks : activeSchedulers.values()) {
            for (ScheduledTask scheduledTask : scheduledTasks) {
                scheduledTask.cancel();
            }
        }
        timer.cancel();
    }
}
