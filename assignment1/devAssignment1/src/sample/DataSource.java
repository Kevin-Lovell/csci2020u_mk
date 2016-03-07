package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class DataSource {



    public static ObservableList<TestFile> getAllData() {
        ObservableList<TestFile> data = FXCollections.observableArrayList();

        //read treemap file here and add to data
        Map<String,Integer> wordCounts;
        /*public DataSource() {
            wordCounts = new TreeMap<>();
        }

        wordCounts.put("string1", 1);
        wordCounts.put("string2", 5);
        wordCounts.put("string3", 10);

        Set<String> keys = wordCounts.keySet();
        Iterator<String> keyIterator = keys.iterator();

        while(keyIterator.hasNext()) {
            String key = keyIterator.next();
            int count = wordCounts.get(key);
            data.add(new TestFile(key,count, key));
        }

        // TestFile ID, Assignments, Midterm, Final exam
        //data.add(new TestFile("string1", 75.0, "string2"));
        //data.add(new TestFile("string3", 70.0, "string4"));

        return data;
    }
}

