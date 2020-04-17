package com.github.mason.server.fileManagement;

import java.net.Socket;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class MyFile
{

    public static void SendFile(Socket client) throws Exception
    {
        //Open the indicated file
        File page = new File("index.html");

        PrintWriter pageWriter = new PrintWriter(client.getOutputStream());//Make a writer for the output stream to the client

        //This was hardcoded, It was in the file but it would break the html page when sent
        pageWriter.println("HTTP/1.1 200 OK");
        pageWriter.println("Content-Type: text/html");
        pageWriter.println("\r\n");

        //Connect buffered reader to a filereader that reads the file
        BufferedReader reader = new BufferedReader(new FileReader(page));//grab a file and put it into the buffer

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