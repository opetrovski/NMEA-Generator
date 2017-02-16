package generator.nmea;


import java.io.*;
import java.net.Socket;

public class Session extends Thread {

    Socket socket;
    PipedInputStream pis;
    PipedOutputStream posMonitor;

    public Session(Socket socket, PipedInputStream pis, PipedOutputStream posMonitor) {
        this.socket = socket;
        this.pis = pis;
        this.posMonitor = posMonitor;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(pis));
            PrintStream raus = new PrintStream(socket.getOutputStream(), true, "US-ASCII");
            String s;
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }

                while (in.ready()) {
                    s = in.readLine();
                    String msg = "Sending   " + s + "\n";
                    posMonitor.write(msg.getBytes());
                    raus.println(s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null)
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }


}