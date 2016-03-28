package server;

import java.io.*;
import java.net.*;

public class HttpClient2 {
    public static void main(String[] args) {
        Socket socket;
        BufferedReader in;
        PrintWriter out;

        if (args.length == 0) {
            System.out.println("Usage:  java HttpClient2 <host> [<port>=80]");
            System.exit(0);
        }

        // get the host name
        String hostName = args[0];

        // get the port number, if there is one
        int portNumber = 80;
        if (args.length > 1) {
            portNumber = Integer.parseInt(args[1]); 
        }

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
        }
    }
}

