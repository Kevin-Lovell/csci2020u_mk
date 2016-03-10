package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sun.reflect.generics.tree.Tree;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class DataSource {
    Map<String,Double> trainHamFreq;
    Map<String,Double> trainSpamFreq;
    static double finalAccuaracy, finalPrecision;


    DataSource(Map tHam, Map tSpam) {
        this.trainHamFreq = tHam;
        this.trainSpamFreq = tSpam;
    }
    public static ObservableList<TestFile> getAllData(TreeMap tHam, TreeMap tSpam) {
        ObservableList<TestFile> data = FXCollections.observableArrayList();

        Map<String,Double> hamList;
        Map<String,Double> spamList;
        Map<String,Double> totalList;

        hamList = new TreeMap<>();
        spamList = new TreeMap<>();

        //uncomment for these to work
        hamList = tHam;
        spamList = tSpam;
        totalList = new TreeMap<>();

        totalList = (TreeMap)((TreeMap)spamList).clone();

        Set<String> hamListKeys = hamList.keySet();
        Iterator<String> hamListKeyIterator = hamListKeys.iterator();

        //This loop combines both the ham list and spam list into one large list of all words
        while(hamListKeyIterator.hasNext()) {
            String key = hamListKeyIterator.next();
            if (spamList.containsKey(key)){
                totalList.put(key, hamList.get(key) + spamList.get(key));
            }else{
                totalList.put(key, hamList.get(key));
            }
        }

        Set<String> keys = totalList.keySet();
        Iterator<String> keyIterator = keys.iterator();

        Map<String,Double> totalList2 = totalList;

        Set<String> keys2 = totalList2.keySet();
        Iterator<String> keyIterator2 = keys2.iterator();


        /////////////////Formula Testing Using PlaceHolder Data//////////////////////
        //Note: I am unsure how I will obtain the number of files containing spam and ham, so for now they will be fixed (100)
        //Also In order for this formula to work I would also need a list of ALL words

        Map<String,Double> probInSpamTree;
        Map<String,Double> probInHamTree;
        Map<String,Double> probWordIsSpamTree;

        probInSpamTree = new TreeMap<>();
        probInHamTree = new TreeMap<>();
        probWordIsSpamTree= new TreeMap<>();

        //Performing calculations to find the probability a word will appear
        // in ham and the probability it will appear spam
        while(keyIterator.hasNext()) {
            String key = keyIterator.next();
            double probInSpam;
            if (spamList.containsKey(key)) {
                probInSpam = spamList.get(key) / spamList.size();
                probInSpamTree.put(key, probInSpam);
            } else {
                probInSpamTree.put(key, 0.0);
            }
            double probInHam;
            if (hamList.containsKey(key)) {
                probInHam = hamList.get(key) / hamList.size();
                probInHamTree.put(key, probInHam);
            } else {
                probInHamTree.put(key, 0.0);
            }
        }


        while(keyIterator2.hasNext()) {
            String key = keyIterator2.next();
            double probInHam = probInHamTree.get(key);
            double probInSpam = probInSpamTree.get(key);
            double probWordIsSpam = probInSpam/(probInHam + probInSpam);
            data.add(new TestFile(key, probWordIsSpam, "test", "ham"));
        }

        finalAccuaracy = 1.2345;
        finalPrecision = 6.7890;


        return data;
    }
}