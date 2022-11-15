
/**
 * @author Xiaoyun Fu and Gaurav Raj 
 * All rights reserved.
 * This classes implements a Fowler–Noll–Vo hash function.
 */

import java.util.Random;

public class kFNV {

    private long offset_basis;
    private final long FNV_prime = 0x100000001b3L;
    private int a; //h(x) = (ax + b) % p This is the a.
    private int b; //h(x) = (ax + b) % p This is the b.
    private int filterSize; //h(x) = (ax + b) % p This is the p.

    public kFNV(int offba, int size){
        offset_basis = offba;
        filterSize = size;
        Random rand = new Random();
        while (a == 0)
            a = rand.nextInt(filterSize);
        while (b == 0)
            b = rand.nextInt(filterSize);
    }

    /**
     * gets the hash value of input string str using Fowler–Noll–Vo hash function.
     * @param str a string whose hash value s to be computed
     * @return the hash value of str using MurmurHash function.
     */
    public int hashV(String str){
        long hash = offset_basis;
        int index = 0;
        char[] charArray = str.toCharArray();
        for(int i = 0; i < charArray.length; i++){
            hash = hash ^ charArray[i];
            hash = hash * FNV_prime;
        }
        index = (int) ((Math.abs (a * Math.abs(hash) + b)) % filterSize);
        return index;
    }

    /**
     * Find the smallest prime that is at least as large as the input value n
     * @param n an input integer value
     * @return the smallest prime that is at least as large as n
     */
    private int findPrime(int n) {
        boolean found = false;
        int num = n;
        while (!found) {
            if (isPrime(num))
                return num;
            num++;
        }
        return -1;

    }

    /**
     * Check whether an input value n is a prime or not.
     * @param n an input integer value
     * @return true if the input value n is a prime; false otherwise.
     */
    private boolean isPrime(int n) {
        for (int i = 2; i <= Math.sqrt(n); i++)
            if (n % i == 0)
                return false;
        return true;
    }


    /**
     * Gets the offset_basis used in Fowler–Noll–Vo hash function.
     * @return the offset_basis used in Fowler–Noll–Vo hash function.
     */
    public long getOffset_basis() {
        return offset_basis;
    }

    /**
     * Get the value a used in h(x) = a * x + b
     * @return the value a
     */
    public int getA() {
        return a;
    }

    /**
     * Get the value b used in h(x) = a * x + b
     * @return the value b
     */
    public int getB() {
        return b;
    }

}
