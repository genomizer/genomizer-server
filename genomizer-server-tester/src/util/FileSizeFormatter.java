package util;
/**
 * A class that converts bytes to formatted strings. (B, kB, MB, GB)
 *
 * @author oi12pjn & oi12mlw
 *
 */
public class FileSizeFormatter {

    private static final long kiloByte = 1000L;
    private static final long megaByte = 1000000L;
    private static final long gigaByte = 1000000000L;
    /**
     * Formats given amount of bytes to a string corresponding to the
     * different prefixes. Rounds the number to two decimals
     * @param bytes the bytes
     * @return the bytes formatted to perfection.
     */
    public static String convertByteToString(long bytes) {

        if (bytes >= gigaByte) {
            double gigaBytes = ((double) bytes / gigaByte);
            return roundToTwoDecimals(gigaBytes) + " GB";
        } else if (bytes >= megaByte) {
            double megaBytes = ((double) bytes / megaByte);
            return roundToTwoDecimals(megaBytes) + " MB";
        } else if (bytes >= kiloByte) {
            double kiloBytes = ((double) bytes / kiloByte);
            return roundToTwoDecimals(kiloBytes) + " kB";
        } else {
            return bytes + " B";
        }

    }
    /**
     * Rounds a number to two decimals
     * @param number the number
     * @return the rounded number
     */
    private static double roundToTwoDecimals(double number) {
        return (double) Math.round(number * 100) / 100;
    }

}
