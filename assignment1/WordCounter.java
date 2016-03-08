import java.io.*;
import java.util.*;

public class WordCounter {
    private Map<String,Integer> trainHamFreq;
	private Map<String,Integer> trainSpamFreq;

    public WordCounter() {
        trainHamFreq = new TreeMap<>();
		
    }
    
    public void processFile(File file) throws IOException {
        if (file.isDirectory()) {
            // process all of the files recursively
            File[] filesInDir = file.listFiles();
            for (int i = 0; i < filesInDir.length; i++) {
                processFile(filesInDir[i]);
break;
            }
        } else if (file.exists()) {
            // load all of the data, deletes duplicates and process it into words
            Scanner scanner = new Scanner(file);
			
            String email = "";
            while (scanner.hasNext()) {
				String word = scanner.next();
                if ((isWord(word))) {
                    email = email + " " + word;
					//trainHamFreq(word);
                }
				 email = removeDuplicates(email, "\\ ");
            }
		    System.out.println(email);
           /*
			String sArray[] = email.split(" ");
			int j = sArray.length-1;
            do {
                String word = sArray[j];
                System.out.println(word);
				if ((isWord(word))) {
					trainHamFreq(word);
                }
		        j--;
            } while(j != -1);
             */   
        }
    }

    public static String removeDuplicates(String email, String splitRe)
    {
        List<String> values = new ArrayList<String>();
        String[] splitted = email.split(splitRe);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < splitted.length; ++i)
        {
            if (!values.contains(splitted[i]))
            {
                values.add(splitted[i]);
                sb.append(' ');
                sb.append(splitted[i]);
            }
        }
        return sb.substring(1);

    }
    
    private void trainHamFreq(String word) {
        if (trainHamFreq.containsKey(word)) {
			int oldCount = trainHamFreq.get(word);
            trainHamFreq.put(word, oldCount+1);
        } else {
            trainHamFreq.put(word, 1);
        }
    }
    
	 /*    private void countSpam(String word) {
        if (wordCounts.containsKey(word)) {
            int oldCount = wordCounts.get(word);
            wordCounts.put(word, oldCount + 1);
        } else {
            wordCounts.put(word, 1);
        }
    } */
	
    private boolean isWord(String str){
        String pattern = "^[a-zA-Z]*$";
        if (str.matches(pattern)){
            return true;
        }
        return false;   
    }
    
    public void printWordCounts(int minCount, File outputFile) throws FileNotFoundException {
        System.out.println("Saving word counts to " + outputFile.getAbsolutePath());
        if (!outputFile.exists() || outputFile.canWrite()) {
            PrintWriter fout = new PrintWriter(outputFile);
                        
            Set<String> keys = trainHamFreq.keySet();
            Iterator<String> keyIterator = keys.iterator();
            
            while(keyIterator.hasNext()) {
                String key = keyIterator.next();
                int count = trainHamFreq.get(key);
                
                if (count >= minCount) {
                 //   System.out.println(key + ": " + count);
					fout.println(key + ": " + count);
                }
            }
            fout.close();
        } else {
            System.err.println("Cannot write to output file");
        }
    }
    
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage:  java WordCounter <dir> <outputFile>");
            System.exit(0);
        }
        
        WordCounter wordCounter = new WordCounter();
        File dataDir = new File(args[0]);
        System.out.println("File: " + dataDir);

        try {
            wordCounter.processFile(dataDir);
            wordCounter.printWordCounts(2, new File(args[1]));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
