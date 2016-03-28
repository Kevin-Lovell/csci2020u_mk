package server;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.*;

public class HttpClient {
    public static void main(String[] args) {
        Socket socket;
        BufferedReader in;
        PrintWriter out;

        // get the host name
        String hostName = "localhost";

        // get the port number, if there is one
        int portNumber = 8080;

        try {
            // connect to the server (3-way connection establishment handshake)
            socket = new Socket(hostName, portNumber);

            // wrap the input streams into readers and writers
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            ObservableList<String> items = FXCollections.observableArrayList();

            File folder = new File("www/samples/");
            File[] files = folder.listFiles();

            for (File file : files) {
                if (file.isFile()) {
                    items.add(file.getName());
                    String requests = "GET samples/" + items + " HTTP/1.0";
                    String delim = "\r\n";
                    out.print(requests + delim + delim);
                }
            }


            // send the HTTP request GET /yahoo/yahoo.html HTTP/1.0\n\n

            out.flush();

            // read and print the response
            String response;
            System.out.println("Response:");
            while ((response = in.readLine()) != null) {
                System.out.println(response);
            }

            // close the connection (3-way tear down handshake)
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}

