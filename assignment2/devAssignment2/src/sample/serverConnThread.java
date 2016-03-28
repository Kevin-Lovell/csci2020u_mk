package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

public class serverConnThread implements Runnable {
    private String temp;
    private Socket socket;
    private PrintWriter out;

    public serverConnThread() {
        getUpdate();
    }

    public serverConnThread(String temp) {
        this.temp = temp;
    }

    private BufferedReader in;
    public void run() {
        // get the host name
        String hostName = "localhost";

        // get the port number, if there is one
        int portNumber = 8080;

        try {
            // connect to the server (3-way connection establishment handshake)
            URL url = new URL("http://" + hostName + ":" + portNumber + "/yahoo/yahoo.html");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(false);
            conn.setDoInput(true);

            // read and print the response
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response;
            System.out.println("Response:");
            while ((response = in.readLine()) != null) {
                System.out.println(response);
            }

            // close the connection (3-way tear down handshake)
            in.close();
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e2) {}
        }
    }

    public String getUpdate() {
        String response = "";
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

            // send the HTTP request GET yahoo/yahoo.html HTTP/1.0\n\n
            //String request = "GET /yahoo/yahoo.html HTTP/1.0";
            String request = "GET updateMePLS42";
            String delim = "\r\n";
            out.print(request + delim + delim);
            out.flush();

            // read and print the response
            System.out.println("Response:");
            while ((response = in.readLine()) != null) {
                System.out.println(response);
            }
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