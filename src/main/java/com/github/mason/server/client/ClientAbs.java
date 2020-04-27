package com.github.mason.server.client;

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

    @Override
    public void run()
    {

        try
        {
            SetClientRW();

            GetClientRequest();


            if(input != null)
            {
                RequestParser(input);

                if(!method.equals("GET") && !method.equals("HEAD"))
                {
                    System.out.println("File Not Found");
                }
                else
                {
                    
                    if(fileRequested.endsWith("/"))
                    {
                        fileRequested += deFile;
                    }

                    File file = FillInput();

                    if(method.equals("GET"))
                    {
                        if(content.contains("image"))
                        {
                            SendMedia(file);
                        }
                        else
                        {
                            SendText(file);
                        }
                    }
                    System.out.println("***************************");
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
            e.printStackTrace();
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
                e.printStackTrace();
            }
        }

    }
    
    public void setClientSocket(Socket c)
    {
        client = c;
    }

    private void SetClientRW()
    {
        try
        {
            requestReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            requestWriter = new PrintWriter(client.getOutputStream());//printwriter linked to client stream
            dataOut = new BufferedOutputStream(client.getOutputStream());//bufferedOutputStream linked to client
            imageOut = client.getOutputStream();//

        }
        catch(IOException e)
        {
            System.out.println("Failed to oppen connections");
            
        }
        finally
        {
            System.out.println("Readers & Writers open");
            
        }

    }

    private void GetClientRequest()
    {
        try
        {
            input = requestReader.readLine();
//s            String tmp = requestReader.readLine();
//            while(tmp!=null)
//            {
//                System.out.println("MyInput: "+ tmp);
//                tmp = requestReader.readLine();
//            }
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
        System.out.println("FileName: " + name);
        fileLength = (int) file.length();
        content = getContentType(fileRequested);
        return file;
    }
    private void RequestParser(String input)
    {
        StringTokenizer parse = new StringTokenizer(input);
        method = parse.nextToken().toUpperCase();
        fileRequested = parse.nextToken().toLowerCase();
        //System.out.println("Requested file: " +fileRequested);
    }

    private String getContentType(String fileRequested)
    {
        String type = null;
        if(fileRequested.endsWith(".htm") || fileRequested.endsWith(".html"))
        {
            //System.out.println("Content type = text/html");
            type = "text/html";
        }
        if(fileRequested.endsWith(".ico"))
        {
            //System.out.println("Content type = image/x-icon");
            type = "image/x-icon";
        }
        if(fileRequested.endsWith(".png"))
        {
            //System.out.println("Content type = image/png");
            type = "image/png";
        }
        if(fileRequested.endsWith(".jpg"))
        {
            //System.out.println("Content type = image/jpeg");
            type = "image/jpeg";
        }
        if(fileRequested.endsWith(".css"))
        {
            //System.out.println("Content type = text/css");
            type = "text/css";
        }

        System.out.println("Final Content Type: " + type);
        return type;
    }

    private void SendMedia(File file) throws Exception
    {
        dataOut = new BufferedOutputStream( client.getOutputStream() );
        // f is the file to be sent to the client.
        BufferedInputStream reader = new BufferedInputStream( new FileInputStream( file ) );//read file and transfer it
        // send OK headers and content length using f.length()
        byte[] buffer = new byte[ 4096 ];
        int bytesRead;
        while ( (bytesRead = reader.read(buffer)) != -1 )
        {
            dataOut.write( buffer, 0, bytesRead );
        }
        reader.close();
        dataOut.flush();

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