package generator.nmea;

import javax.swing.*;
import java.io.IOException;
import java.io.PipedInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class NmeaDispatcher extends Thread {

    private ServerSocket server;
    PipedInputStream pis;
    int port;

    public NmeaDispatcher(PipedInputStream pis) {
        this.pis = pis;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void run() {
        try {
        server = new ServerSocket(port);
        while (true) {
                System.out.println("wait for client connection");
                Socket socket = server.accept();
                System.out.println("new client connection accepted");
                Session s = new Session(socket, pis);
                s.start();
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void log(String message) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //logger.append(message);
            }
        });
    }


}