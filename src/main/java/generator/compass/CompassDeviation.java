package generator.compass;

public class CompassDeviation {

    static final double[][] deviation_table = {
            {0, -3},
            {10, -1.5},
            {20, 0},
            {30, 2},
            {40, 4},
            {50, 6},
            {60, 8},
            {70, 10},
            {80, 12},
            {90, 14},
            {100, 12},
            {110, 11},
            {120, 10},
            {130, 8},
            {140, 7},
            {150, 6},
            {160, 5},
            {170, 4},
            {180, 3},
            {190, 1.5},
            {200, 0},
            {210, -2},
            {220, -4},
            {230, -6},
            {240, -8},
            {250, -10},
            {260, -12},
            {270, -14},
            {280, -12},
            {290, -11},
            {300, -10},
            {310, -8},
            {320, -7},
            {330, -6},
            {340, -5},
            {350, -4},
            {360, -3}
    };


    public static double getDeviation(double hdg) {
        double hdg_srch = Math.floor(hdg);
        for (double[] d : deviation_table) {
            if (d[0] == hdg_srch) {
                return d[1];
            }
        }
        return 1.0;
    }
}
