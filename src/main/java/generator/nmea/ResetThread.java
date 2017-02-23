package generator.nmea;


import java.io.IOException;
import java.net.ServerSocket;

public class ResetThread extends Thread{

    ServerSocket socket;

    public ResetThread(ServerSocket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
