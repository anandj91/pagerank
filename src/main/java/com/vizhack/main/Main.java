package com.vizhack.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.vizhack.graphdb.DBUtil;

public class Main {

    
    public static void createDomainNodes(File fin) throws IOException {
        FileInputStream fis = new FileInputStream(fin);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));        
        DBUtil db =  new DBUtil();
        ArrayList<String> domainCacheList = new ArrayList<String>();

        String line = null, domain = null;
        while ((line = br.readLine()) != null) {            
            String[] lineArray = line.split("\t");
            domain = lineArray[3];
            domain = domain.replace("https://","").replace("http://","");
            
            if(domain != "unknown" && domain != "-") {
                domain = domain.contains("/") ? domain.substring(0,domain.indexOf("/")) : domain;
                if(!domainCacheList.contains(domain)) {
                    db.insertNode(domain);
                    domainCacheList.add(domain);
                }
            }
        }
        
        
    }

    public static void main(String[] args) throws IOException {
        File a = new File("/home/ubuntu/a");
        createDomainNodes(a);
        CreateRelations(a);
        
    }
//cookie timestamp
    private static void CreateRelations(File fin) throws IOException {
        FileInputStream fis = new FileInputStream(fin);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));  
        
        DBUtil db =  new DBUtil();
        String prevLine=br.readLine();
        String prevLineArray[]=prevLine.split("\t");
        String domPrev = prevLineArray[3]
                .replace("https://", "")
                .replace("http://", "")
                .substring(
                        0,
                        prevLineArray[3].indexOf("/") == -1 ? prevLineArray[3]
                                .length() : prevLineArray[3].indexOf("/"));

        String line = null, domain = null;
        while ((line = br.readLine()) != null) {            
            String[] lineArray = line.split("\t");
            if(prevLineArray[0].equals(lineArray[0]) && !lineArray[3].equals("unknown")  && !lineArray[3].equals("-"))
                {
                String dom = lineArray[3]
                        .replace("https://", "")
                        .replace("http://", "")
                        .substring(
                                0,
                                lineArray[3].indexOf("/") == -1 ? lineArray[3]
                                        .length() : lineArray[3].indexOf("/"));
                    if(!domPrev.equals(dom)){
                        db.addRelation(domPrev,dom,lineArray[0]);
                    }
                    for(int i=0;i<prevLineArray.length;i++){
                        prevLineArray[i]=lineArray[i];
                    }
                    domPrev=dom;
                }
        }
        
        
    }

}
