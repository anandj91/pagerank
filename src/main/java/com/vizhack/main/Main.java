package com.vizhack.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

import com.vizhack.graphdb.DBUtil;

public class Main {
    
    private static final String delim = "\t";

    public static void createDomainNodes(File fin) throws IOException {
        FileInputStream fis = new FileInputStream(fin);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        DBUtil db = new DBUtil();
        HashSet<String> domainCacheList = new HashSet<String>();

        String line = null, domain = null;
        while ((line = br.readLine()) != null) {
            String[] lineArray = line.split(delim);
            domain = lineArray[3];
            domain = domain.replace("https://", "").replace("http://", "");

            if (domain != "unknown" && domain != "-") {
                domain = domain.contains("/") ? domain.substring(0,
                        domain.indexOf("/")) : domain;
                if (!domainCacheList.contains(domain)) {
                    db.insertNode(domain);
                    domainCacheList.add(domain);
                }
            }
        }

    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Pass file name as argument");
            System.exit(-1);
        }
        File a = new File(args[0]);
        createDomainNodes(a);
        CreateRelations(a);
    }

    // cookie timestamp
    private static void CreateRelations(File fin) throws IOException {
        FileInputStream fis = new FileInputStream(fin);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        DBUtil db = new DBUtil();
        String prevLine = br.readLine();
        String prevLineArray[] = prevLine.split(delim);
        String domPrev = getTopDomain(prevLineArray[3]);
        String line = null, domain = null;
        while ((line = br.readLine()) != null) {
            String[] lineArray = line.split(delim);
            if (prevLineArray[0].equals(lineArray[0])
                    && !lineArray[3].equals("unknown")
                    && !lineArray[3].equals("-")) {
                String dom = getTopDomain(lineArray[3]);
                if (!domPrev.equals(dom)) {
                    db.addRelation(domPrev, dom, lineArray[0]);
                    if(lineArray[1].equals("true")){
                        db.addRelation(dom, lineArray[2], lineArray[0]);
                    }
                }
                for (int i = 0; i < prevLineArray.length; i++) {
                    prevLineArray[i] = lineArray[i];
                }
                domPrev = dom;
            }
            else if(!prevLineArray[0].equals(lineArray[0])){
                for (int i = 0; i < prevLineArray.length; i++) {
                    prevLineArray[i] = lineArray[i];
                }
            }
        }

    }

    private static String getTopDomain(String url) {
        String temp = url.replace("https://", "").replace("http://", "");
        String domPrev = temp.substring(0,
                temp.indexOf("/") == -1 ? temp.length() : temp.indexOf("/"));
        return domPrev;
    }

}
