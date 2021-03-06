package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Main extends Application {
    private Stage window;
    private BorderPane layout;
    private TableView<TestFile> table;
    private TextField accuracyField, precisionField;
    private double accuracy, precision;

    private WordCounter wordCounter;

    public void processFolderTest(File file, String folderFrom, WordCounter counter) throws IOException {
        if (file.isDirectory()) {
            if(file.getName().contains("ham")) {
                folderFrom = "ham";
            } else if(file.getName().contains("spam")) {
                folderFrom = "spam";
            }
            // process all of the files recursively
            File[] filesInDir = file.listFiles();
            for (int i = 0; i < filesInDir.length; i++) {
                if(folderFrom == "ham") {
                    processFolderTest(filesInDir[i], "ham", counter);
                } else if(folderFrom == "spam") {
                    processFolderTest(filesInDir[i],  "spam", counter);
                }
                processFolderTest(filesInDir[i], "", counter);
                System.out.println(i);
            }
        } else if (file.exists()) {
            if(folderFrom == "ham") {
                counter.processFileforMath(file, counter.getProbWordIsSpamTree(), "ham");
            } else if(folderFrom == "spam") {
                counter.processFileforMath(file, counter.getProbWordIsSpamTree(), "spam");
            }

        }

    }

    public void processFolder(File file, String folderFrom, WordCounter counter) throws IOException {
        if (file.isDirectory()) {
            File[] filesInDir = file.listFiles();
            //checks what folder is the program looking at right now
            if(file.getName().equals("ham")) {
                counter.processFile(file,"ham");

                counter.doMath();
            } else if (file.getName().contains("spam")) {
                counter.processFile(file,"spam");
                counter.doMath();
            } else if(file.getName().contains("ham2")) {
                counter.processFile(file,"ham");
                counter.doMath();
            }

            //process all of the files recursively
            for (int i = 0; i < filesInDir.length; i++) {

                processFolder(filesInDir[i], folderFrom, counter);
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        File mainDirectory = directoryChooser.showDialog(primaryStage);

        wordCounter = new WordCounter();
        System.out.println(mainDirectory.toPath());

        String trainPath = "train";
        String testPath = "test";
        File test = new File(mainDirectory + File.separator+ testPath);
        File train = new File(mainDirectory + File.separator+ trainPath);
        try {
            processFolder(train, "train", wordCounter);
            wordCounter.doMath();
            processFolderTest(test, "", wordCounter);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage.setTitle("Spam Master 3000");

        table = new TableView<>();
        table.setItems(DataSource.getAllData(wordCounter.getFileSpamProb(),wordCounter.getActClass()));
        table.setEditable(true);

        TableColumn<TestFile,String> fileColumn = null;
        fileColumn = new TableColumn<>("File");
        fileColumn.setMinWidth(200);
        fileColumn.setCellValueFactory(new PropertyValueFactory<>("filename"));

        TableColumn<TestFile,Double> actualClassColumn = null;
        actualClassColumn = new TableColumn<>("Actual Class");
        actualClassColumn.setMinWidth(80);
        actualClassColumn.setCellValueFactory(new PropertyValueFactory<>("actualClass"));

        TableColumn<TestFile,String> spamProbabilityColumn = null;
        spamProbabilityColumn = new TableColumn<>("Spam Probability");
        spamProbabilityColumn.setMinWidth(200);
        spamProbabilityColumn.setCellValueFactory(new PropertyValueFactory<>("spamProbability"));

        TableColumn<TestFile,String> categorizationColumn = null;
        categorizationColumn = new TableColumn<>("Categorization");
        categorizationColumn.setMinWidth(80);
        categorizationColumn.setCellValueFactory(new PropertyValueFactory<>("categorization"));

        table.getColumns().add(fileColumn);
        table.getColumns().add(actualClassColumn);
        table.getColumns().add(spamProbabilityColumn);
        table.getColumns().add(categorizationColumn);

        /* create an edit form (for the bottom of the user interface) */
        GridPane editArea = new GridPane();
        editArea.setPadding(new Insets(10, 10, 10, 10));
        editArea.setVgap(10);
        editArea.setHgap(10);

        //accuracy = number of correct guesses / total number of files;
        //precision = number of correct guesses / number of spam guesses;

        DataSource dat = new DataSource();

        Label accuracyLabel = new Label("Accuracy:");
        editArea.add(accuracyLabel, 0, 0);
        TextField accuracyField = new TextField();
        accuracyField.setText(String.valueOf(dat.finalAccuaracy));
        accuracyField.setPrefWidth(200);
        editArea.add(accuracyField, 1, 0);

        Label precisionLabel = new Label("Precision:");
        editArea.add(precisionLabel, 0, 1);
        TextField precisionField = new TextField();
        precisionField.setText(String.valueOf(dat.finalPrecision));
        precisionField.setPrefWidth(200);
        editArea.add(precisionField, 1, 1);

        /* arrange all components in the main user interface */
        layout = new BorderPane();
        layout.setCenter(table);
        layout.setBottom(editArea);

        Scene scene = new Scene(layout, 800, 900);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ImageView imageFile(String filename) {
        return new ImageView(new Image("file:"+filename));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
