package server;

import java.io.*;
import java.net.*;

public class WebBrowser {
    private String hostname = null;
    private int port = 0;
    
    public WebBrowser(String ip, int port) {
        this.hostname = ip;
        this.port = port;
    }
    
    public void submitQuery(String uri) {
        try {
            Socket socket = new Socket(this.hostname, this.port);
            
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println("GET " + uri + " HTTP/1.0\r\n");
            out.println("Host: " + this.hostname + "\r\n\r\n");
            out.flush();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            
            out.close();
            in.close();
            socket.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        WebBrowser webBrowser = new WebBrowser("127.0.0.1", 8080);
        webBrowser.submitQuery("/index.html");
    }
}
