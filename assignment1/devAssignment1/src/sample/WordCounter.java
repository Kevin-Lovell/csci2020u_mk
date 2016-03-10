package sample;

import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class WordCounter {
    Map<String,Double> trainHamFreq;
    Map<String,Double> trainSpamFreq;

    public WordCounter() {
        trainHamFreq = new TreeMap<>();
        trainSpamFreq = new TreeMap<>();
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
