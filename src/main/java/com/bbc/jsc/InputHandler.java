package com.bbc.jsc;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class InputHandler {

    //------------ INSTANCE VARIABLES -------------
    private List<String>lstOfURLs=new LinkedList<>();


    //------------ CONSTRUCTOR -------------
    public InputHandler(List<String>lstOfURLs){
        this.lstOfURLs=lstOfURLs;
    }

    public List<URLObject> getURLData(){
        List<URLObject>lstOfProperties=new LinkedList<>();
        for(String url:lstOfURLs)
            lstOfProperties.add(getProperties(url));
        return lstOfProperties;
    }

    public URLObject getProperties(String url){
        URLObject urlData=new URLObject();
        urlData.setUrl(url);
        try {
            URL link = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) link.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.connect();
            urlData.setValid(true);
            urlData.setStatusCode(connection.getResponseCode());
            urlData.setContentLength(connection.getContentLengthLong());
            urlData.setDate(new Date(connection.getDate()).toString());
        }catch(Exception e){
            System.err.println("The URL \""+url +"\" is invalid ");
            urlData.setValid(false);
            urlData.setError("Invalid URL");
        }
        return urlData;
    }
}