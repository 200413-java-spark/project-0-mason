package com.github.mason.server;

import com.github.mason.server.serverManagement.MyServer;
import com.github.mason.server.filemanagement.MyFile;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.io.IOException;



public class Server
{

    public static void main (String[] args) throws Exception
    {
        MyFile index = new MyFile();

        MyServer server = new MyServer();
        server.StartServer();
  
    }
   
} 

