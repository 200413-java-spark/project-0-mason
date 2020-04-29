package com.github.mason.server.filemanagement;

import com.github.mason.server.filemanagement.MyComments;

import java.util.ArrayList;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.File;

public class MyFile
{

    //On creation, run CreateIndexFile
    public MyFile() throws Exception
    {
        System.out.println("MyFile Object Created");
        CreateIndexFile();
    }

    //Grabs a template file, checks for the resent post queue, updates front page
    public void CreateIndexFile() throws Exception
    {
        //open the index.html file
        PrintStream tmpIndex = new PrintStream(new File("index.html"));

        //read from indexGen File
        BufferedReader genIndex = new BufferedReader(new FileReader("./templates/indexGen.html"));

        MyComments comments = new MyComments();

        //get first line
        String line = genIndex.readLine();
        while(line!=null)
        {
            //logic for adding new posts
            if(line.contains("[IndexLink]"))
            {
                if(comments.getSize() > 0)
                {
                    for(MyComment c : comments.comments)
                    {
                        System.out.println("IndexLinkSeen");
                        BufferedReader indexLink = new BufferedReader(new FileReader("./templates/indexLink.html"));
                        String iline = indexLink.readLine();

                        while(iline!=null)
                        {
                            System.out.println(iline);
                            if(iline.contains("[fname]"))
                            {
                                tmpIndex.println("<p class=\"text-light\">" + c.myfname + "</p>");
                                iline = indexLink.readLine();
                            }
                            else if(iline.contains("[lname]"))
                            {
                                tmpIndex.println("<p class=\"text-light\">" + c.mylname + "</p>");
                                iline = indexLink.readLine();
                            }
                            else if(iline.contains("[title]"))
                            {
                                tmpIndex.println("<p class=\"text-light\">" + c.mytitle + "</p>");
                                iline = indexLink.readLine();
                            }
                            else if(iline.contains("[comment]"))
                            {
                                tmpIndex.println("<p class=\"text-light\">" + c.mycomment + "</p>");
                                iline = indexLink.readLine();
                            }
                            else 
                            {
                                tmpIndex.println(iline);
                                iline = indexLink.readLine();
                            }
                        }
                        indexLink.close();

                    }
                }
                else
                {
                    tmpIndex.println("<p class=\"text-light\"> No Comments Here </p>");
                }
                line = genIndex.readLine();
                
            }
        
            else
            {
                tmpIndex.println(line);
                line = genIndex.readLine();
            }
        }
        genIndex.close();

    }
}