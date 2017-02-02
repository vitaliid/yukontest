package yukon.service;

import yukon.Caller;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vitalii on 02/02/2017.
 */
public class ServiceWorker implements Runnable {
    private final ServiceDTO service;
    private final SocketAddress address;

    public ServiceWorker(ServiceDTO service) {
        this.service = service;
        this.address = new InetSocketAddress(service.getAddress(), service.getPort());
    }

    private final List<Caller> callers = new ArrayList<>();
    private boolean running = true;

    @Override
    public void run() {
        Socket clientSocket = new Socket();

        while (running) {
            if (!getCallers().isEmpty()) {

                ServiceStatus status = ServiceStatus.DOWN;
                try {
                    clientSocket.connect(address);
                    if (clientSocket.isConnected()) {
                        status = ServiceStatus.UP;
                    }
                } catch (IOException e) {
                    System.out.println("Something wrong with service: " + service + " =" + e.toString());
                } finally {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        clientSocket = new Socket();
                    }
                }

                synchronized (this) {
                    for (Caller caller : getCallers()) {
                        caller.receiveNotification(service, status);
                    }
                    callers.clear();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Worker for " + service + " is finishing.");
    }

    public synchronized List<Caller> getCallers() {
        return callers;
    }


    public ServiceDTO getService() {
        return service;
    }

    public void terminate() {
        running = false;
    }
}
