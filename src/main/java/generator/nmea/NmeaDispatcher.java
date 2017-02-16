package generator.nmea;

import javax.swing.*;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class NmeaDispatcher extends Thread {

    private ServerSocket server;
    PipedInputStream pis;
    PipedOutputStream posMonitor = null;
    int port;
    boolean portHasChanged = false;

    public NmeaDispatcher(PipedInputStream pis) {
        this.pis = pis;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void reset(int port){
        this.port = port;
        portHasChanged = true;
    }

    public void setMonitorPipe(PipedOutputStream pos){
        posMonitor = pos;
    }

    public void run() {
        try {
            while (true) {
                server = new ServerSocket(port);
                while (!portHasChanged) {
                    portHasChanged = false;
                    String msg = "Waiting for client connections on port " + port + "\n";
                    posMonitor.write(msg.getBytes());
                    Socket socket = server.accept();
                    msg = "New client connection accepted from " + socket.getRemoteSocketAddress().toString() + "\n";
                    posMonitor.write(msg.getBytes());
                    Session s = new Session(socket, pis, posMonitor);
                    s.start();
                }
                server.close();
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