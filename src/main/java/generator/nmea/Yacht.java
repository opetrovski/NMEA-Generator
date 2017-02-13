package generator.nmea;


import generator.compass.DeviationTable;
import generator.compass.DeclinationTable2013;

public class Yacht extends Thread {
    int updateInterval = 1; // seconds
    double hullSpeed = 0.5;
    double speed = 0.0;
    double heading = 0.0;
    double heading_magnetic;
    double heading_true;
    double heading_compass;
    double compass_declination;
    double compass_deviation;
    int throttleposition = 9000;
    double latitude = 55.0;
    double longitude = 6.65;
    int rudderposition = 0;
    static final double EARTH_RADIUS = 3443.89849; // earth radius in nautical miles

    public Yacht() {
    }

    public void decRudderPosition(){ rudderposition--; }
    public void incRudderPosition(){ rudderposition++; }
    public void setSpeedInKnots(int speed){ this.speed = speed; }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getHeading_magnetic() {
        return heading_magnetic;
    }

    public double getHeading_true() {
        return heading_true;
    }

    public double getHeading_compass() {
        return heading_compass;
    }

    public double getCompass_declination() {
        return compass_declination;
    }

    public double getCompass_deviation() {
        return compass_deviation;
    }

    public double getSpeed() {
        return speed;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(updateInterval * 1000);
            } catch (InterruptedException e) {
            }
            calcLongLat();
        }
    }

    private void calcLongLat() {
        if (throttleposition >= 0) {
            speed = hullSpeed * throttleposition / 100;
        }

        // Turn according to rudder setting
        if (rudderposition != 0) {
            heading = heading + rudderposition * 0.05;
            if (heading < -180) {
                heading = heading + 360;
            }
            if (heading > 180) {
                heading = heading - 360;
            }
        }

        heading_true = heading;
        if (heading_true < 0) {
            heading_true = heading_true + 360;
        }
        if (heading_true >= 360) {
            heading_true = heading_true - 360;
        }

        compass_declination = DeclinationTable2013.getDeclination(latitude, longitude);
        heading_magnetic = heading_true - compass_declination;
        if (heading_magnetic < 0) {
            heading_magnetic = heading_magnetic + 360;
        }
        if (heading_magnetic >= 360) {
            heading_magnetic = heading_magnetic - 360;
        }

        compass_deviation = DeviationTable.getDeviation(heading_magnetic);
        heading_compass = heading_magnetic - compass_deviation;
        if (heading_compass < 0) {
            heading_compass = heading_compass + 360;
        }
        if (heading_compass >= 360) {
            heading_compass = heading_compass - 360;
        }
        //
        // Displacement in nautical miles (speed is in knots)
        // interval is in seconds so we divide by 3600 to get hours
        // knot = 1nm/hr = 1/3600nm/sec
        //
        double displ = speed * updateInterval / 3600;
        //
        // To find the lat/lon of a point on true course t, distance d from (p1,l1) all in RADIANS
        // along a rhumbline (initial point cannot be a pole!):
        //
        // This calculation assumes a spherical earth and is quite accurate for most practical purposes
        //
        // Formula:  φ2 = asin( sin(φ1)*cos(d/R) + cos(φ1)*sin(d/R)*cos(θ) )
        //   λ2 = λ1 + atan2( sin(θ)*sin(d/R)*cos(φ1), cos(d/R)−sin(φ1)*sin(φ2) )
        // where   φ is latitude (in radians)
        //         λ is longitude (in radians)
        //         θ is the bearing (in radians, clockwise from north)
        //         d is the distance travelled (say, nautical miles)
        //         R is the earth’s radius in same units as d (say, 3443.89849 nautical miles)
        //           (d/R is the angular distance, in radians)
        //
        // We need everything in Radians first (this is real Math baby)
        //
        double p1 = latitude * Math.PI / 180;
        double l1 = longitude * Math.PI / 180;
        double t = heading_true * Math.PI / 180;
        double p2 = Math.asin(Math.sin(p1) * Math.cos(displ / EARTH_RADIUS) + Math.cos(p1) * Math.sin(displ / EARTH_RADIUS) * Math.cos(t));
        double l2 = l1 + Math.atan2(Math.sin(t) * Math.sin(displ / EARTH_RADIUS) * Math.cos(p1), Math.cos(displ / EARTH_RADIUS) - Math.sin(p1) * Math.sin(p2));
        //
        // Sanitize longitude into the range -pi < l2 < pi (i.e between -180 and 180 degrees)
        //
        if (l2 > Math.PI) {
            l2 = l2 - (2 * Math.PI);
        }
        if (l2 < (-Math.PI)) {
            l2 = (2 * Math.PI) + l2;
        }
        // Now convert lat, long back into degrees
        latitude = p2 * 180 / Math.PI;
        longitude = l2 * 180 / Math.PI;
    }

}