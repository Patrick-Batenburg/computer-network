package com.patrick.library.main;

import com.patrick.library.logic.LibraryProtocol;
import java.net.*;
import java.io.*;

public class LibraryServer
{
    public static void main(String[] args)
    {
        int port = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(port);
            Socket clientSocket = serverSocket.accept();
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())))
        {
            String inputLine, outputLine;

            // Initiate conversation with client
            LibraryProtocol libraryProtocol = new LibraryProtocol();
            outputLine = libraryProtocol.processInput(null);
            output.println(outputLine);

            while ((inputLine = input.readLine()) != null)
            {
                outputLine = libraryProtocol.processInput(inputLine);
                output.println(outputLine);

                if (outputLine.equals("Bye."))
                {
                    break;
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Exception caught when trying to listen on port " + port + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}