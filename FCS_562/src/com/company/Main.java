package com.company;
import java.util.Arrays;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        int[] kBits = new int[]{0,0,1,1,0,1,1,1,0,0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0};//LSB on Index 0
        
        int[] divisor = new int[]{1, 1,0,1};

        int[] crc1 = generateCRC(kBits, divisor);
        System.out.println("----------------------------------");
        System.out.println("----------------------------------");
        System.out.println("----------------------------------");
        
        Boolean output = checkCRC(concat(crc1,kBits), divisor);
        
        System.out.println("CRC: "+Arrays.toString(crc1));
        System.out.println("Syndrome Check: " + (output ? "Correct":"Incorrect"));
        
    }
    /**
     * 
     * @param payload   Payload bits with LSB at index 0
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
        for (int i = k + n -1; i >= n; i--) {   //From the MSB of the payload to the LSB of the payload.
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

    public static boolean checkCRC(int[] stream,int[] generator){

        for(int i=stream.length-1;i>=generator.length - 1;i--){
            if (stream[i] == 1) {    //if current bit is 1 then XOR the block length of n+1 with the generator.
                for (int j = 0; j < generator.length; j++) {
                    stream[i - j] ^= generator[generator.length-1 - j];
                }
                System.out.println(Arrays.toString(stream));
            }
        }
        for(int i:stream){
            if(i == 1)
                return false;
        }
        return true;
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
    

    /*
    public static String receive(int[] kBits, int[] nBits, int[] check){ //Nick's

        int pattern[] = new int[kBits.length + nBits.length];
        System.arraycopy(kBits, 0, pattern, 0, kBits.length);
        System.arraycopy(nBits, 0, pattern, kBits.length, nBits.length);

        for(int index = 0;index <(kBits.length);index++) {
            //iterates thru to end of DataWord "kBits length"
            System.out.print(Arrays.toString(pattern));
            System.out.println(" : " + index);

            if (pattern[index] == 0)//if pattern's current bit is 0, it does nothing
                System.out.println("DC");
            else {
                for (int k = 0; k < check.length; k++)
                    //XOR first divisor.length bits together then stores them back into the arr @ current index
                    pattern[index+k] = pattern[index+k] ^ check[k];
            }
        }
        System.out.println(Arrays.toString(pattern));

        int[] Syndrome = new int[nBits.length];
        System.arraycopy(pattern,(pattern.length- nBits.length),Syndrome, 0, nBits.length);
        System.out.println("Syndrome: " + Arrays.toString(Syndrome));

        for(int q : Syndrome)//foreach loops thru every val of Syndrome
        {
            if (Syndrome[q] != 0)
                return "Not Correct";
        }
            return "Correct";
    }
    */
}
