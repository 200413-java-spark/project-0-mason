package com.github.mason.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Date;
import java.io.File;
import java.nio.file.Files;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;


public class Server
{
    public static void main (String[] args) throws Exception
    {
        Server server = new Server();
        //int portNumber = Integer.parseInt(args[0]);
        int portNumber = 8080;//change to args later
        //create server socket and say that it is running
        final ServerSocket myServer = new ServerSocket(portNumber);//change portNumber to args later
        System.out.println("I have Connected To Port " + portNumber);

        boolean running = true;
        while(running)
        {
            //See if anyone connects
            try(Socket client = myServer.accept())
            {                    
                server.sendPage(client);
            }
            catch(IOException e)
            {
                System.out.println("Something went wrong streaming the page");
                System.exit(1);
            }
        }
        try
        {
            myServer.close();
        }
        finally
        {
            System.out.println("Server Is now closed");
        }        
    }


    private void sendPage(Socket client) throws Exception
    {
        System.out.println("Page writter called");

        //Open the indicated file
        File page = new File("index.html");

        PrintWriter pageWriter = new PrintWriter(client.getOutputStream());//Make a writer for the output stream to the client
        System.out.println("PageWriter Made");

        //This was hardcoded, It was in the file but it would break the html page when sent
        pageWriter.println("HTTP/1.1 200 OK");
        pageWriter.println("Content-Type: text/html");
        pageWriter.println("\r\n");

        //Connect buffered reader to a filereader that reads the file
        BufferedReader reader = new BufferedReader(new FileReader(page));//grab a file and put it into the buffer
        System.out.println("ReaderMade");

        String line = reader.readLine();//line to go line by line from file
        while(line != null)//repeat till the file is empty
        {
            pageWriter.println(line);//print current line to Dom
            line = reader.readLine();//read next line
        }
        reader.close();//close the reader
        pageWriter.close();//Close the writer
    
    }    
} 

