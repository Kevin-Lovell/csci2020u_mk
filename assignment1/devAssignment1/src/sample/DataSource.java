package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class DataSource {

    public static ObservableList<TestFile> getAllData() {
        ObservableList<TestFile> data = FXCollections.observableArrayList();

        //read treemap file here and add to data
        Map<String,Integer> wordCounts;
        Map<String,Double> probCounts;
        //public DataSource() {
            wordCounts = new TreeMap<>();
        //}

        probCounts = new TreeMap<>();

        //Read files and put their names into an array, this is the first row of TestFile
        //Read file directory, ham or spam, into an array  this is the second row of TestFile
        //iterate over wordCounts and calculate the probability for each word, store in treeMap
        //do this for both ham(W|H) and spam(W|S)
        //iterate through W|H and W|S together and populate S|W, a new array, with the results,
        //read through every file, check words and to get their S|W, with that calc n, the S|F
        //this is the third row of the testFile
        //the three arrays(fileNames, directories, and probabilities) are printed in the table

        //word, #
        //word, probability
        //fileNames

        //read files, find words, match word to its probability in Treemap, sum all probabilities, get file probability, do this for all files.

        //replace this with the reading of the file and counting the words
        wordCounts.put("string1", 1);
        wordCounts.put("string2", 5);
        wordCounts.put("string3", 10);

        Set<String> keys = wordCounts.keySet();
        Iterator<String> keyIterator = keys.iterator();

        //The array for actualClass, displays Ham or Spam
        String[] actualclass = new String[100];
        actualclass[1] = "one";
        actualclass[2] = "two";
        actualclass[3] = "three";

        int counter = 0;

        while(keyIterator.hasNext()) {
            String key = keyIterator.next();
            //Count has to be modified to be the spam probability to be entered in the table
            int count = wordCounts.get(key);
            double probability = count*5;
            probCounts.put(key,probability);
            counter++;
            data.add(new TestFile(key,count, actualclass[counter], "ham"));
        }

        System.out.println(probCounts);
        // TestFile ID, Assignments, Midterm, Final exam
        //data.add(new TestFile("string1", 75.0, "string2"));
        //data.add(new TestFile("string3", 70.0, "string4"));

        return data;
    }
}

