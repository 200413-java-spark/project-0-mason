package com.github.mason.server.client;

import com.github.mason.server.filemanagement.MyFile;

import java.net.Socket;
public class NewClient extends ClientAbs
{
    public NewClient(Socket s,MyFile index)//, DataInputStream dis, DataOutputStream dos) 
    {
        setClientSocket(s);
        setIndex(index);
    }

}
