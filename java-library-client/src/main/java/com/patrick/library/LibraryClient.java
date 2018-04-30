package com.patrick.library;

import java.io.*;
import java.net.*;

public class LibraryClient
{
    public static void main(String[] args) throws IOException
    {
        if (args.length != 2)
        {
            System.err.println("Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try (Socket socket = new Socket(host, port);
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream())))
        {
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromClient;
            boolean isISBN = true;

            while ((fromServer = input.readLine()) != null)
            {
                System.out.println(fromServer);

                if (isISBN)
                {
                    fromClient = stdIn.readLine();

                    if (fromClient != null)
                    {
                        output.println(fromClient);
                        System.out.println();
                    }

                    isISBN = false;
                }

                if (fromServer.substring(0, 9).equals("Language:") || fromServer.substring(0, 6).equals("Error:"))
                {
                    System.out.println();
                    output.println();
                    isISBN = true;
                }
            }
        }
        catch (UnknownHostException e)
        {
            System.err.println("Don't know about host " + host);
            System.exit(1);
        }
        catch (IOException e)
        {
            System.err.println("Couldn't get I/O for the connection to " + host);
            System.exit(1);
        }
    }
}