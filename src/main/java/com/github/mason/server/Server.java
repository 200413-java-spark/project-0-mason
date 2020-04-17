package com.github.mason.server;

import com.github.mason.server.fileManagement.MyFile;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;



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
        MyFile file = new MyFile();//maybe showing red Squiggles, still good. Just Visual Code Things

        boolean running = true;
        while(running)
        {
            //See if anyone connects
            Socket client = myServer.accept();                    
            file.SendFile(client);
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
   
} 

