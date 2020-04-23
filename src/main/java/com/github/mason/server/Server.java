package com.github.mason.server;

import com.github.mason.server.fileManagement.MyFile;
import com.github.mason.server.serverManagement.MyServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.io.IOException;



public class Server
{
    public static final Hashtable fileType = new Hashtable();

    
    static
    {
        String image = "image/";
        fileType.put(".html", image +"html");
    }
    public static void main (String[] args) throws Exception
    {
        MyServer server = new MyServer();
        server.StartServer();
  
    }
   
} 

