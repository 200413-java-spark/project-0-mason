package com.github.mason.server.client;

import java.net.Socket;
public class NewClient extends ClientAbs
{
    public NewClient(Socket s)//, DataInputStream dis, DataOutputStream dos) 
    {
        setClientSocket(s);
    }

}
