
/**
 * @author Xiaoyun Fu and Gaurav Raj 
 * All rights reserved.
 * This program compares the naive way and the time-efficient way using bloom filters in the application of
 * retrieving records associated with a query key in the differential file management system where 
 * the differential file stores all the records that are newly changed, and the database stores all the old records.
 * 
 * The comparison is done in 3 modes.
 * Mode 1: select a random Key from the database
 * Mode 2: select a random key from the differential file
 * Mode 3: select a random key that is not in the differential file but is in the database
 * 
 * The average time difference of the two methods over 10 experiments is reported.
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import javax.management.modelmbean.ModelMBean;

public class EmpericalComparison {

    public static void main(String[] args) {
    	int numExperiments = 10;
    	int bitsPerElement = 8;
    	String differentialFile = "differential.txt"; 
    	int numItemsInDiffFile = 1262147;
    	String keysOfDatabase = "keysOfDatabase.txt";
       	String database  = "database.txt";
       	int numItemsInDatabase = 12632196;
       	String subsetFile = "subset.txt"; //this is a sub collection of entries that are in database but not in differential file
       	int numItemsInSubsetFile = 722;
        EmpericalComparison myEmperor = new EmpericalComparison();
        for(int mode = 1; mode <= 3; mode++ )
        {
            myEmperor.startEmpiricalExp(numExperiments, mode, differentialFile, numItemsInDiffFile, bitsPerElement, keysOfDatabase, database, numItemsInDatabase, subsetFile, numItemsInSubsetFile);
        }
    }

/**
 * 
 * @param numExperiments  number of times the experiment is conducted
 * @param diffFile name of the differential file
 * @param numItemsInDiffFile number of entries/items in differential file
 * @param bitsPerElement bits per element used to generate the bloom filter
 * @param database name of the database file
 * @param numItemsInDatabase number of entries/items in database file
 */
    public void startEmpiricalExp(int numExperiments, int mode, String diffFile, int numItemsInDiffFile, int bitsPerElement, String keysOfDatabase, String database, int numItemsInDatabase, String subsetFile, int numItemsInSubsetFile){
       //pre-processing
        //Create and store the keys in differential file into the BloomDifferential to be used for the experiment
        BloomDifferential myBloom = new BloomDifferential();
        myBloom.createFilter(diffFile, numItemsInDiffFile, bitsPerElement);
        //Now create and store the NaiveDifferential to be used for the experiment
        NaiveDifferential myNaive = new NaiveDifferential();
       
        long bloomTime  = 0;
        long naiveTime = 0;
        String key="";//This will be the key that will be searched in BloomDiffernetial and NaiveDifferential
        String keySelectedFromFile = "";
        int numItemsInFile = 0;
        for(int i = 0; i < numExperiments; i++){
            //Generate Key
        	if(mode == 1)            // Mode 1: select a random Key from  keysOfDatabase
        	{
        		keySelectedFromFile = keysOfDatabase;
                numItemsInFile = numItemsInDatabase;        		
        	}	
        	else if(mode == 2)   //Mode 2: select a random key from the differential file
        	{
        		keySelectedFromFile = diffFile;
        		numItemsInFile = numItemsInDiffFile;
        	}
        	else if(mode == 3) //Mode 3: select a random key that is not in the differential file but is in the database
        	{
        		keySelectedFromFile = subsetFile;
        		numItemsInFile = numItemsInSubsetFile;
        	}
        	key = generateRandomKey(keySelectedFromFile, numItemsInFile);
        	
            long startTime;
            long endTime;
            System.out.println("*** Bloom Differential Experiment: " + i + " ***" );
            //Experiment for BloomDifferential==================================
            startTime = System.currentTimeMillis();
            myBloom.retrieveRecord(key, diffFile,database);
            endTime   = System.currentTimeMillis();
            bloomTime =  bloomTime + (endTime - startTime);
            //=======================================================

            System.out.println("*** Naive Differential Experiment: " + i + " ***" );
            //Start experiment for NaiveDifferential--------
            startTime = System.currentTimeMillis();
            myNaive.retrieveRecord(key, diffFile,database);
            endTime   = System.currentTimeMillis();
            naiveTime = naiveTime +  (endTime - startTime);
            //End experiment for NaiveDifferential--------
            System.out.println("***** END OF ITERATION *****");
            System.out.println();
            System.out.println();
        }
        System.out.println("Average time to retrive a record using BloomDifferential: " + 1.0 * bloomTime/numExperiments + " ms.");
        System.out.println("TAverage time to retrive a record using naiveDifferential: " + 1.0 * naiveTime/numExperiments + " ms.");
    }

    private String generateRandomKey(String keySelectedFromFile, int numItemsInFile){
        int randomLineNumber = 0;//key will be located at this line number
        int countLineNum = 1;//Counts the line numbers currently iterating through in text doc
        String key = "";
        Random rand = new Random();
        randomLineNumber = rand.nextInt(numItemsInFile);
//        System.out.println(randomLineNumber);

        //Search for the key at randomLineNumber
        File file = new File(keySelectedFromFile);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while(countLineNum < randomLineNumber){
                br.readLine();
                countLineNum++;
            }
            line = br.readLine();//This should be line at line number: randomLineNumber
            String[] arrOfStr = line.split(" ", 5); 

            for(int i = 0; i < arrOfStr.length ; i++){
                key = key + arrOfStr[i];
            }
        }catch(IOException e){
            System.out.println("Exception in getFileContents(" + keySelectedFromFile + "), msg=" + e);
        }
        return key;
    }


}
