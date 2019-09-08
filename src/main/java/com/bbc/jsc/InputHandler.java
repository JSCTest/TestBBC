package com.bbc.jsc;

import org.apache.commons.validator.routines.UrlValidator;

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
        urlData.setValid(isValidURL(url));
        try {
            URL link = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) link.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.connect();
            urlData.setStatusCode(connection.getResponseCode());
            urlData.setContentLength(connection.getContentLengthLong());
            urlData.setDate(new Date(connection.getDate()).toString());
        }catch(Exception e){
            urlData.setError("Invalid URL");
        }
        return urlData;
    }

    public boolean isValidURL(String url){
        UrlValidator urlValidator = new UrlValidator();
        if(urlValidator.isValid(url)) return true;
        else System.err.println("The URL \""+url +"\" is invalid ");
        return false;
    }
}