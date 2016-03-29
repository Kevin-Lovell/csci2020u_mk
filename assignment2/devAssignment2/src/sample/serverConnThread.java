package sample;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

public class serverConnThread implements Runnable {
    private String hostName, temp;
    private int portNumber;
    private Socket socket;
    private PrintWriter out;
    private String[] fileArray;

    public serverConnThread(ObservableList<String> items, ListView<String> list) {
            getUpdate(items, list);
    }

    //download constructor
    public serverConnThread() {

    }

    public String downloadFile(String fileName) {
        String content = "";
        try {
            // connect to the server (3-way connection establishment handshake)
            URL url = new URL("http://localhost:8080/" + fileName);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(false);
            conn.setDoInput(true);



            // read and print the response
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response;
            System.out.println("Response:");
            while ((response = in.readLine()) != null) {
                content = content + "\n" + response ;
            }

        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e2) {}
        }
        return content;
    }

    private BufferedReader in;
    public void run() {
        // get the host name
        this.hostName = "localhost";

        // get the port number, if there is one
        this.portNumber = 8080;

        if(Thread.interrupted()) {
            System.out.println("interrupted");
            return;
        }


    }

    public String getUpdate(ObservableList<String> items, ListView<String> list) {
        String response = "";
        // get the host name
        String hostName = "localhost";
        list.getItems().clear();

        // get the port number, if there is one
        int portNumber = 8080;

        try {
            // connect to the server (3-way connection establishment handshake)
            socket = new Socket(hostName, portNumber);

            // wrap the input streams into readers and writers
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            // send the HTTP request GET yahoo/yahoo.html HTTP/1.0\n\n
            //String request = "GET /yahoo/yahoo.html HTTP/1.0";
            String request = "GET updateMePLS42";
            String delim = "\r\n";
            out.print(request + delim + delim);
            out.flush();


            // read and print the response
            System.out.println("Response:");
            while ((response = in.readLine()) != null) {
                fileArray = response.split("----------");
            }

            for(int i = 0; i < fileArray.length; i++) {
                System.out.println(fileArray[i]);
                items.add(fileArray[i]);
            }
            list.setItems(items);
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e2) {}
        }

        return response;
    }
}