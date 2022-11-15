
/**
 * @author Xiaoyun Fu and Gaurav Raj 
 * All rights reserved.
 * This classes implements a MurmurHash hash function.
 */

import java.util.Random;

public class kMurmur {
    private int seedValue;
    private int a; //h(x) = (ax + b) % p This is the a.
    private int b; //h(x) = (ax + b) % p This is the b.
    private int filterSize; //h(x) = (ax + b) % p This is the p.

    public kMurmur(int value, int size){
        seedValue = value;
        filterSize = size;

        Random rand = new Random();
        while (a == 0)
            a = rand.nextInt(filterSize);
        while (b == 0)
            b = rand.nextInt(filterSize);
    }

    /**
     * gets the hash value of input string str using MurmurHash function.
     * @param str a string whose hash value s to be computed
     * @return the hash value of str using MurmurHash function.
     */
    public int hashV(String str){
        int index = 0;
        long hash = 0;
        byte[] byteArray = str.getBytes();
        hash = hash64(byteArray, byteArray.length, seedValue);
        index = (int) ((Math.abs (a * Math.abs(hash) + b)) % filterSize);
        return index;
    }



    /** Generates a 64 bit hash value from byte array of the given length and seed.
     * @param data byte array to hash
     * @param length length of the array to hash
     * @param seed initial seed value
     * @return 64 bit hash of the given array
     */
    public static long hash64( final byte[] data, int length, int seed) {
        final long m = 0xc6a4a7935bd1e995L;
        final int r = 47;
        long h = (seed&0xffffffffl)^(length*m);
        int length8 = length/8;
        for (int i=0; i<length8; i++) {
            final int i8 = i*8;
            long k =  ((long)data[i8+0]&0xff)      +(((long)data[i8+1]&0xff)<<8)
                    +(((long)data[i8+2]&0xff)<<16) +(((long)data[i8+3]&0xff)<<24)
                    +(((long)data[i8+4]&0xff)<<32) +(((long)data[i8+5]&0xff)<<40)
                    +(((long)data[i8+6]&0xff)<<48) +(((long)data[i8+7]&0xff)<<56);

            k *= m;
            k ^= k >>> r;
            k *= m;
            h ^= k;
            h *= m;
        }

        switch (length%8) {
            case 7: h ^= (long)(data[(length&~7)+6]&0xff) << 48;
            case 6: h ^= (long)(data[(length&~7)+5]&0xff) << 40;
            case 5: h ^= (long)(data[(length&~7)+4]&0xff) << 32;
            case 4: h ^= (long)(data[(length&~7)+3]&0xff) << 24;
            case 3: h ^= (long)(data[(length&~7)+2]&0xff) << 16;
            case 2: h ^= (long)(data[(length&~7)+1]&0xff) << 8;
            case 1: h ^= (long)(data[length&~7]&0xff);
                h *= m;
        };

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;

        return h;
    }
}
