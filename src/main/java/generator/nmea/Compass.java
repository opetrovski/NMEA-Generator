package generator.nmea;

import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.util.Locale;

public class Compass extends NmeaEmitter {
    int updateInterval = 2;
    boolean enabled = false;
    Yacht yacht;
    PipedOutputStream pos;

    public Compass(Yacht yacht, PipedOutputStream pos) {
        this.yacht = yacht;
        this.pos = pos;
    }

    public void setInterval(int interval) {
        updateInterval = interval;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public void run() {
        try {
            PrintWriter ps = new PrintWriter(pos, true);

            while (true) {
                try {
                    Thread.sleep(updateInterval * 1000);
                } catch (InterruptedException e) {
                }
                String heading_magnetic = String.format(Locale.US, "%.1f", yacht.getHeading_magnetic());
                String heading_compass = String.format(Locale.US, "%.1f", yacht.getHeading_compass());
                String heading_true = String.format(Locale.US, "%.1f", yacht.getHeading_true());
                String compass_declination = String.format(Locale.US, "%.1f", Math.abs(yacht.getCompass_declination()));
                String compass_deviation = String.format(Locale.US, "%.1f", Math.abs(yacht.getCompass_deviation()));

                String direction = "E";
                if (yacht.compass_declination < 0) {
                    direction = "W";
                }

                String dv_direction = "E";
                if (yacht.compass_deviation < 0) {
                    dv_direction = "W";
                }

                String hehdt = "$HEHDT," + heading_true + ",T";
                String cksum = calculateChecksum(hehdt);
                hehdt = hehdt + "*" + cksum;

                String hchdm = "$HCHDM," + heading_magnetic + ",M";
                cksum = calculateChecksum(hchdm);
                hchdm = hchdm + "*" + cksum;

                String hchdg = "$HEHDG," + heading_magnetic + "," + compass_deviation + "," + dv_direction + "," + compass_declination + "," + direction;
                cksum = calculateChecksum(hchdg);
                hchdg = hchdg + "*" + cksum;

                if (enabled) {
                    ps.println(hehdt);
                    ps.println(hchdm);
                    ps.println(hchdg);
                }
            }
        } catch (Exception e) {
            System.out.println("IOProbleme");
            e.printStackTrace();
        }
    }
}