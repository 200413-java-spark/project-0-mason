package com.github.mason.server.filemanagement;

import com.github.mason.server.filemanagement.MyComment;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

public class MyComments {

    public List<MyComment> comments = new ArrayList<MyComment>();

    public MyComments() throws Exception
    {
        File folder = new File("commentHome");
        for (final File fileEntry : folder.listFiles()) 
        {
            

            String fname = "";
            String lname = "";
            String title = "";
            String comment = "";
            System.out.println(fileEntry.getName());
            BufferedReader genIndex = new BufferedReader(new FileReader(fileEntry));
            fname = genIndex.readLine();
            lname = genIndex.readLine();
            title = genIndex.readLine();
            comment = genIndex.readLine();
            MyComment mycomment = new MyComment(fname,lname,title,comment);

            comments.add(mycomment);

            System.out.println(fname);
            System.out.println(lname);
            System.out.println(title);
            System.out.println(comment);

        }
    }

    public int getSize()
    {
        return comments.size();
    }


}