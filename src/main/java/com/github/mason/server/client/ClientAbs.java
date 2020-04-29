package com.github.mason.server.client;

import com.github.mason.server.filemanagement.MyFile;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.net.Socket;
import java.util.StringTokenizer;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.util.List; // import just the List interface
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.lang.StringBuilder;
import java.io.InputStream;
import java.io.PrintStream;

public abstract class ClientAbs extends Thread
{
    static final File WebRoot = new File(".");
    static final String deFile = "index.html"; //default file

    private Socket client;
    private BufferedReader requestReader = null;
    private InputStream imageReader = null;
    private PrintWriter requestWriter = null;
    private BufferedOutputStream dataOut = null;
    private OutputStream imageOut = null;
    private String fileRequested = null;
    private String method = null;
    private String input = null;
    private String content = null;
    private int fileLength = 0;
    private MyFile index = null;
    private String fname = null;
    private String lname = null;
    private String comment = null;
    private String title = null;
    private String commentHome = "commenthome/";
    

    @Override
    public void run()
    {

        try
        {
            SetClientRW();//Set the Readers and Writers

            GetClientRequest();


            if(input != null)
            {
                RequestParser(input);//fill the input

                System.out.println("Requested File: " + fileRequested);
                
                if(fileRequested.endsWith("/"))
                {
                    fileRequested += deFile;
                }

                File file = FillInput();

                if(method.equals("GET"))
                {

                    SendText(file);

                }

            }
            else
            {
                System.out.println("input is null");
                return;
            }

        }
        catch(Exception e)
        {
            System.out.println("Something went wrong getting the request");
        }
        finally
        {
            try
            {
                requestWriter.close();//printwriterclosed
                requestReader.close();//BufferedReaderclosed
                dataOut.close();//bufferedOutputStream closed
                client.close();
            }
            catch(Exception e)
            {
                System.out.println("Something went wrong closing everything");
            }
        }
        System.out.println("***************************");

    }
    
    public void setClientSocket(Socket c)
    {
        client = c;
    }

    public void setIndex(MyFile ind)
    {
        index = ind;
    }

    private void SetClientRW()
    {
        try
        {
            requestReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            requestWriter = new PrintWriter(client.getOutputStream());//printwriter linked to client stream
            dataOut = new BufferedOutputStream(client.getOutputStream());//bufferedOutputStream linked to client
            imageOut = client.getOutputStream();

        }
        catch(IOException e)
        {
            System.out.println("Failed to oppen connections");
            
        }
    }

    private void GetClientRequest()
    {
        try
        {
            input = requestReader.readLine();

            
        }
        catch(IOException e)
        {
            System.out.println("Could Not read input from client");
            
        }
    }

    private File FillInput()
    {
        File file = new File(WebRoot, fileRequested);
        String name = file.getName();
        fileLength = (int) file.length();
        content = getContentType(fileRequested);
        return file;
    }
    private void RequestParser(String input) throws Exception
    {
        StringTokenizer parse = new StringTokenizer(input);
        method = parse.nextToken().toUpperCase();
        fileRequested = parse.nextToken();//.toLowerCase();
        //reparse fileRequested if there is extra stuff
        if(fileRequested.indexOf("fname=") != -1)
        {
            System.out.println("Request get has variables");
            String[] frVariables = fileRequested.split("\\?");
            fileRequested = frVariables[0];
            
            System.out.println("Varibles = " + frVariables[1]);

            String[] var1Var2Var3 = frVariables[1].split("&");
            
            System.out.println("Var1 = " + var1Var2Var3[0]);
            fname = var1Var2Var3[0].replace("fname=","");
            System.out.println("Var1 fname: " + fname);

            System.out.println("Var2 = " + var1Var2Var3[1]);
            lname = var1Var2Var3[1].replace("lname=","");
            System.out.println("Var2 lname: " + lname);

            System.out.println("Var3 = " + var1Var2Var3[2]);
            title = var1Var2Var3[2].replace("title=","");
            System.out.println("Var3 Title: " + title);
            if(title.contains("+"))
            {
                title = title.replace("+", " ");
            }
            System.out.println("Var3 Title2: " + title);

            System.out.println("Var4 = " + var1Var2Var3[3]);
            comment = var1Var2Var3[3].replace("comment = ","");
            System.out.println("Var4 comment: " + comment);
            //+ = " "
            //%0D%0A = Enter
            //%21 = ! 
            // %3F = ?
            //%2 = ,
            String paragraphs = comment.replaceAll("\\%0D\\%0A","+");// Shorten Paragraphs and make them scentences
            paragraphs = paragraphs.replace("comment=","");

            System.out.println("realComment = " + paragraphs);

            String sentences = paragraphs.replace("+", " ");

            System.out.println("sentences = " + sentences);

            sentences = sentences.replaceAll("%21","!");
            sentences = sentences.replaceAll("%3F","?");
            sentences = sentences.replaceAll("%2C","\\,");

            System.out.println("sentences2 = " + sentences);
            PrintStream tmpIndex = new PrintStream(new File(commentHome + title  + ".txt"));
            tmpIndex.println(fname);
            tmpIndex.println(lname);
            tmpIndex.println(title);
            tmpIndex.println(sentences);
            tmpIndex.close();

        }

        fileRequested = fileRequested.toLowerCase();
        System.out.println("FileRequested = " + fileRequested);

        index.CreateIndexFile();
    }

    private String getContentType(String fileRequested)
    {
        String type = null;
        if(fileRequested.endsWith(".htm") || fileRequested.endsWith(".html"))
        {
            type = "text/html";
        }
        if(fileRequested.endsWith(".css"))
        {
            type = "text/css";
        }
        return type;
    }

    private void SendText(File file) throws Exception
    {
               
        byte[] fileData = readFileData(file);
        
        SendHeader();

        dataOut.write(fileData, 0, fileLength);
        dataOut.flush();
        
    }

    private byte[] readFileData(File file) throws IOException
    {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];
        try
        {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        }
        finally
        {
            if(fileIn != null)
            {
                fileIn.close();
            }
            return fileData;
        }

    }

    private void SendHeader()
    {
        requestWriter.println("HTTP/1.1 200 OK");//printwriter used to send text directly to browser
        requestWriter.println("Content-Type: " + content + "\r\n");
        requestWriter.println("Content-Length: " + fileLength + "\r\n");
        requestWriter.println("\r\n");

        requestWriter.flush();
    }

}