
/**
 * @author Xiaoyun Fu and Gaurav Raj 
 * All rights reserved.
 * This class implements the naive way to retrieve a record associated with the key: 
 * search the key in the differential file. If it exists, return the associated record; otherwise search in the database.
 */

import java.awt.RenderingHints.Key;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class NaiveDifferential {
//    public static void main(String[] args) {
//        NaiveDifferential myDiff = new NaiveDifferential();
//        //Exists in Diff
//        System.out.println(myDiff.retrieveRecord("artistic study , and"));
//        //Does not exist in Diff but exists in Dbases
//        System.out.println(myDiff.retrieveRecord("arts colleges in Pennsylvania"));
//        //Does not exist in either
//        System.out.println(myDiff.retrieveRecord("arts_NOUN and_CONJ general_ADJ studies_"));
//    }

    public String retrieveRecord(String key, String diffFile, String database){
        String myKey = key.replaceAll("\\s+","");

        File file = new File( diffFile); //search the key in diffFile directly
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] arrOfStr = line.split(" ", 5);
                String temp = "";
                for(int i = 0; i < arrOfStr.length - 1; i++){
                    temp = temp + arrOfStr[i];
                }
                if(temp.equals(myKey)){
                    System.out.println("Found in DiffFile.txt");
                    System.out.println(line);
                    return line;
                }
            }
            //key is not in diffFile
            return retrieveRecordfromDatabase(myKey, database);
        }catch(IOException e) {
            System.out.println("Exception in getFileContents(" +  diffFile + "), msg=" + e);
        }
        return key + " does not exist!";
    }

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
//                    System.out.println("Found in Database.txt");
//                    System.out.println(line);
                    return line;
                }
            }
        }catch(IOException e){
            System.out.println("Exception in getFileContents(" +database + "), msg=" + e);
        }
        return myKey + "does not exist!";
    }

}
