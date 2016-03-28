package sample;

import javafx.collections.*;

public class DataSource {



    public static ObservableList<Document> getAllDocuments() {
        ObservableList<Document> documents = FXCollections.observableArrayList();

        //these are simply placeholder items
        //the table will be populated with the list of files from the client and server
        documents.add(new Document( "client.txt", "Main.java"));
        documents.add(new Document("", "server.txt"));

        return documents;
    }
}