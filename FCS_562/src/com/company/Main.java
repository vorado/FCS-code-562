package com.company;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws Exception {

        int[] kBits = new int[] {1,1,0,1,0,0,1,1,1,0,1,1,0,0};//{1,0,0,0};
        int[] nBits = new int[] {1,0,0};//{1,1,0};
        int[] divisor = new int[] {1,0,1,1};

        String output = receive(kBits,nBits,divisor);
        System.out.println("from main: " + output);
    }

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
}

