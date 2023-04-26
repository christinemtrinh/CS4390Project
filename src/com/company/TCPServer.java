package src.com.company;

import java.io.*;
import java.net.*;

class TCPServer {

    public static void main(String argv[]) throws Exception {
        String clientSentence;
        String capitalizedSentence;

        //welcome socket accepts and establishes incoming connection
        ServerSocket welcomeSocket = new ServerSocket(61611);

        while(true) {

            //waits on incoming connection and creates a new socket for it
            Socket connectionSocket = welcomeSocket.accept();
            //create a new thread to handle the new connection
            HandleClient hc = new HandleClient(connectionSocket);
            //start listening to the new connection to handle its requests
            hc.start();

            //Note:Probably don't need to break this loop at any point since server continues until stopped by user.
        }
    }
}
