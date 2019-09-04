package com.bbc.jsc;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.commons.validator.*;

import org.json.JSONObject;
import org.json.JSONArray;

public class InputHandler {

    //------------ INSTANCE VARIABLES -------------
    private List<String>lstOfURLs=new LinkedList<>();
    private JSONArray URLlst=new JSONArray();
    
    public InputHandler(List<String>lstOfURLs){
        this.lstOfURLs=lstOfURLs;
    }

    public void createJSON(){
        for(String url:lstOfURLs){
            boolean valid = isURLValid(url);
            Map<String, String>lstOfProperties=new LinkedHashMap<String, String>();
            if(valid) lstOfProperties.putAll(getProperties(url));
            else lstOfProperties.put("Error","Invalid URL");
            toJSON(lstOfProperties);
        }
        createJSONDocument();
    }

    public boolean isURLValid(String url){
        UrlValidator urlValidator = new UrlValidator();
        if(urlValidator.isValid(url)) return true;
        else System.err.println("The URL \""+url +"\" is invalid ");
        return false;
    }

    public Map<String,String> getProperties(String url){
        Map<String, String>properties=new LinkedHashMap<String, String>();
        properties.put("Url",url);
        try {
            URL link = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) link.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            properties.put("Valid","true");
            properties.put("Status_code",String.valueOf(connection.getResponseCode()));
            properties.put("Content_length",String.valueOf(connection.getContentLengthLong()));
            properties.put("Date",String.valueOf(connection.getDate()));
        }catch(Exception e){
            System.err.println("There seems to be a problem with the URL");
            properties.put("Valid","false");
            properties.put("Error","Invalid URL");
        }
        return properties;
    }

    public void toJSON(Map<String, String>lstOfProperties){
        JSONObject jsonLink = new JSONObject();
        if(lstOfProperties.size()>3) {
            jsonLink.put("Url",lstOfProperties.get("Url"));
            jsonLink.put("Status_code",lstOfProperties.get("Status_code"));
            jsonLink.put("Content_length",lstOfProperties.get("Content_length"));
            jsonLink.put("Date",lstOfProperties.get("Date"));

        }else{
            jsonLink.put("Url",lstOfProperties.get("Url"));
            jsonLink.put("Error",lstOfProperties.get("Error"));
        }
        URLlst.put(jsonLink);
    }

    public void createJSONDocument(){
        File file = new File("LinkProperties.json");
        try(FileWriter writer = new FileWriter(file)){
            writer.write(URLlst.toString(4));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONArray getURLlst(){
        return URLlst;
    }
}
