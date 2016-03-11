package sample;

import javafx.stage.Stage;
import sun.reflect.generics.tree.Tree;

import java.io.*;
import java.util.*;

public class WordCounter {
    TreeMap<String,Double> trainHamFreq;
    TreeMap<String,Double> trainSpamFreq;
    TreeMap<String,Integer> countWords;
    TreeMap<String,Double> hamList;
    TreeMap<String,Double> fileSpamProb;
    TreeMap<String,Double> spamList;
    TreeMap<String,Double> totalList;
    TreeMap<String,String> categor;
    TreeMap<String, String> actClass;


    public WordCounter() {
        trainHamFreq = new TreeMap<>();
        trainSpamFreq = new TreeMap<>();
        countWords = new TreeMap<>();
        fileSpamProb = new TreeMap<>();
        actClass = new TreeMap<>();
        categor = new TreeMap<>();
    }

    public TreeMap<String, Double> getHam() {
        return trainHamFreq;
    }

    public TreeMap<String, Double> getSpam() {
        return trainSpamFreq;
    }

    public TreeMap<String, String> getCategor() { return categor; }

    public TreeMap<String, String> getActClass() { return actClass; }

    public TreeMap<String, Double> getFileSpamProb() { return fileSpamProb; }

    //change parameters based on variables
    public void processFileforMath(File file, TreeMap probWordIsSpamTree, String classification) throws IOException {
        if (file.isDirectory()) {
            // process all of the files recursively
            File[] filesInDir = file.listFiles();
            for (int i = 0; i < filesInDir.length; i++) {
                processFileforMath(filesInDir[i], probWordIsSpamTree, classification);
            }
        } else if (file.exists()) {
            // load all of the data, and process it into words
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String word = scanner.next();
                if (isWord(word)) {
                    countWord(word);
                }
                //calc & if statement for categy
                //double for %


                //calc end


                actClass.put(file.getName(), classification);
               // categor.put(file.getName(), )
                fileSpamProb.put(file.getName(), 0.0);
                //fileSpamProb.put(file.getName(), % double);
            }
        }
    }

    private void countWord(String word) {
        if (countWords.containsKey(word)) {
            int oldCount = countWords.get(word);
            countWords.put(word, oldCount + 1);
        } else {
            countWords.put(word, 1);
        }
    }



    public void processFile(File file, String folderFrom) throws IOException {
        if (file.isDirectory()) {
            //process all of the files recursively
            File[] filesInDir = file.listFiles();
            for (int i = 0; i < filesInDir.length; i++) {
                processFile(filesInDir[i], folderFrom);
            }
        } else if (file.exists()) {
            //load all of the data and process it into words
            Scanner scanner = new Scanner(file);
            String email = "";

            while (scanner.hasNext()) {
                String word = scanner.next();
                if ((isWord(word))) {
                    email = email + " " + word;
                }
            }
            //deletes duplicates from scanner(twice to avoid duplicates)
            email = removeDupes(email, "\\ ");
            email = removeDupes(email, "\\ ");

            //puts the string back into a temporary textfile to make it easier to reuse some previous code
            try{
                //creates a temp file
                File tmp = new File("temp.txt");

                //deletes file on exit
                tmp.deleteOnExit();

                if (tmp.createNewFile()){
                    //writes to temp file for the first time
                    BufferedWriter bw = new BufferedWriter(new FileWriter(tmp));
                    bw.write(email);
                    bw.close();
                } else {
                    //rewrites to temp file if it already exists
                    BufferedWriter bw = new BufferedWriter(new FileWriter(tmp));
                    bw.write(email);
                    bw.close();
                }

                //processes the tmp file to scanner, then puts it into treemap
                Scanner scan = new Scanner(tmp);
                while (scan.hasNext()) {
                    String word = scan.next();
                    if(folderFrom == "ham") {
                        countHam(word);
                    } else if (folderFrom == "spam") {
                        countSpam(word);
                    }
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String removeDupes(String email, String splitRe) {
        List<String> values = new ArrayList<String>();
        //splits the string version of the email into single words
        String[] splitted = email.split(splitRe);
        StringBuilder sb = new StringBuilder();
        //if the list doesn't contain the word, add it to the string, if it does, then don't add to list
        for (int i = 0; i < splitted.length; ++i) {
            if (!values.contains(splitted[i])) {
                values.add(splitted[i]);
                sb.append(' ');
                sb.append(splitted[i]);
            }
        }
        //returns duplicate free version of string
        return sb.substring(1);
    }

    private void countHam(String word) {
        if (trainHamFreq.containsKey(word)) {
            double oldCount = trainHamFreq.get(word);
            trainHamFreq.put(word, oldCount+1);
        } else {
            trainHamFreq.put(word, 1.0);
        }
    }

    private void countSpam(String word) {
        if (trainSpamFreq.containsKey(word)) {
            double oldCount = trainSpamFreq.get(word);
            trainSpamFreq.put(word, oldCount + 1);
        } else {
            trainSpamFreq.put(word, 1.0);
        }
    }

    private boolean isWord(String str){
        String pattern = "^[a-zA-Z]*$";
        if (str.matches(pattern)){
            return true;
        }
        return false;
    }

    public void printWordCounts(int minCount, File outputFile, String folderFrom) throws FileNotFoundException {
        System.out.println("Saving word counts to " + outputFile.getAbsolutePath());
        if (!outputFile.exists() || outputFile.canWrite()) {
            PrintWriter fout = new PrintWriter(outputFile);
            if(folderFrom == "ham") {
                Set<String> keys = trainHamFreq.keySet();
                Iterator<String> keyIterator = keys.iterator();

                while(keyIterator.hasNext()) {
                    String key = keyIterator.next();
                    double count = trainHamFreq.get(key);

                    if (count >= minCount) {
                        //System.out.println(key + ": " + count);
                        fout.println(key + ": " + count);
                    }
                }
            } else if(folderFrom == "spam") {
                Set<String> keys = trainSpamFreq.keySet();
                Iterator<String> keyIterator = keys.iterator();

                while(keyIterator.hasNext()) {
                    String key = keyIterator.next();
                    double count = trainSpamFreq.get(key);

                    if (count >= minCount) {
                        //System.out.println(key + ": " + count);
                        fout.println(key + ": " + count);
                    }
                }
            }
            fout.close();
        } else {
            System.err.println("Cannot write to output file");
        }
    }

    public TreeMap doMath(File file, String classification) {
        hamList = new TreeMap<>();
        spamList = new TreeMap<>();

        //uncomment for these to work
        hamList = trainHamFreq;
        spamList = trainSpamFreq;
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

        TreeMap<String,Double> probInSpamTree;
        TreeMap<String,Double> probInHamTree;
        TreeMap<String,Double> probWordIsSpamTree;

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
            probWordIsSpamTree.put(key, probWordIsSpam);

        }

        //calcs
        try {
            //pass variables needed to to calc here
            processFileforMath(file, probWordIsSpamTree, classification);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //fileSpamProb.put(file.getName(), //spamprob);

        return probWordIsSpamTree;
    }

    public static void main(String[] args, Stage primaryStage) {
        if (args.length < 2) {
            System.err.println("Usage:  java WordCounter <dir> <outputFile>");
            System.exit(0);
        }

        WordCounter wordCounter = new WordCounter();
        File dataDir = new File(args[0]);
        System.out.println("File: " + dataDir);

        try {
            //checks what folder is the program looking at right now
            if(dataDir.getName().contains("ham")) {
                wordCounter.processFile(dataDir,"ham");
                //first parameter is for minimum # of appearances the word needs to be shown on list(ie: 2 = print all
                //values that are appear 2x, ignore all words that appear once)
                wordCounter.printWordCounts(2, new File(args[1]),"ham");
            } else if (dataDir.getName().contains("spam")) {
                wordCounter.processFile(dataDir,"spam");
                //first parameter is for minimum # of appearances the word needs to be shown on list(ie: 2 = print all
                //values that are appear 2x, ignore all words that appear once)
                wordCounter.printWordCounts(2, new File(args[1]),"spam");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
