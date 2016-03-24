package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {
    private Stage window;
    private BorderPane layout;
    private TableView<Document> table;
    private TextField sidField, fnameField, lnameField, gpaField;

    @Override
    public void start(Stage primaryStage) throws Exception {

        /*This code will be used in the future to to read the files into the table.
        We may not need the DataSource.java and Document.java files with this code.
        We also may need two tables side-by-side because this code reads all of the files
        in a directory into a single table*/

        //TableView<File> table = new TableView<>();
        //File dir = ... ;
        //table.getItems().addAll(dir.listFiles());

        primaryStage.setTitle("File Sharer v1.0");

        /* create the table (for the center of the user interface) */
        table = new TableView<>();
        table.setItems(DataSource.getAllDocuments());
        table.setEditable(true);

        TableColumn<Document,String> firstNameColumn = null;
        firstNameColumn = new TableColumn<>();
        firstNameColumn.setMinWidth(300);
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("clientFile"));
        firstNameColumn.setCellFactory(TextFieldTableCell.<Document>forTableColumn());
        firstNameColumn.setOnEditCommit((CellEditEvent<Document, String> event) -> {
            ((Document)event.getTableView().getItems().get(event.getTablePosition().getRow())).setClientFile(event.getNewValue());
        });

        table.getColumns().add(firstNameColumn);

        TableColumn<Document,String> lastNameColumn = null;
        lastNameColumn = new TableColumn<>();
        lastNameColumn.setMinWidth(300);
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("serverFile"));
        lastNameColumn.setCellFactory(TextFieldTableCell.<Document>forTableColumn());
        lastNameColumn.setOnEditCommit((CellEditEvent<Document, String> event) -> {
            ((Document)event.getTableView().getItems().get(event.getTablePosition().getRow())).setServerFile(event.getNewValue());
        });

        table.getColumns().add(lastNameColumn);

        /* create an edit form (for the bottom of the user interface) */
        GridPane editArea = new GridPane();
      
        Button downloadButton = new Button("Download");
        downloadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                //String firstName = fnameField.getText();
                //table.getItems().add(new Student(0, firstName, a, 0));
                //fnameField.setText("");
            }
        });
        editArea.add(downloadButton, 0, 0);

        Button uploadButton = new Button("Upload");
        uploadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                //String firstName = fnameField.getText();
                //table.getItems().add(new Student(0, firstName, a, 0));
                //fnameField.setText("");
            }
        });
        editArea.add(uploadButton, 1, 0);

        /* arrange all components in the main user interface */
        layout = new BorderPane();
        layout.setTop(editArea);
        layout.setCenter(table);

        table.getStylesheets().addAll(getClass().getResource("hidden-tableview-headers.css").toExternalForm());

        Scene scene = new Scene(layout, 600, 600);
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
