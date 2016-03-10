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
import java.util.Map;
import java.util.TreeMap;

public class Main extends Application {
    private Stage window;
    private BorderPane layout;
    private TableView<TestFile> table;
    private TextField accuracyField, precisionField;
    private double accuracy, precision;

    @Override
    public void start(Stage primaryStage) throws Exception {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        File mainDirectory = directoryChooser.showDialog(primaryStage);

        WordCounter wordCounter = new WordCounter();
        System.out.println("File: " + mainDirectory);

        try {
            //checks what folder is the program looking at right now
            if(mainDirectory.getName().contains("ham")) {
                wordCounter.processFile(mainDirectory,"ham");
                //first parameter is for minimum # of appearances the word needs to be shown on list(ie: 2 = print all
                //values that are appear 2x, ignore all words that appear once)
                //wordCounter.printWordCounts(2, new File("countOutput.txt"),"ham");
                System.out.println(wordCounter.trainHamFreq);
            } else if (mainDirectory.getName().contains("spam")) {
                wordCounter.processFile(mainDirectory,"spam");
                //first parameter is for minimum # of appearances the word needs to be shown on list(ie: 2 = print all
                //values that are appear 2x, ignore all words that appear once)
                //wordCounter.printWordCounts(2, new File("countOutput.txt"),"spam");
                System.out.println(wordCounter.trainSpamFreq);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String,Double> cloned;
        cloned=(TreeMap)((TreeMap)wordCounter.trainSpamFreq).clone();
        System.out.println("Cloned map: "+ cloned);

        primaryStage.setTitle("Spam Master 3000");

        table = new TableView<>();
        table.setItems(DataSource.getAllData());
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

        Label accuracyLabel = new Label("Accuracy:");
        editArea.add(accuracyLabel, 0, 0);
        TextField accuracyField = new TextField();
        accuracyField.setText("calculated accuracy");
        accuracyField.setPrefWidth(200);
        editArea.add(accuracyField, 1, 0);

        Label precisionLabel = new Label("Precision:");
        editArea.add(precisionLabel, 0, 1);
        TextField precisionField = new TextField();
        precisionField.setText("calculated precision");
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
