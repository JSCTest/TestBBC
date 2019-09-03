package com.bbc.jsc;

/**
 * Created by Jagjeet on 02/09/2019.
 */

import java.net.*;
import java.util.*;

import org.apache.commons.validator.*;
public class InputHandler {
    private String[]lstOfURLs;

    public InputHandler(String[]lstOfURLs){
        this.lstOfURLs=lstOfURLs;
    }

    public void createJSON(){
        for(String url:lstOfURLs){
            boolean valid = isURLValid(url);
            List<String>lstOfProperties=new LinkedList<String>();
            lstOfProperties.add(url);
            lstOfProperties.add(String.valueOf(valid));
            if(valid) lstOfProperties.addAll(getProperties(url));
            else lstOfProperties.add("Invalid URL");
            toJSON(lstOfProperties);
        }
    }

    public boolean isURLValid(String url){
        UrlValidator urlValidator = new UrlValidator();
        if(urlValidator.isValid(url)) return true;
        else System.err.println("The URL \""+url +"\" is invalid ");
        return false;
    }

    public List<String> getProperties(String url){
        List<String>properties=new LinkedList<String>();
        properties.add(url);
        try {
            URL link = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) link.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            properties.add("true");
            properties.add(String.valueOf(connection.getResponseCode()));
            properties.add(String.valueOf(connection.getContentLengthLong()));
            properties.add(String.valueOf(connection.getDate()));
        }catch(Exception e){
            System.err.println("There seems to be a problem with the URL");
            properties.add("false");
            properties.add("Invalid URL");
        }
        return properties;
    }

    public void toJSON(List<String>lstOfProperties){

    }
}
