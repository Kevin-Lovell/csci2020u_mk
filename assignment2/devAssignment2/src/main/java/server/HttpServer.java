package server;

import java.io.*;
import java.net.*;

public final class HttpServer {
   private ServerSocket serverSocket = null;

   public HttpServer(int port) throws IOException {
      serverSocket = new ServerSocket(port);
   }

   public void handleRequests() throws IOException {
      System.out.println("File Sharing System: Ready to handle incoming requests.");

      while(true) {
         Socket socket = serverSocket.accept();
         Thread clientThread = new Thread(new ClientConnectionHandler(socket));
         clientThread.start();
      }
   }

   public static void main(String[] args) {
      int port = 8080;
      if (args.length > 0)
         port = Integer.parseInt(args[0]);
      try {
         HttpServer server = new HttpServer(port);
         server.handleRequests();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
