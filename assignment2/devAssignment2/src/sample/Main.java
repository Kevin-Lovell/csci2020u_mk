package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;


public class Main extends Application {
    private Stage window;
    private BorderPane layout;
    private TableView<Document> table;
    private TextField sidField, fnameField, lnameField, gpaField;
    String FileToDownload, FileToUpload;


    @Override
    public void start(Stage primaryStage) throws Exception {

        /*This code will be used in the future to to read the files into the table.
        We may not need the DataSource.java and Document.java files with this code.
        We also may need two tables side-by-side because this code reads all of the files
        in a directory into a single table*/

        ListView<String> list = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList();

        ListView<String> listServer = new ListView<String>();
        ObservableList<String> itemsServer = FXCollections.observableArrayList();

        serverConnThread serverConnThread = new serverConnThread(itemsServer,listServer);
        Thread sCT = new Thread(serverConnThread);
        sCT.start();
        sCT.interrupt();

        File folder = new File("clientFiles/");

        File[] files = folder.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                //System.out.println(file.getName());
                items.add(file.getName());
            }
        }

        list.setItems(items);


        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("clicked on " + list.getSelectionModel().getSelectedItem());
                FileToUpload = list.getSelectionModel().getSelectedItem();
                System.out.println (FileToUpload);
            }
        });


        listServer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("clicked on " + listServer.getSelectionModel().getSelectedItem());
                FileToDownload = listServer.getSelectionModel().getSelectedItem();
                System.out.println (FileToDownload);
            }
        });

        primaryStage.setTitle("File Sharer v1.0");

        //connect to server

        //get updates of files
        //get a list of files on server to pass to the data source
        //data source prints the files onto the right list
        //show User sharing dictionary on left list

        /* create an edit form (for the bottom of the user interface) */
        GridPane editArea = new GridPane();


        Button downloadButton = new Button("Download");
        //get name of selected listview file

        downloadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                //send download request
                serverConnThread serverConnThread = new serverConnThread();
                String DLContents = serverConnThread.downloadFile(FileToDownload);
                System.out.println(DLContents);
//                Thread sCT = new Thread(serverConnThread);
//                sCT.start();
//                sCT.interrupt();

                try {
                    PrintWriter writer = new PrintWriter("clientFiles/" + FileToDownload, "UTF-8");
                    writer.println(DLContents);
                    writer.close();
                }
                catch(FileNotFoundException ex) {
                    System.out.println("Unable to open file '" + FileToDownload + "'");
                }
                catch(IOException ex) {
                    System.out.println("Error reading file '" + FileToDownload + "'");
                }


                list.getItems().clear();

                //items.add(FileToDownload);

                File folder = new File("clientFiles/");

                File[] files = folder.listFiles();

                for (File file : files) {
                    if (file.isFile()) {
                        //System.out.println(file.getName());
                        items.add(file.getName());
                    }
                }

            }
        });
        editArea.add(downloadButton, 0, 0);


        Button updateButton = new Button("Update");
        updateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                //send update request
                serverConnThread serverConnThread = new serverConnThread(itemsServer,listServer);
                Thread sCT = new Thread(serverConnThread);
                sCT.start();
                sCT.interrupt();
            }
        });
        editArea.add(updateButton, 2, 0);

        Button uploadButton = new Button("Upload");
        uploadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                //send upload request

                System.out.println("Uploaded");
            }
        });
        editArea.add(uploadButton, 1, 0);

        /* arrange all components in the main user interface*/
        layout = new BorderPane();
        layout.setTop(editArea);
        layout.setLeft(list);
        layout.setRight(listServer);

        //table.getStylesheets().addAll(getClass().getResource("hidden-tableview-headers.css").toExternalForm());

        Scene scene = new Scene(layout, 500, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void connectToServer() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
