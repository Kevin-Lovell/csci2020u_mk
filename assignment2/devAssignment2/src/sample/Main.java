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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.*;
import java .net.*;

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

        //connect to server

        //get updates of files
        //get a list of files on server to pass to the data source
        //data source prints the files onto the right list


        //show User sharing dictionary on left list







        /* create the table (for the center of the user interface) */
        table = new TableView<>();
        table.setItems(DataSource.getAllDocuments());
        table.setEditable(true);

        TableColumn<Document,String> clientFileColumn = null;
        clientFileColumn = new TableColumn<>();
        clientFileColumn.setMinWidth(300);
        clientFileColumn.setCellValueFactory(new PropertyValueFactory<>("clientFile"));
        clientFileColumn.setCellFactory(TextFieldTableCell.<Document>forTableColumn());
        clientFileColumn.setOnEditCommit((CellEditEvent<Document, String> event) -> {
            ((Document)event.getTableView().getItems().get(event.getTablePosition().getRow())).setClientFile(event.getNewValue());
        });

        table.getColumns().add(clientFileColumn);

        TableColumn<Document,String> serverFileColumn = null;
        serverFileColumn = new TableColumn<>();
        serverFileColumn.setMinWidth(300);
        serverFileColumn.setCellValueFactory(new PropertyValueFactory<>("serverFile"));
        serverFileColumn.setCellFactory(TextFieldTableCell.<Document>forTableColumn());
        serverFileColumn.setOnEditCommit((CellEditEvent<Document, String> event) -> {
            ((Document)event.getTableView().getItems().get(event.getTablePosition().getRow())).setServerFile(event.getNewValue());
        });

        table.getColumns().add(serverFileColumn);

        /* create an edit form (for the bottom of the user interface) */
        GridPane editArea = new GridPane();

        Button downloadButton = new Button("Download");
        downloadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                //send download request

                //String firstName = fnameField.getText();
                //table.getItems().add(new Student(0, firstName, a, 0));
                //fnameField.setText("");
            }
        });
        editArea.add(downloadButton, 0, 0);

        Button uploadButton = new Button("Upload");
        uploadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                //send upload request

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


    public void connectToServer() {

    }


    public static void main(String[] args) {
        launch(args);
    }
}
