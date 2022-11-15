
/**
 * @author Xiaoyun Fu and Gaurav Raj 
 * All rights reserved.
 * This class implements a bloom filter that stores all keys in a differential file, 
 * and uses the bloom filter for faster query retrieval.
 */

import java.io.*;

public class BloomDifferential {

    private BloomFilterFNV diffFilter;

    /**
     * Returns a bloom filter corresponding to the records in the file diffFile.
     * Each line of this file is of the following from:
     *  word1 word2 word3 word4 year1 n1 m1 year2 n2 m2 .....
     *  For example a record would look like 
     *  Archbishop had given him 1720 8 6 1727 10 4 1758 20 6 .....
     * @param diffFile the name of the file whose content is to be stored in the created bloom filter
     * @return a bloom filter
     */
    public BloomFilterFNV createFilter(String diffFile, int numItems, int bitsPerElement){
        diffFilter = new BloomFilterFNV(numItems, bitsPerElement);

        File file = new File(diffFile);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] arrOfStr = line.split(" ", 5); //only the first 4 words are the key
                String temp = "";
                for(int i = 0; i < arrOfStr.length - 1; i++){
                    temp = temp + arrOfStr[i];
                }
                diffFilter.add(temp);
            }
        }catch(IOException e){
            System.out.println("Exception in getFileContents(" + diffFile + "), msg=" + e);
        }
        return diffFilter;
    }

    /**
     * Retrieve the newest record associated with the key.
     * @param key 
     * @param diffFile name of the differential file that stores changed records
     * @param database name of the database where all records (all old records) are stored
     * @return the record associated with the key. 
     *  If the record appears in the diffFile, return the record there. Otherwise return the record stored in database
     */
    public String retrieveRecord(String key,  String diffFile, String database){
        String myKey = key.replaceAll("\\s+","");
        if(diffFilter.appears(myKey)){
            File file = new File(diffFile);
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] arrOfStr = line.split(" ", 5);
                    String temp = "";
                    for(int i = 0; i < arrOfStr.length - 1; i++){
                        temp = temp + arrOfStr[i];
                    }
                    if(temp.equals(myKey)){
//                        System.out.println("Found in DiffFile.txt");
//                        System.out.println(line);
                        return line;
                    }
                }
              //key is not found in DiffFile, the Bloom filter gave a false positive
                return retrieveRecordfromDatabase(myKey, database); //search database
            }catch(IOException e){
                System.out.println("Exception in getFileContents(" + diffFile + "), msg=" + e);
            }
        }else{// key is not in diffFile
            return retrieveRecordfromDatabase(myKey, database);
        }
        return key + " does not exist!";
    }

    /**
     * Retrieve the record associated with the key from the database.
     * @param myKey a key whose value/record is to be retrieved
     * @param database name of the file where all records are stored
     * @return the record associated with the key
     */
    private String retrieveRecordfromDatabase(String myKey, String database){
        File file = new File(database);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] arrOfStr = line.split(" ", 5);
                String temp = "";
                for(int i = 0; i < arrOfStr.length - 1; i++){
                    temp = temp + arrOfStr[i];
                }
                if(temp.equals(myKey)){
                    return line;
                }
            }
        }catch(IOException e){
            System.out.println("Exception in getFileContents(" + database + "), msg=" + e);
        }
        return "Key does not exist!";
    }
}
