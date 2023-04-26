package src.com.company;

import java.io.*;
import java.net.*;

class TCPClient {

    public static void main(String argv[]) throws Exception
    {
        //mathematical calculation inputted by user
        String request;
        //mathematical answer received from Math server
        String response;

        //used to read input from user
        BufferedReader inFromUser =
                new BufferedReader(new InputStreamReader(System.in));

        //establish connection with server
        Socket clientSocket = new Socket("127.0.0.1", 6789);

        //used to send requests to server
        DataOutputStream outToServer =
                new DataOutputStream(clientSocket.getOutputStream());

        //used to receive responses from server
        BufferedReader inFromServer =
                new BufferedReader(new
                        InputStreamReader(clientSocket.getInputStream()));

        //read math expression from user
        request = inFromUser.readLine();

        //send expression to the Math server
        outToServer.writeBytes(request + '\n');

        //receive answer from the Math server
        response = inFromServer.readLine();

        //print answer
        System.out.println("FROM SERVER: " + response);

        //closes connection
        clientSocket.close();
    }
}
