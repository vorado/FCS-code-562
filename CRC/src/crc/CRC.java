package crc;

import java.io.IOException;
import java.util.Arrays;

public class CRC {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        mainForm.main(null);
        int[] kBits = new int[]{0, 0, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0};//LSB on Index 0

        int[] divisor = new int[]{1, 1, 0, 1};

        int[] crc1 = generateCRC(kBits, divisor);
        System.out.println("----------------------------------");
        System.out.println("----------------------------------");
        System.out.println("----------------------------------");

        Boolean output = checkCRC(concat(crc1, kBits), divisor);

        System.out.println("CRC: " + Arrays.toString(crc1));
        System.out.println("Syndrome Check: " + (output ? "Correct" : "Incorrect"));
    }

    /**
     * Thanks AvrDragon at stackoverflow
     *
     * @param str
     * @return
     */
    public static String superscript(String str) {
        str = str.replaceAll("0", "⁰");
        str = str.replaceAll("1", "¹");
        str = str.replaceAll("2", "²");
        str = str.replaceAll("3", "³");
        str = str.replaceAll("4", "⁴");
        str = str.replaceAll("5", "⁵");
        str = str.replaceAll("6", "⁶");
        str = str.replaceAll("7", "⁷");
        str = str.replaceAll("8", "⁸");
        str = str.replaceAll("9", "⁹");
        return str;
    }

    /**
     *
     * @param a
     * @return
     */
    public static String getPolynomialString(int[] a) {
        String str = "";
        if(a.length<1)
            return str;
        for (int i = a.length - 1; i > 0; i--) {
            if (a[i] == 1) {
                str += "x" + superscript(Integer.toString(i));
                str += " + ";
            }

        }
        if (a[0] == 1) {
            str += "1";
        }
        if (str.endsWith(" + ")) {
            str = str.substring(0, str.length() - 3);
        }

        return str;
    }

    public static String getPolynomialString(String a) throws IOException {
        return getPolynomialString(stringToBin(a));
    }

    /**
     *
     * @param a
     * @return
     */
    public static String binToString(int[] a) {
        String str = "";
        for (int i = 0; i < a.length; i++) {
            if (a[i] == 1) {
                str += "1";
            } else {
                str += "0";
            }
        }
        String s;
        s = new StringBuilder(str).reverse().toString();
        return s;
    }

    /**
     * Converts string to array of integers
     *
     * @param s
     * @return int array
     * @throws java.io.IOException
     */
    public static int[] stringToBin(String s) throws IOException {
        int[] b = new int[s.length()];
        String a = new StringBuilder(s).reverse().toString();
        int tmp;
        for (int i = 0; i < a.length(); i++) {
            tmp = Integer.parseInt(String.valueOf(a.charAt(i)));
            if (tmp != 0 && tmp != 1) {   //To validate input
                IOException e = new IOException();
                throw e;
            } else {
                b[i] = tmp;
            }

        }
        return b;
    }

    /**
     *
     * @param payload Payload bits with LSB at index 0
     * @param generator Generator bits with LSB at index 0
     * @return crc bits with LSB at index 0
     */
    public static int[] generateCRC(int[] payload, int[] generator) {
        int n = generator.length - 1;
        int k = payload.length;

        int buf[] = new int[n + k];

        System.arraycopy(payload, 0, buf, n, k);
        System.out.println(Arrays.toString(buf));
        System.out.println("----------------------------------");
        for (int i = k + n - 1; i >= n; i--) {   //From the MSB of the payload to the LSB of the payload.
            if (buf[i] == 1) {    //if current bit is 1 then XOR the block length of n+1 with the generator.
                for (int j = 0; j < generator.length; j++) {
                    buf[i - j] ^= generator[n - j];
                }
                System.out.println(Arrays.toString(buf));
            }
        }
        int crc[] = new int[n];
        System.arraycopy(buf, 0, crc, 0, n);
        return crc;
    }

    /**
     *
     * @param stream
     * @param generator
     * @return
     */
    public static boolean checkCRC(int[] stream, int[] generator) {

        for (int i = stream.length - 1; i >= generator.length - 1; i--) {
            if (stream[i] == 1) {    //if current bit is 1 then XOR the block length of n+1 with the generator.
                for (int j = 0; j < generator.length; j++) {
                    stream[i - j] ^= generator[generator.length - 1 - j];
                }

            }
        }
        for (int i : stream) {
            if (i == 1) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param stream
     * @param generator
     * @return
     */
    public static int[] getSyndrome(int[] stream, int[] generator) {

        for (int i = stream.length - 1; i >= generator.length - 1; i--) {
            if (stream[i] == 1) {    //if current bit is 1 then XOR the block length of n+1 with the generator.
                for (int j = 0; j < generator.length; j++) {
                    stream[i - j] ^= generator[generator.length - 1 - j];
                }
                System.out.println(Arrays.toString(stream));
            }
        }
        return stream;
    }

    /**
     *
     * @param a
     * @param errorPattern
     * @return
     * @throws IOException
     */
    public static int[] alter(int[] a, int[] errorPattern) throws IOException {
        if (errorPattern.length > a.length) {
            IOException e = new IOException();
            throw (e);
        }
        for (int i = 0; i < errorPattern.length; i++) {
            a[i] = a[i] ^ errorPattern[i];
        }
        return a;
    }

    /**
     *
     * @param a
     * @param b
     * @return concatenated array b:a
     */
    public static int[] concat(int[] a, int[] b) {
        int aLen = a.length;
        int bLen = b.length;
        int[] c = new int[aLen + bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }
}
