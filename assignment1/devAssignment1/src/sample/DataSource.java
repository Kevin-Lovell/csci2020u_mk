package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jdk.internal.dynalink.support.ClassMap;
import sun.reflect.generics.tree.Tree;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class DataSource {
       static double finalAccuaracy, finalPrecision;


    public static ObservableList<TestFile> getAllData(TreeMap probSpam, TreeMap classMap) {
        ObservableList<TestFile> data = FXCollections.observableArrayList();
        TreeMap<String, Double> prob;
        TreeMap<String, String> actClass = new TreeMap<>();
        actClass = classMap;
        prob = new TreeMap<>();

        prob = probSpam;

        Set<String> SpamListKeys = prob.keySet();
        Iterator<String> SpamListKeyIterator = SpamListKeys.iterator();

       while(SpamListKeyIterator.hasNext()) {
           String key = SpamListKeyIterator.next();
           double probWordIsSpam = prob.get(key);
           //String category = ~.get.(key);
           String actualClass = actClass.get(key);
           data.add(new TestFile(key, probWordIsSpam, actualClass, "ham"));
           //data.add(new TestFile(key, probWordIsSpam, actualClass, category));
        }
        finalAccuaracy = 1.2345;
        finalPrecision = 6.7890;


        return data;
    }
}