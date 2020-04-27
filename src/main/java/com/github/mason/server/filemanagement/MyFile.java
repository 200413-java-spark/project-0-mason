package com.github.mason.server.filemanagement;

import java.util.ArrayList;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.File;

public class MyFile
{
//    enum htmlType
//    {
//        HEADER,
//        FOOTER,
//        SIDEBAR,
//        DIV,
//        PAR,
//        H1,
//    }

//    ArrayList<htmlType> htmlTypes = new ArrayList<htmlType>();

    public MyFile() throws Exception
    {
        System.out.println("MyFile Object Created");
        CreateIndexFile();
    }

    private void CreateIndexFile() throws Exception
    {
        PrintStream tmpIndex = new PrintStream(new File("index.html"));

        BufferedReader genIndex = new BufferedReader(new FileReader("./templates/indexGen.html"));
        String line = genIndex.readLine();
        while(line!=null)
        {
            //line.concat("\r\n");
            if(line.contains("[IndexLink]"))
            {
                System.out.println("IndexLinkSeen");
                BufferedReader indexLink = new BufferedReader(new FileReader("./templates/indexLink.html"));
                String iline = indexLink.readLine();
                while(iline!=null)
                {
                    tmpIndex.println(iline);
                    iline = indexLink.readLine();
                }
                line = genIndex.readLine();
                indexLink.close();

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