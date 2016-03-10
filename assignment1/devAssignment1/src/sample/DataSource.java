package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sun.reflect.generics.tree.Tree;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class DataSource {
       static double finalAccuaracy, finalPrecision;


    public static ObservableList<TestFile> getAllData(TreeMap probSpam) {
        ObservableList<TestFile> data = FXCollections.observableArrayList();
        TreeMap<String, Double> prob;
        prob = new TreeMap<>();

        prob = probSpam;

        Set<String> SpamListKeys = prob.keySet();
        Iterator<String> SpamListKeyIterator = SpamListKeys.iterator();

       while(SpamListKeyIterator.hasNext()) {
           String key = SpamListKeyIterator.next();
           double probWordIsSpam = prob.get(key);
           data.add(new TestFile(key, probWordIsSpam, "test", "ham"));
        }
        finalAccuaracy = 1.2345;
        finalPrecision = 6.7890;


        return data;
    }
}