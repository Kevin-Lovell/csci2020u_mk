package sample;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;


public class serverConnThread implements Runnable {
    private String hostName, temp;
    private int portNumber;
    private Socket socket;
    private PrintWriter out;
    private String[] fileArray;
    private ObservableList<String> items;
    private ListView<String> list;
    private BufferedReader in;

    public serverConnThread(ObservableList<String> itemsFromMain, ListView<String> listFromMain) {
        this.items = itemsFromMain;
        this.list = listFromMain;
        getUpdate();
    }

    public serverConnThread() {

    }

    private String getContentType(String filename) {
        if (filename.endsWith(".html") || filename.endsWith(".htm")) {
            return "text/html";
        } else if (filename.endsWith(".txt")) {
            return "text/plain";
        } else if (filename.endsWith(".css")) {
            return "text/css";
        } else if (filename.endsWith(".js")) {
            return "text/javascript";
        } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (filename.endsWith(".gif")) {
            return "image/gif";
        } else if (filename.endsWith(".png")) {
            return "image/png";
        }
        return "unknown";
    }

    public void uploadFile(File file)throws IOException {
        String header = "POST /upload\r\n";
        String contentType = getContentType(file.getName());
        String fileName = file.getName();
        byte[] content = new byte[(int)file.length()];
        FileInputStream fileInput = null;

        try {
            fileInput = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            fileInput = null;
        }

        if (fileInput != null) {
            fileInput.read(content);
            fileInput.close();
            sendResponse(header, contentType, content, fileName);
        } else {
            sendError(404, "File Not Found",
                    "The requested file ("+file.getName()+") does not exist on the server.", fileName);
        }
    }

    private void sendResponse(String header, String contentType,
                              byte[] content, String fileName) throws IOException {
        sendResponse(header, contentType, content, fileName,0);
    }

    private void sendResponse(String header, String contentType,
                              byte[] content, String fileName, long lastModified) throws IOException {

        Platform.runLater(new Runnable() {
            @Override public void run() {
                try {
                    Socket socket = new Socket("localhost", 8080);
                    DataOutputStream requestOutput = new DataOutputStream(socket.getOutputStream());
                    requestOutput.writeBytes(header);
                    requestOutput.writeBytes("Content-Type: "+contentType+"\r\n");
                    requestOutput.writeBytes("Server: Simple-Http-Client/1.0\r\n");
                    requestOutput.writeBytes("Date: "+(new Date())+"\r\n");
                    requestOutput.writeBytes("Content-Length: "+content.length+"\r\n");
                    if (lastModified > 0) {
                        requestOutput.writeBytes("Last-Modified: "+(new Date(lastModified))+"\r\n");
                    }
                    requestOutput.writeBytes("Connection: Close\r\n\r\n");
                    requestOutput.writeBytes(fileName + "\r\n");
                    requestOutput.write(content);
                    requestOutput.writeBytes("\r\n");
                    requestOutput.flush();
                    try {
                        requestOutput.close();
                        socket.close();
                    } catch (IOException e2) {}


                } catch (IOException e) {
                    System.err.println("Server Error while processing new socket\r\n");
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendError(int code, String title, String message, String fileName) throws IOException {
        String header = "HTTP/1.1 "+code+" "+title+"\r\n";
        String contentType = "text/html";
        String content = "<!DOCTYPE html>" +
                "<html>\r\n" +
                "  <head>\r\n" +
                "    <title>Http Error "+code+" "+title+"</title>\r\n" +
                "  </head>\r\n" +
                "  <body>\r\n" +
                "    <h1>Http Error "+code+" "+title+"</h1>\r\n" +
                "    <p>"+message+"</p>\r\n" +
                "  </body>\r\n" +
                "</html>\r\n";
        sendResponse(header, contentType,content.getBytes(), fileName);
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

    boolean notInterrupted() {
        if(Thread.interrupted()) {
            return false;
        }
        return true;
    }

    public void run() {
        // get the host name
        this.hostName = "localhost";

        // get the port number, if there is one
        this.portNumber = 8080;

        while(notInterrupted()) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    getUpdate();
                }
            });
            try {
                Thread.sleep(10000);

            } catch (InterruptedException ex) {
                break;
            }
        }
    }

    public String getUpdate() {
        String response = "";
        String hostName = "localhost";
        list.getItems().clear();

        int portNumber = 8080;

        try {
            // connect to the server (3-way connection establishment handshake)
            socket = new Socket(hostName, portNumber);
            // wrap the input streams into readers and writers
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            String request = "PUT /update";
            String delim = "\r\n";
            out.print(request + delim + delim);
            out.flush();

            // read and print the response
            while ((response = in.readLine()) != null) {
                fileArray = response.split("----------");
            }

            for(int i = 0; i < fileArray.length; i++) {
                //System.out.println(fileArray[i]);
                items.add(fileArray[i]);
            }
            list.setItems(items);
        } catch (IOException e) {

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