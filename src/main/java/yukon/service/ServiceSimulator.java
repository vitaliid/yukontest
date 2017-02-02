package yukon.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by vitalii on 02/02/2017.
 */
public class ServiceSimulator implements Runnable {

    final int port;

    public ServiceSimulator(int port) {
        this.port = port;
    }


    @Override
    public void run() {
        try {
            ServerSocket welcomeSocket = new ServerSocket(port);
            while (true) {
                Socket connectionSocket = welcomeSocket.accept();
                System.out.println("Me: " + port + " new connection from: " + connectionSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        for (int i = 1234; i < 1246; i++) {
            ServiceSimulator serviceSimulator = new ServiceSimulator(i);
            new Thread(serviceSimulator).start();
        }
    }
}
