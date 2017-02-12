package generator.nmea;


import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GPS extends NmeaEmitter {
    int updateInterval = 2;
    boolean enabled = false;
    Yacht yacht;
    PipedOutputStream pos;

    public GPS(Yacht yacht, PipedOutputStream pos) {
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

                String latitude = toNmeaLatitudeFormat(yacht.getLatitude());
                String north_south = "S";
                if (yacht.getLatitude() > 0) {
                    north_south = "N";
                }

                String longitude = toNmeaLongitudeFormat(yacht.getLongitude());
                String east_west = "W";
                if (yacht.getLongitude() > 0) {
                    east_west = "E";
                }

                DateFormat utc_time_fmt = new SimpleDateFormat("hhmmss");
                DateFormat utc_date_fmt = new SimpleDateFormat("ddMMyy");
                String utc_time = utc_time_fmt.format(new Date());
                String utc_date = utc_date_fmt.format(new Date());
                String data_valid = "A";
                String heading_magnetic = String.format(Locale.US, "%.1f", yacht.getHeading_magnetic());
                String heading_true = String.format(Locale.US, "%.1f", yacht.getHeading_true());
                // Simulation speed is knots. Convert to km/h
                double s_kmh = yacht.getSpeed() * 1.852;
                String speed_kmh = String.format(Locale.US, "%.1f", s_kmh);

                String gpgll = "$GPGLL," + latitude + "," + north_south + "," + longitude + "," + east_west + "," + utc_time + "," + data_valid;
                String gpgga = "$GPGGA," + utc_time + "," + latitude + "," + north_south + "," + longitude + "," + east_west + ",1,12,1.5,0.0,M,0.0,M,";
                String cksum = calculateChecksum(gpgga);
                gpgga = gpgga + "*" + cksum;

                String gpvtd = "$GPVTD," + heading_true + ",T," + heading_magnetic + ",M," + yacht.getSpeed() + ",N," + speed_kmh + ",K";
                cksum = calculateChecksum(gpvtd);
                gpvtd = gpvtd + "*" + cksum;

                String gprmc = "$GPRMC," + utc_time + "," + data_valid + "," + latitude + "," + north_south + "," + longitude + "," + east_west + "," + yacht.getSpeed() + "," + heading_true + "," + utc_date + ",0,E";
                cksum = calculateChecksum(gprmc);
                gprmc = gprmc + "*" + cksum;

                if (enabled) {
                    ps.println(gpgll);
                    ps.println(gpgga);
                    ps.println(gpvtd);
                    ps.println(gprmc);
                    //
                    // Dump out some fake satellites in view sentences
                    // Satellites 16, 18, 22 and 24 will appear to be OK (non-zero dB SNR)
                    // The rest (03, 04, 06, 13,14, 19, 27) will not (dB SNR value is zero)
                    //
                    ps.println("$GPGSV,3,1,11,03,03,111,00,04,15,270,00,06,01,010,00,13,06,292,00*74");
                    ps.println("$GPGSV,3,2,11,14,25,170,00,16,57,208,39,18,67,296,40,19,40,246,00*74");
                    ps.println("$GPGSV,3,3,11,22,42,067,42,24,14,311,43,27,05,244,00,,,,*4D");
                }
            }
        } catch (Exception e) {
            System.out.println("IOProbleme");
            e.printStackTrace();
        }
    }
}