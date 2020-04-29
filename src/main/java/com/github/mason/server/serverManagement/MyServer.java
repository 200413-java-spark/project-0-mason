package com.github.mason.server.serverManagement;

import com.github.mason.server.filemanagement.MyFile;
import com.github.mason.server.client.NewClient;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.File;

public class MyServer
{


    public static void StartServer() throws Exception
    {
        //portNumber for socket
        int portNumber = 8080;
        //create server socket and say that it is running
        final ServerSocket myServer = new ServerSocket(portNumber);//change portNumber to args later
        System.out.println("I have Connected To Port " + portNumber);

        boolean running = true;
        MyFile index = new MyFile();

        while(running)
        {
            //create socket client initialized to null
            Socket client = null;
            try
            {
                //See if anyone connects
                client = myServer.accept(); 
                
                //create thread for new client
                Thread clientThread = new NewClient(client,index);//, dis, dos);
                //Start Thread
                clientThread.start();

            }
            catch(Exception e)
            {
                System.out.println("Something Went Wrong Creating New Client");
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



}
