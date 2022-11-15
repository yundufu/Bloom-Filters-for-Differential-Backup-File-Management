
/**
 * @author Xiaoyun Fu and Gaurav Raj 
 * All rights reserved.
 * This class implements a Bloom Filter using MurmurHash hash functions.
 */

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;

public class BloomFilterMurmur {

    private BitSet myBit; //The Bloom Filter, a BitSet is used instead of an integer array to save space because only binary numbers are stored.
    private int filterSize;//The size of the bloom filter
    private int numOfElmntsAdded = 0;// the number of elements added in the Bloom Filter
    private int k = 0; //The number of hash functions to be generated
    int kMurmurSeedValues[];//This stores the k-Murmur seed values to be used to calculate Murmur
    private ArrayList<kMurmur> kMurmurs = new ArrayList<kMurmur>();//A data structure to store the kMurmur functions that are generated

    /**
     * Creates a Bloom filter that can store a set S of cardinality setSize.        
     * @param setSize the number of elements in set S
     * @param bitsPerElement a parameter to control the size of the Bloom Filter generated.
     */
    BloomFilterMurmur(int setSize, int bitsPerElement) {
        filterSize = findPrime(setSize * bitsPerElement); //The size of the filter should approximately be setSize * bitsPerElement.
        myBit = new BitSet(filterSize);
        k = (int)(Math.log(2) * bitsPerElement) ; //Calculates the number of hash functions to be generated
        kMurmurSeedValues = new int[k];
        generateKSeedValues();

        for (int i = 0; i < kMurmurSeedValues.length; i++){ //generate the k-Murmur Hash Functions themselves
            int seed = kMurmurSeedValues[i];
            kMurmur myMurmur = new kMurmur(seed, filterSize);
            kMurmurs.add(myMurmur);
        }
    }

    /**
     *  Add a string to the bloom filter.
     * @param s a string to be added to the Bloom Filter
     */
    public void add(String s){
        for(int i = 0; i < kMurmurs.size(); i++){     	//Add string s to the Bloom Filter (to each hash table)
            myBit.set(kMurmurs.get(i).hashV(s.toLowerCase()));
        }
        numOfElmntsAdded++; //Increment counter for number of elements added
    }

    /**
     * Check whether a string is stored in the bloom filter or not. This method must be case-insensitive.
     * @param s a string whose membership in the bloom filter is to be checked
     * @return Returns true if s appears in the filter; otherwise returns false. 
     */
    public boolean appears(String s){
        for(int i = 0; i < kMurmurs.size(); i++){
            if (! myBit.get(kMurmurs.get(i).hashV(s.toLowerCase()))){
                return false;
            }
        }
        return true;
    }

    /**
     *  Get the size of the bloom filter (the size of hash tables).
     * @return the size of the bloom filter filter
     */
    public int filterSize() {
        return filterSize;
    }

    /**
     *  Get the number of elements stored in the bloom filter.
     * @return the number of elements added to the filter
     */
    public int dataSize() {
        return numOfElmntsAdded;
    }

    /**
     * Get the number of hash functions used for the bloom filter.
     * @return the number of hash functions used for the bloom filter.
     */
    public int numHashes() {
        return k;
    }

    /**
     *  Generate k independent seed values to be used to calculate MurmurHash function.
     */
    private void generateKSeedValues(){
        Random rand = new Random();
        for(int i = 0; i < kMurmurSeedValues.length; i++){
            int j = 0;
            while (j == 0){
                j = rand.nextInt();
            }
            j = Math.abs(j);
            j = findPrime(j);
            kMurmurSeedValues[i] = Math.abs(j);
        }
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
}
