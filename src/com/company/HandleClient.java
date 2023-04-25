package com.company;

import java.io.*;
import java.net.*;


public class HandleClient extends Thread {
    Socket clientSocket;
    HandleClient(Socket s) {
        this.clientSocket = s;
    }

    //This method {waits on a request from the client, fulfills the request} and repeats until stopped by client
    public void run() {
        System.out.println("Run started");
        String clientRequest;
        BufferedReader inFromClient;
        DataOutputStream outToClient;

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
        System.out.println("Thread created Streams successfully.");
        //Todo: should read from input stream here, log client name, store current time, and send acknowledgement back
        while (true) {
            try{

                clientRequest = inFromClient.readLine();
                System.out.println(clientRequest);
                if (clientRequest == null || clientRequest.equals("stop")) {
                    //Todo: Take current time, subtract stored time to find time elapsed
                    //Todo: Log Time elapsed and client name here as well
                    System.out.println("Client disconnected.");
                    return;
                }

                //Note that when replacing this with the real response, you must include a \n at the end or it wont work
                outToClient.writeBytes("Request Received\n");
            } catch (IOException e) {
                System.out.println("Exception thrown while handing a request: " + e);
            }
        }
    }
}
