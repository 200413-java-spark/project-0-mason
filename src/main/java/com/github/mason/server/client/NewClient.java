package com.github.mason.server.client;

import com.github.mason.server.fileManagement.MyFile;

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



public class NewClient extends Thread
{
    static final File WebRoot = new File(".");
    static final String deFile = "index.html"; //default file
    //final DataInputStream dataInputStream;
    //final DataOutputStream dataOutputStream;
    final Socket client;

    //for debug
    boolean dall = false;
    boolean dimage = true;
    boolean dtext = false;


    public NewClient(Socket s)//, DataInputStream dis, DataOutputStream dos) 
    {
        client = s;
        //dataInputStream = dis;
        //dataOutputStream = dos;
    }

    @Override
    public void run()
    {
        BufferedReader requestReader = null;
        InputStream imageReader = null;
        PrintWriter requestWriter = null;
        BufferedOutputStream dataOut = null;
        String fileRequested = null;
        try
        {
            String input = null;
            try
            {
                requestReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                requestWriter = new PrintWriter(client.getOutputStream());
                dataOut = new BufferedOutputStream(client.getOutputStream());
            }
            catch(IOException e)
            {
                if(dall)System.out.println("Failed to oppen connections");
                
            }
            finally
            {
                if(dall)System.out.println("Readers & Writers open");
                
            }
            try
            {
                input = requestReader.readLine();
                if(dall)System.out.println(input);
                
            }
            catch(IOException e)
            {
                if(dall || dimage)System.out.println("Could Not read input from client");
                
            }
            if(input != null)
            {
                StringTokenizer parse = new StringTokenizer(input);
                String method = parse.nextToken().toUpperCase();
                fileRequested = parse.nextToken().toLowerCase();
                if(dall || dimage) System.out.println(fileRequested);
                if(!method.equals("GET") && !method.equals("HEAD"))
                {
                    if(dall || dimage)System.out.println("File Not Found");
                }
                else
                {
                    
                    if(fileRequested.endsWith("/"))
                    {
                        fileRequested += deFile;
                    }
                    File file = new File(WebRoot, fileRequested);
                    int fileLength = (int) file.length();
                    String content = getContentType(fileRequested);
                    if(method.equals("GET"))
                    {
                        if(content.contains("image"))
                        {
                            
                            if(dall || dimage)System.out.println("This is an Image");
                            imageReader = new BufferedInputStream(new FileInputStream(file));

                            if(dimage)System.out.println(file.getAbsolutePath());
                            if(dimage)System.out.println("FileLength: " + fileLength);
                            if(dimage)System.out.println("ContentType2: " + content);
                            
                            requestWriter.println("HTTP/1.1 200 OK");
                            requestWriter.println("Content-Type: " + content);
                            requestWriter.println("Content-Length: " + fileLength + "\r\n");
                            requestWriter.println("\r\n");
                            requestWriter.flush();

                            
                            	                 
                            
                            byte[] buffer = new byte[fileLength];
                            int bytesRead;
                            imageReader.read(buffer,0,buffer.length);
                            dataOut.write(buffer,0,buffer.length);

                            //while ((bytesRead = imageReader.read(buffer)) != -1)
                            //{
                            //    dataOut.write(buffer, 0, bytesRead);
                            //}
                            imageReader.close();
                            dataOut.flush();
//                            dataOut.close();
                            if(dall || dimage)System.out.println("ImageSent");
                            

                        }
                        else
                        {
                            if(dall || dtext)System.out.println("This is a text");
                            
                            byte[] fileData = readFileData(file, fileLength);
                            if(dtext)System.out.println(file.getAbsolutePath());
                            if(dtext)System.out.println("FileLength: " + fileLength);
                            
                            requestWriter.println("HTTP/1.1 200 OK");
                            requestWriter.println("Content-Type: " + content);
                            requestWriter.println("Content-Length: " + fileLength + "\r\n");
                            requestWriter.println("\r\n");
                            requestWriter.flush();

                            dataOut.write(fileData, 0, fileLength);
                            dataOut.flush();
                            if(dall || dtext)System.out.println("text sent");
                            
                        }
                    }
                }
            }
            else
            {
                if(dall)
                System.out.println("input is null");
                return;
            }

        }
        catch(Exception e)
        {
            if(dall)
            System.out.println("Something went wrong getting the request");
        }
        finally
        {
            try
            {
                requestWriter.close();
                requestReader.close();
                dataOut.close();
                client.close();
            }
            catch(Exception e)
            {
                if(dall)
                System.out.println("Something went wrong closing everything");
            }
        }

    }

    private String getContentType(String fileRequested)
    {
        if(fileRequested.endsWith(".htm") || fileRequested.endsWith(".html"))
        {
            System.out.println("Content type = text/html");
            return "text/html";
        }
        if(fileRequested.endsWith(".ico"))
        {
            System.out.println("Content type = image/x-icon");
            return "image/x-icon";
        }
        if(fileRequested.endsWith(".png"))
        {
            System.out.println("Content type = image/png");
            return "image/png";
        }
        if(fileRequested.endsWith(".jpg"))
        {
            System.out.println("Content type = image/jpeg");
            return "image/jpeg";
        }
        if(fileRequested.endsWith("css"))
        {
            System.out.println("Content type = text/css");
            return "text/css";
        }
        else
        {
            System.out.println("Content type = text/plain");
            return "text/plain";
        }
        
    }

    private byte[] readFileData(File file, int fileLength) throws IOException
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
}
