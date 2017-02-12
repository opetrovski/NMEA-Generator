package generator.nmea;


abstract public class NmeaEmitter extends Thread {

    /**
     * Calculate checksums for NMEA 0183 sentences. basically what you do
     * is to XOR every byte starting from the second (the one after the "$")
     * So - take the second byte, XOR with third, then XOR the result with fourth
     * and so on until done. Return the two-digit hex value of the checksum.
     */
    protected String calculateChecksum(String data) {
        byte[] array = data.getBytes();
        byte cksum = array[1];
        for (int i = 2; i < data.length(); i++) {
            int one = (int) cksum;
            int two = (int) array[i];
            int xor = one ^ two;
            cksum = (byte) (0xff & xor);
        }
        return String.format("%02X ", cksum).trim();
    }

    /**
     * Convert longitude from decimal degrees to the format expected in NMWEA 0183 sentences.
     */
    protected String toNmeaLongitudeFormat(double longitude) {
        double d1 = Math.abs(longitude);
        double d2 = d1 % 1;
        int d3 = (int) Math.round(d2 * 60);
        String minutes = String.format("%02d", d3);

        double d4 = Math.abs(d2 * 60);
        double d5 = d4 % 1;
        String d6 = String.format("%.4f", d5);
        String s = d6.substring(d6.indexOf(".") + 1, d6.length());

        int d = (int) Math.round(Math.abs(longitude));
        String degree = String.format("%03d", d);
        return degree + minutes + ".0000";
    }

    /**
     * Convert latitude from decimal degrees to the format expected in NMWEA 0183 sentences.
     */
    protected String toNmeaLatitudeFormat(double longitude) {
        double d1 = Math.abs(longitude);
        double d2 = d1 % 1;
        int d3 = (int) Math.round(d2 * 60);
        String minutes = String.format("%02d", d3);

        double d4 = Math.abs(d2 * 60);
        double d5 = d4 % 1;
        String d6 = String.format("%.4f", d5);
        String s = d6.substring(d6.indexOf(".") + 1, d6.length());

        int d = (int) Math.round(Math.abs(longitude));
        String degree = String.format("%02d", d);
        return degree + minutes + ".0000";
    }

}
