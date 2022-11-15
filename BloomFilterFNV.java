
/**
 * @author Xiaoyun Fu and Gaurav Raj 
 * All rights reserved.
 * This class implements a Bloom Filter using Fowler–Noll–Vo hash functions.
 *
 */


import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;

public class BloomFilterFNV {

    public BitSet myBit;//The Bloom Filter, a BitSet is used instead of an integer array to save space because only binary numbers are stored.
    private int filterSize;//The size of the bloom filter
    private int numOfElmntsAdded = 0;//Counts the number of elements added to the Bloom Filter
    private int k = 0; //The number of hash functions to be generated
    int kOffsetBasisValues[];//This stores the k-offset basis values to be used to calculate FNV
    private ArrayList<kFNV> kFNVS = new ArrayList<kFNV>();//A data structure to store the k kFNV functions that are generated

    /**
     * Creates a Bloom filter that can store a set S of cardinality setSize.        
     * @param setSize the number of elements in set S
     * @param bitsPerElement a parameter to control the size of the Bloom Filter generated.
     */
    BloomFilterFNV(int setSize, int bitsPerElement) {
        filterSize = findPrime(setSize * bitsPerElement); //The size of the filter should approximately be setSize * bitsPerElement.
        myBit = new BitSet(filterSize);
        k = (int)(Math.log(2) * bitsPerElement);//Calculates the number of hash functions to be generated
        kOffsetBasisValues = new int[k];
        generateKOffsetBasisValues();          //Generating k integers. Then casting them into a long later.

        for (int i = 0; i < kOffsetBasisValues.length; i++)         //Generate the k-FNV Hash Functions.
        { 
            int offsetBasis = kOffsetBasisValues[i];
            kFNV myFNV = new kFNV(offsetBasis, filterSize);
            kFNVS.add(myFNV);
        }
    }

    /**
     *  Add a string to the bloom filter.
     * @param s a string to be added to the Bloom Filter
     */
    public void add(String s){
        for(int i = 0; i < kFNVS.size(); i++){      	//Add string s to the Bloom Filter (to each hash table)
            myBit.set(kFNVS.get(i).hashV(s.toLowerCase()));  //should be case-insensitive
        }
        numOfElmntsAdded++;  //Increment counter for number of elements added
    }

   /**
    * Check whether a string is stored in the bloom filter or not. This method must be case-insensitive.
    * @param s a string whose membership in the bloom filter is to be checked
    * @return Returns true if s appears in the filter; otherwise returns false. 
    */
    public boolean appears(String s){
        for(int i = 0; i < kFNVS.size(); i++){
            if (! myBit.get(kFNVS.get(i).hashV(s.toLowerCase()))){
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
    * Generate k offset basis values used in the k independent Fowler–Noll–Vo hash functions.
    */
    private void generateKOffsetBasisValues(){
        Random rand = new Random();
        for(int i = 0; i < kOffsetBasisValues.length; i++){
            int j = 0;
            while (j == 0)
            {
                j = rand.nextInt();
            }
            j = Math.abs(j);
            j = findPrime(j);
            kOffsetBasisValues[i] = Math.abs(j);
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
        return -1; //never reaches here, placed for syntax reason
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
