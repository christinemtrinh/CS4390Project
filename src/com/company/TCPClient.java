package com.company;

import java.io.*;
import java.net.*;

class TCPClient {

    public static void main(String argv[]) throws Exception
    {
        //mathematical calculation inputted by user
        String request;

        //mathematical answer received from Math server
        String response;
	
	//read user's self-identification
	if (argv.length < 1) {
		throw new Exception("\n\tExpected one parameter. Usage: make client <identifier>");
	} 

	System.out.println("Logged in as: " + argv[0]);
	System.out.println("Send a simple math calculation in prefix form, or 'stop' to exit");
        //used to read input from user
        BufferedReader inFromUser =
                new BufferedReader(new InputStreamReader(System.in));


	//check if server is running

	try {
	        //establish connection with server
        	Socket clientSocket = new Socket("127.0.0.1", 61618);

	        //used to send requests to server
	        DataOutputStream outToServer =
	                new DataOutputStream(clientSocket.getOutputStream());

	        //used to receive responses from server
        	BufferedReader inFromServer =
                	new BufferedReader(new
                        	InputStreamReader(clientSocket.getInputStream()));

		//send user ID to server
		outToServer.writeBytes(argv[0] + '\n');

		//read math expression from user
        	request = inFromUser.readLine();

		while (!(request.equals("stop") || request == null)) {
		    //send expression to the Math server
           	    outToServer.writeBytes(request + '\n');

	            //receive answer from the Math server
         	    response = inFromServer.readLine();

	            //print answer
        	    System.out.println("FROM SERVER: " + response);

		    //ask for next question
		    request = inFromUser.readLine();
		}
	        //closes connection
        	clientSocket.close();
	} catch (UnknownHostException uhe) {
		uhe.printStackTrace();
	} catch (IOException ioe) {
		ioe.printStackTrace();
	}
    }

}
