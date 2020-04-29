package com.github.mason.server;

import com.github.mason.server.serverManagement.MyServer;


import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.io.IOException;




public class Server
{

    public static void main (String[] args) throws Exception
    {


//        List<MyComment> comments = new ArrayList<>();
        
//        UpdateList();


        MyServer server = new MyServer();
        server.StartServer();
  
    }
//    public void UpdateList(MyComment com)
//    {
//        comments.add(com);
//    }
   
} 

