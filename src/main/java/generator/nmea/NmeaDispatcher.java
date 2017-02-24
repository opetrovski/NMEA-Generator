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
        portHasChanged = true;
    }

    public ServerSocket getSocket() {
        return server;
    }

    public void setMonitorPipe(PipedOutputStream pos){
        posMonitor = pos;
    }

    public void run() {
            while (true) {

                if( createSocket() ){
                    waitForClientConnections();
                }
                else{
                    // wait for reset of port
                    portHasChanged = false;
                    while (!portHasChanged) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                    }
                }

            }
    }

    private void waitForClientConnections() {
        try {
            while (!server.isClosed()) {
                portHasChanged = false;
                String msg = "Waiting for client connections on port " + port + "\n";
                posMonitor.write(msg.getBytes());
                Socket socket = null;
                try {
                    socket = server.accept();
                }
                catch(Exception e){
                    // ignore
                }
                if( !server.isClosed() ) {
                    msg = "New client connection accepted from " + socket.getRemoteSocketAddress().toString() + "\n";
                    posMonitor.write(msg.getBytes());
                    Session s = new Session(socket, pis, posMonitor);
                    s.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean createSocket(){
    try {
        String msg = "Bind socket to port " + port + "\n";
        posMonitor.write(msg.getBytes());
        server = new ServerSocket(port);
    } catch (IOException e) {
        String msg = "Create Socket failed on port " + port + ". "+e.getMessage()+"\n";
        e.printStackTrace();
        try {
            posMonitor.write(msg.getBytes());
        } catch (IOException e2) {
        }
        return false;
    }
    return true;
}

}