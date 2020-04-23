package com.github.mason.server.fileManagement;

import java.net.Socket;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;


public class MyFile
{
    //static PrintWriter pageWriter;

    public static void SendFile(Socket client, PrintWriter pageWriter) throws Exception
    {
        //Open the indicated file
        File page = new File("index.html");

        //pageWriter = new PrintWriter(client.getOutputStream());//Make a writer for the output stream to the client



        //Connect buffered reader to a filereader that reads the file
        BufferedReader reader = new BufferedReader(new FileReader(page));//grab a file and put it into the buffer

        //This was hardcoded, It was in the file but it would break the html page when sent
        pageWriter.println("HTTP/1.1 200 OK");
        pageWriter.println("Content-Type: text/html");
        pageWriter.println("Content-Length: " + page.length() + "\r\n");
        pageWriter.println("\r\n");
        
        String line = reader.readLine();//line to go line by line from file
        while(line != null)//repeat till the file is empty
        {
            pageWriter.println(line);//print current line to Dom
            line = reader.readLine();//read next line
        }
        reader.close();//close the reader
        pageWriter.flush();//flush the writer and keep the socket open
        
//        pageWriter.close();
//        GetRequest(client);
    }

/*    public static void GetRequest(Socket client) throws Exception
    {
        
        InputStream request = client.getInputStream();
        InputStreamReader sreader= new InputStreamReader(request);
        BufferedReader streamreader = new BufferedReader(sreader);
        String line = streamreader.readLine();
        line = line.replace("GET ","");
        line = line.replace("HTTP/1.1","");
        line = line.replace("\\","/");
        line = "." + line;
        if(line.length() > 4)
        {
            System.out.println(line.length());
            System.out.println(line);

            File file = new File(line);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String fline = reader.readLine();
            while(fline != null)
            {
                pageWriter.println(fline);
                fline = reader.readLine();
            }
            reader.close();
            
        }
        pageWriter.close();
    }*/
} 
/*private static void SendHeader(BufferedOutputStream pageWriter, int code, String contentType, long contentLength, long lastModified) throws IOException
{
    pageWriter.write(("HTTP/1.0 " + code + "OK\r\n"+
                    "Date: " + new Date().toString() +"\r\n" +
                    "Server: MasonsWebServer/1.0\r\n" +
                    "Content-Type: " + contentType + "\r\n" +
                    "Expires: Tue, 03 Jan 2021 12:00:00 GMT\r\n" +
                    ((contentLength != -1) ? "Content-Length: " + contentLength +"\r\n" : "") +//if content length is not less then one, send the content length, else send blank
                    "Last-modified: " + new Date(lastModified).toString() + "\r\n" +"\r\n").getBytes());
} */  