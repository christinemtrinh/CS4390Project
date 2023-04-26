package com.company;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.text.SimpleDateFormat;

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

	//Handle new connection: Save client name + current time
	Date connectionStartTime  = new Date();	
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	String clientID = null;

	try {
	    //Have server read client identifier from buffer then save for future logging 
	    String userID  = inFromClient.readLine();
	    clientID = userID;

	    //Log new connection details
	    String startLog = formatter.format(connectionStartTime) + ": " + clientID + " has connected";
	    System.out.println(startLog);

	} catch (IOException e) {
	    System.out.println("Exception thrown while loggin a new connection: " + e);
	}


        //fulfills requests from clients as they appear until prompted to stop
        while (true) {
            try{

                //waits for and read next request from client
                clientRequest = inFromClient.readLine();
		Date reqRecTime = new Date();

                //if client has disconnected or requests to disconnect, do end session protocols
                if (clientRequest == null || clientRequest.equals("stop")) {
                    //Todo: Log Time elapsed and client name here as well

		    //Save time of quit and find elapsed time
		    Date connectionEndTime = new Date();
		    String endLog = formatter.format(connectionEndTime);
		    long deltaTime = Math.abs(connectionEndTime.getTime() - connectionStartTime.getTime());
		    long timeSec = TimeUnit.SECONDS.convert(deltaTime, TimeUnit.MILLISECONDS);

		    //Log details of session end
                    System.out.println(endLog + ": " + clientID + " disconnected");
		    System.out.println("\t  Session length (s): " + timeSec);
                    return;
                }

                //NOT end of session, log request
                System.out.println(formatter.format(reqRecTime) + ": " + clientID + " made a request.");
		System.out.println("\t  Req: " + clientRequest);

		//Handle request
                //Note that when replacing this with the real response, you must include a \n at the end or it wont work
                //sends response back to client
                outToClient.writeBytes("Request Received" + "\n");

            } catch (IOException e) {
                System.out.println("Exception thrown while handing a request: " + e);
            }
        }
    }
}
