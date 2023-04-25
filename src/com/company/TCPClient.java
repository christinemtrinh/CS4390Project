package com.company;

import java.io.*;
import java.net.*;
class TCPClient {

    public static void main(String argv[]) throws Exception
    {
        String request;
        String response;

        BufferedReader inFromUser =
                new BufferedReader(new InputStreamReader(System.in));

        Socket clientSocket = new Socket("127.0.0.1", 6789);

        DataOutputStream outToServer =
                new DataOutputStream(clientSocket.getOutputStream());

        BufferedReader inFromServer =
                new BufferedReader(new
                        InputStreamReader(clientSocket.getInputStream()));

        request = inFromUser.readLine();

        outToServer.writeBytes(request + '\n');

        response = inFromServer.readLine();

        System.out.println("FROM SERVER: " + response);

        clientSocket.close();
    }
}
