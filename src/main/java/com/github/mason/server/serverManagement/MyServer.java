package com.github.mason.server.serverManagement;

//import com.github.mason.server.fileManagement.MyFile;
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
        //int portNumber = Integer.parseInt(args[0]);
        int portNumber = 8080;//change to args later
        //create server socket and say that it is running
        final ServerSocket myServer = new ServerSocket(portNumber);//change portNumber to args later
        System.out.println("I have Connected To Port " + portNumber);
//        MyFile file = new MyFile();//maybe showing red Squiggles, still good. Just Visual Code Things

        boolean running = true;
        while(running)
        {
            //create socket client initialized to null
            Socket client = null;
            try
            {
                //See if anyone connects
                client = myServer.accept(); 
                //create the clients input and output streams
//                DataInputStream dis = new DataInputStream(client.getInputStream());
//                DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                //create thread for new client
                Thread clientThread = new NewClient(client);//, dis, dos);
                //Start Thread
                clientThread.start();

//                file.SendFile(client);
            }
            catch(Exception e)
            {
                System.out.println("Something Went Wrong Creating New Client");
            }

//            file.GetRequest(client);
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
