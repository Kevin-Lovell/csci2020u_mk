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

        Map<String,Double> hamList;
        Map<String,Double> spamList;
        Map<String,Double> totalList;

        hamList = new TreeMap<>();
        spamList = new TreeMap<>();
        totalList = new TreeMap<>();

        //Read files and put their names into an array, this is the first row of TestFile
        //Read file directory, ham or spam, into an array  this is the second row of TestFile
        //iterate over wordCounts and calculate the probability for each word, store in treeMap
        //do this for both ham(W|H) and spam(W|S)
        //iterate through W|H and W|S together and populate S|W, a new array, with the results,
        //read through every file, check words and to get their S|W, with that calc n, the S|F
        //this is the third row of the testFile
        //the three arrays(fileNames, directories, and probabilities) are printed in the table

        //read files, find words, match word to its probability in Treemap, sum all probabilities, get file probability, do this for all files.

        //replace this with the reading of the file and counting the words

        totalList.put("apple", 1.0);
        totalList.put("bar", 5.0);
        totalList.put("cat", 10.0);
        totalList.put("dog", 15.0);
        totalList.put("ear", 55.0);
        totalList.put("fire", 105.0);


        hamList.put("apple", 1.0);
        hamList.put("cat", 4.0);
        hamList.put("dog", 6.0);
        hamList.put("ear", 30.0);

        spamList.put("bar", 5.0);
        spamList.put("cat", 6.0);
        spamList.put("dog", 8.0);
        spamList.put("ear", 25.0);

        Set<String> keys = totalList.keySet();
        Iterator<String> keyIterator = keys.iterator();

        /////////////////Formula Testing Using PlaceHolder Data//////////////////////

        //Note: I am unsure how I will obtain the number of files containing spam and ham, so for now they will be fixed (100)
        //Also In order for this formula to work I would also need a list of ALL words

        Map<String,Double> probInSpamTree;
        Map<String,Double> probInHamTree;
        Map<String,Double> probWordIsSpamTree;

        probInSpamTree = new TreeMap<>();
        probInHamTree = new TreeMap<>();
        probWordIsSpamTree= new TreeMap<>();

        while(keyIterator.hasNext()) {
            String key = keyIterator.next();
            double probInSpam;
            if (spamList.containsKey(key)) {
                probInSpam = spamList.get(key);         //spamList.get(key) is working
                probInSpamTree.put(key, probInSpam);
            } else {
                probInSpamTree.put(key, 0.0);
            }
            double probInHam;
            if (hamList.containsKey(key)) {
                probInHam = hamList.get(key);
                probInHamTree.put(key, probInHam);
            } else {
                probInHamTree.put(key, 0.0);
            }
        }

        Set<String> spamKeys = probInSpamTree.keySet();
        Iterator<String> spamIterator = spamKeys.iterator();

        while(spamIterator.hasNext()) {
            String key = spamIterator.next();                                                      //naming this key in multiple places could be an issue
            //Count has to be modified to be the spam probability to be entered in the table
            double count = probInSpamTree.get(key);
            data.add(new TestFile(key,count, "test", "spam"));
        }

        Set<String> hamKeys = probInHamTree.keySet();
        Iterator<String> hamIterator = hamKeys.iterator();

        while(hamIterator.hasNext()) {
            String key = hamIterator.next();
            //Count has to be modified to be the spam probability to be entered in the table
            double count = probInHamTree.get(key);
            data.add(new TestFile(key,count, "test", "ham"));
        }

//        while(keyIterator.hasNext()) {
//            String key = keyIterator.next();
//            double probInHam = probInHamTree.get(key);
//            double probInSpam = probInSpamTree.get(key);
//            double probWordIsSpam = probInSpam/(probInHam + probInSpam);
//            data.add(new TestFile(key,5.0, "test", "ham"));
//        }

        return data;
    }
}