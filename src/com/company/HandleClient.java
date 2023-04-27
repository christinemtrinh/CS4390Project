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
	    outToClient.writeBytes("Connection established\n");
	    clientID = userID;

	    //Log new connection details
	    String startLog = formatter.format(connectionStartTime) + ": " + clientID + " has connected";
	    System.out.println(startLog);

	} catch (IOException e) {
	    System.out.println("Exception thrown while logging a new connection: " + e);
	}


        //fulfills requests from clients as they appear until prompted to stop
        while (true) {
            try{

                //waits for and read next request from client
                clientRequest = inFromClient.readLine();

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

	    //Handle request
	    String numberRead = "";     // Temporarily stores numbers extracted from the client request
            int firstNum=0;               // First number in the calculation
	    int secondNum=0;              // Second number in the calculation
	    int result=0;                 // Result of the calculation
            char operator;              // Operator found in the client request
	    boolean goodexp = true;
     	   // Note: The only valid expressions accepted are those that have a valid operator for the first
	   // character followed by any number of digits, then a single space followed by any number of digits.
        // Any non-digit characters detected beyond the operator at the beginning and the single space separating
        // the two numbers will result in an error

        // Example: |+813 9184| (Bars indicate bounds of client request line)

        // Extracts operator from client request (Assumes prefix notation)
        operator = clientRequest.charAt(0);

        // Checks if a valid operator is found in the first character of the request string
        if ( operator == '+' ||
             operator == '-' ||
             operator == '*' ||
             operator == '/' ||
             operator == '%')
        {

            // Adds integer digits in string buffer until first space found
            int index = 1;
            while (clientRequest.charAt(index) != ' ' && goodexp) {
                // Returns error if end of string buffer before first space 
                // (i.e., only one valid number found)
                if (index == clientRequest.length()) {
		    goodexp = false;
		}

                // Returns an error if non-integer character detected
                if (clientRequest.charAt(index) < 48 || clientRequest.charAt(index) > 57) {
		    goodexp = false;
		}

                // Adds integer character to the temporary string
                numberRead += clientRequest.charAt(index);
                index++;
            }

            // Converts string of integer digits into an integer for storage in firstNum
	    if(numberRead.length() > 0 && goodexp) {
                firstNum = Integer.parseInt(numberRead);
	    }
	    else {
		goodexp = false; 
	    }

            // Increments the index of the clientRequest and resets numberRead for the second number
            index++;
            numberRead = "";

            // Checks if there is still input remaining in buffer (i.e., something exists beyond the first space)
            if (index == clientRequest.length()) {
		goodexp = false;
            }

            // Reads integer digits until the end of the client request is reached
            while (index < clientRequest.length() && goodexp)
            {
                // Returns an error if non-integer character detected
                if (clientRequest.charAt(index) < 48 || clientRequest.charAt(index) > 57)
                {
		    goodexp = false;
                }

                // Adds integer character to the temporary string
                numberRead += clientRequest.charAt(index);
                index++;
            }

            // Converts string of integer digits into an integer for storage in secondNum
	    if (numberRead.length() > 0 && goodexp) {
                secondNum = Integer.parseInt(numberRead);
	    }
	    else {
		goodexp = false;
	    }

            // Perform calculation based on operator detected
	    if (goodexp) {
	        if (operator == '+')
        	        result = firstNum + secondNum;
            
            	else if (operator == '-')
                	result = firstNum - secondNum;
            
                else if (operator == '*')
                	result = firstNum * secondNum;
            
                else if (operator == '/')
                	result = firstNum / secondNum;

                else
                	result = firstNum % secondNum;
	    }
            
        }
        
        // Returns error if invalid first character detected (i.e., not a valid operator)
        else {
	    goodexp = false;
        }
	if (goodexp) {
            outToClient.writeBytes("\t= " + result + "\n");
	    System.out.println(formatter.format(new Date()) + ": " + clientID + " requested " + clientRequest + ", " + result + " sent");
	}
	else {
	    outToClient.writeBytes("\tBad request. Try again." + "\n");
	    System.out.println(formatter.format(new Date()) + ": " + clientID + " requested " + 
clientRequest + ", Error message sent.");
	}
            } catch (IOException e) {
                System.out.println("Exception thrown while handling a request: " + e);
            }
        }
    }
}
