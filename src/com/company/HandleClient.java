package com.company;

import java.io.*;
import java.net.*;

/* HandleClient is a Thread that listens to a socket and responds to any requests that arrive*/
public class HandleClient extends Thread {

    Socket clientSocket;
    HandleClient(Socket s) {
        this.clientSocket = s;
    }

    //This method repeatedly waits for request and fulfills it until stopped by client
    public void run() {
        String clientRequest;

        //used to read request sent from client
        BufferedReader inFromClient;

        //used to send response back to client
        DataOutputStream outToClient;

        //set up streams
        try {
            inFromClient =
                    new BufferedReader(new
                            InputStreamReader(clientSocket.getInputStream()));
            outToClient =
                    new DataOutputStream(clientSocket.getOutputStream());

        } catch (IOException e) {
            System.out.println("Exception thrown while setting up Input/Output streams: " + e);
            return;
        }

        //Todo: should read from input stream here, log client name, store current time, and send acknowledgement back

        //fulfills requests from clients as they appear until prompted to stop
        while (true) {
            try{

                //waits for and read next request from client
                clientRequest = inFromClient.readLine();

                //if client has disconnected or requests to disconnect, stop listening
                if (clientRequest == null || clientRequest.equals("stop")) {
                    //Todo: Take current time, subtract stored time to find time elapsed
                    //Todo: Log Time elapsed and client name here as well
                    System.out.println("Client disconnected.");
                    return;
                }

                //Todo: Turn this into a proper log of the request
                System.out.println(clientRequest);

                //Note that when replacing this with the real response, you must include a \n at the end or it wont work
                //sends response back to client
                outToClient.writeBytes("Request Received" + "\n");

            } catch (IOException e) {
                System.out.println("Exception thrown while handing a request: " + e);
            }
        }
    }
}
