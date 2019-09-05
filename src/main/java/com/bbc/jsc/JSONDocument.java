package com.bbc.jsc;

import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class JSONDocument implements Document{

    private JSONArray lstURL=new JSONArray();

    @Override
    public void convert(List<URLObject> lstOfURLData) {
        //Convert data of each URL to JSON format
        for(URLObject obj:lstOfURLData) {
            JSONObject jsonLink = new JSONObject();
            if (obj.getValid()) {
                jsonLink.put("Url", obj.getUrl());
                jsonLink.put("Status_code", obj.getStatusCode());
                jsonLink.put("Content_length", obj.getContentLength());
                jsonLink.put("Date", obj.getDate());

            } else {
                jsonLink.put("Url", obj.getUrl());
                jsonLink.put("Error", obj.getError());
            }
            lstURL.put(jsonLink);
        }

        //Create the JSON Document
        File file = new File("LinkProperties.json");
        try(FileWriter writer = new FileWriter(file)){
            writer.write(lstURL.toString(4));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public List<URLObject> read() {
        //Retrieve data from JSON file
        JSONArray URLList=new JSONArray();
        try{
            String content = FileUtils.readFileToString(new File("LinkProperties.json"), "utf-8");
            URLList = new JSONArray(content);
        } catch (Exception e){
            System.err.println("The File does not exist");
            e.printStackTrace();
        }

        List<URLObject>lstOfURLData=new LinkedList<>();
        for (int i=0;i<URLList.length();i++){
            URLObject urlObject =new URLObject();
            if(((JSONObject)URLList.get(i)).length()>3){
                //Getting data of valid URLs from JSON
                String link=((JSONObject)URLList.get(i)).get("Url").toString();
                String statusCode=((JSONObject)URLList.get(i)).get("Status_code").toString();
                String Content_length=((JSONObject)URLList.get(i)).get("Content_length").toString();
                String date=((JSONObject)URLList.get(i)).get("Date").toString();

                //Setting data to URLObject for each URL
                urlObject.setUrl(link);
                urlObject.setValid(true);
                urlObject.setStatusCode(Integer.parseInt(statusCode));
                urlObject.setContentLength(Long.parseLong(Content_length));
                urlObject.setDate(date);
            }else{
                //Getting data of invalid URLs from JSON
                String link=((JSONObject)URLList.get(i)).get("Url").toString();
                String error=((JSONObject)URLList.get(i)).get("Error").toString();

                //Setting data to URLObject for each URL
                urlObject.setUrl(link);
                urlObject.setError(error);
            }
            lstOfURLData.add(urlObject);
        }
        return lstOfURLData;
    }

    @Override
    public void print(){
        //Retrieve data from JSON file
        JSONArray URLList=new JSONArray();
        try{
            String content = FileUtils.readFileToString(new File("LinkProperties.json"), "utf-8");
            URLList = new JSONArray(content);
        } catch (Exception e){
            System.err.println("The File does not exist");
            e.printStackTrace();
        }
        System.out.println(URLList.toString(4));
    }
}