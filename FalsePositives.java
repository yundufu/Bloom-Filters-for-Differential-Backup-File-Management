
/**
 * @author Xiaoyun Fu and Gaurav Raj 
 * All rights reserved.
 * This program implements an experiment to empirically evaluate the false probability rate of the bloom filters: 
 * BloomFilterFNV, BloomFilterMurmur, BloomFilterRan. 
 * 
 *
 */

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class FalsePositives {
	 public static void main(String[] args) {
		 final int biggerSetSize= 10000000;
		 int[] bitsPerElementValues = {4, 8, 10};
		 FalsePositives falsePositives = new FalsePositives(biggerSetSize);
		 for(int bitPerElement : bitsPerElementValues)
		 {
			 falsePositives.startExpKFNV(bitPerElement);
			 falsePositives.startExpKMurmur(bitPerElement);
			 falsePositives.startExpKRan(bitPerElement);
		 }
	 }
	
	
	//instance variables
    HashSet<String> NoRepetitions = new HashSet<String>();//Used to check if the randomly generated String has been generated before
    ArrayList<String> biggerSet = new ArrayList<String>();//a bigger Set of strings
    ArrayList<String> smallerSet = new ArrayList<String>();//a smaller Set of strings
    int largerSetSize;//Size of BiggerSet


    /**
     * 
     * @param n the number of strings to be stored in the Bloom Filter
     */
    public FalsePositives(int n){
        largerSetSize = n;
        generateRandomStrings(largerSetSize);
    }

    /**
     * Evaluate false positive rate of the bloom filter BloomFilterFNV.
     */
    public void startExpKFNV(int bitsPerElement){
        int falsePositives = 0;
        BloomFilterFNV myBloomFilterFNV = new BloomFilterFNV(largerSetSize, bitsPerElement);
        //Add everything in biggerSet to the Bloom Filter
        for(int i = 0; i < biggerSet.size(); i++){
            myBloomFilterFNV.add(biggerSet.get(i));
        }
        //Check whether the strings in smallerSet give false positive or not
        for(int i = 0; i < smallerSet.size(); i++){
            if(myBloomFilterFNV.appears(smallerSet.get(i))){
                falsePositives++;
            }
        }

        //Theoretical false positive rate is: 0.618^bitsPerElement
        System.out.println("******** kFNV RESULTS ********* ");
        System.out.println("False Positives: " + falsePositives);
        System.out.println("biggerSet.size(): " + biggerSet.size());
        System.out.println("smallerSet.size(): " + smallerSet.size());
        System.out.println("myBloomFilterFNV.filterSize(): " + myBloomFilterFNV.filterSize());
        System.out.println("myBloomFilterFNV.dataSize(): " + myBloomFilterFNV.dataSize());
        System.out.println("myBloomFilterFNV.numHashes(): " + myBloomFilterFNV.numHashes());
        double prob = (double) falsePositives / (double) smallerSet.size();
        System.out.println("false positive rate: " + prob);
        System.out.println("Theoretical false positive rate: " + Math.pow(0.618, bitsPerElement));
        System.out.println("**********************************************************");
    }

    /**
     * Evaluate false positive rate of the bloom filter BloomFilterMurmur.
     */
    public void startExpKMurmur(int bitsPerElement){
        int falsePositives = 0;
        BloomFilterMurmur myBloomFilterMurmur = new BloomFilterMurmur(largerSetSize, bitsPerElement);
        //Add everything in biggerSet to the Bloom Filter
        for(int i = 0; i < biggerSet.size(); i++){
            myBloomFilterMurmur.add(biggerSet.get(i));
        }
        //Check whether the strings in smallerSet give false positive or not
        for(int i = 0; i < smallerSet.size(); i++){
            if(myBloomFilterMurmur.appears(smallerSet.get(i))){
                falsePositives++;
            }
        }

        //Theoretical false positive rate is: 0.618^bitsPerElement
        System.out.println("********* k MURMUR RESULTS ********* ");
        System.out.println("False Positives: " + falsePositives);
        System.out.println("biggerSet.size(): " + biggerSet.size());
        System.out.println("smallerSet.size(): " + smallerSet.size());
        System.out.println("myBloomFilter.filterSize(): " + myBloomFilterMurmur.filterSize());
        System.out.println("myBloomFilter.dataSize(): " + myBloomFilterMurmur.dataSize());
        System.out.println("myBloomFilter.numHashes(): " + myBloomFilterMurmur.numHashes());
        double prob = (double) falsePositives / (double) smallerSet.size();
        System.out.println("false positive rate: " + prob);
        System.out.println("Theoretical false positive rate: " + Math.pow(0.618, bitsPerElement));
        System.out.println("**********************************************************");
    }

    /**
     * Evaluate false positive rate of the bloom filter BloomFilterRan.
     */
    public void startExpKRan(int bitsPerElement){
        int falsePositives = 0;
        BloomFilterRan myBloomFilterRAN = new BloomFilterRan(largerSetSize, bitsPerElement);
        //Add everything in biggerSet to the Bloom Filter
        for(int i = 0; i < biggerSet.size(); i++){
            myBloomFilterRAN.add(biggerSet.get(i));
        }
        //Check whether the strings in smallerSet give false positive or not
        for(int i = 0; i < smallerSet.size(); i++){
            if(myBloomFilterRAN.appears(smallerSet.get(i))){
                falsePositives++;
            }
        }

        //Theoretical false positive rate is: 0.618^bitsPerElement
        System.out.println("******** kRAN RESULTS ********* ");
        System.out.println("False Positives: " + falsePositives);
        System.out.println("biggerSet.size(): " + biggerSet.size());
        System.out.println("smallerSet.size(): " + smallerSet.size());
        System.out.println("myBloomFilter.filterSize(): " + myBloomFilterRAN.filterSize());
        System.out.println("myBloomFilter.dataSize(): " + myBloomFilterRAN.dataSize());
        System.out.println("myBloomFilter.numHashes(): " + myBloomFilterRAN.numHashes());
        double prob = (double) falsePositives / (double) smallerSet.size();
        System.out.println("false positive rate: " + prob);
        System.out.println("Theoretical false positive rate: " + Math.pow(0.618, bitsPerElement));
        System.out.println("**********************************************************");
    }


/**
 * Generate k + k/10 random distinct strings with length in the range [minimum, maximum].
 * Add k strings to biggerSet and k/10 strings to smallerSet.
 */
    private void generateRandomStrings(int k){
        int strLength;
        int minLengthOfStr = 6;//Lower Limit of String Length
        int maxLengthOfStr = 12;//Upper Limit of String Length
        int counter = 0;//Increments every time you add something to the biggerSet
        String randStr = "";

        Random rand = new Random();
        //k is number of strings to be added to biggerSet
        while (biggerSet.size() < k){
            randStr = "";
            strLength = rand.nextInt(maxLengthOfStr - (minLengthOfStr - 1))
                    + minLengthOfStr;
            for(int j = 0; j < strLength; j++){
                randStr = randStr + (char) (rand.nextInt(26) + 97);
            }
            if(! NoRepetitions.contains(randStr)){
                if(counter < 10) {//Add the string to the biggerSet
                    counter++;
                    NoRepetitions.add(randStr);
                    biggerSet.add(randStr);
                }else{//Add the String to the smallerSet
                    counter = 0;
                    NoRepetitions.add(randStr);
                    smallerSet.add(randStr);
                }
            }
        }
    }

    /**
     * Print out the strings in biggerSet and smallerSet.
     */
    public void printList(){
        System.out.println("*** Printing biggerSet ***");
        for(String s: biggerSet){
            System.out.println(s);
        }

        System.out.println("*** Printing smallerSet ***");
        for(String s: smallerSet){
            System.out.println(s);
        }
    }
}
