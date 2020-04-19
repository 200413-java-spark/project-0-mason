package com.github.mason.server.client;

import com.github.mason.server.fileManagement.MyFile;

import java.io.DataOutputStream;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.BufferedReader;

public class NewClient extends Thread
{
    //final DataInputStream dataInputStream;
    //final DataOutputStream dataOutputStream;
    final Socket client;
    
    public NewClient(Socket s)//, DataInputStream dis, DataOutputStream dos) 
    {
        client = s;
        //dataInputStream = dis;
        //dataOutputStream = dos;
    }

    @Override
    public void run()
    {
        MyFile filets = new MyFile();
        while (true)  
        { 
            try 
            { 
                PrintWriter pageWriter = new PrintWriter(client.getOutputStream());//Make a writer for the output stream to the client
                InputStream request = client.getInputStream();
                InputStreamReader sreader= new InputStreamReader(request);
                BufferedReader streamreader = new BufferedReader(sreader);
                String rline = streamreader.readLine();
                
                if(rline != null)
                {
                    filets.SendFile(client, pageWriter);
                    System.out.println(rline);
                }
               // System.out.println("Just Testing");
            }
            catch (Exception e) 
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } 
    }
}
