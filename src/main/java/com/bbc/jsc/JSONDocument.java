package com.bbc.jsc;

import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JSONDocument implements Document{

    private JSONArray lstURL=new JSONArray();
    private int fileCount=1;
    private Map<Integer,Integer>responseCount=new LinkedHashMap<>();

    public JSONDocument(){
        try {
            FileUtils.cleanDirectory(new File("LinkData"));
        } catch(IllegalArgumentException e){
            System.out.println("Directory doesn't exist\nCreating directory");
            //new File("/Users/Jagjeet/IdeaProjects/TestBBC/LinkData").mkdir();
            new File("LinkData").mkdir();
        }
        catch (IOException e) {
            System.err.println("Unsuccessful cleaning of directory");
        }
    }

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
            createDoc(jsonLink);
            if(obj.getStatusCode()==0) continue;
            else if(responseCount.containsKey(obj.getStatusCode())) responseCount.put(obj.getStatusCode(),responseCount.get(obj.getStatusCode())+1);
            else responseCount.put(obj.getStatusCode(),1);
        }

        for(int code:responseCount.keySet()){
            JSONObject response = new JSONObject();
            response.put("Status_code",code);
            response.put("Number_of_responses",responseCount.get(code));
            lstURL.put(response);
        }
        createResponseDoc();
    }

    public void createDoc(JSONObject jsonLink) {
        //Create the JSON Document
        File file = new File("LinkData/Link"+fileCount+".json");
        //File file = new File("/Users/Jagjeet/IdeaProjects/TestBBC/LinkData/Link"+fileCount+".json");
        try(FileWriter writer = new FileWriter(file)){
            writer.write(jsonLink.toString(4));
            writer.flush();
            fileCount++;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createResponseDoc() {
        //Create the big JSON Document
        File file = new File("Response_Count.json");
        try(FileWriter writer = new FileWriter(file)){
            writer.write(lstURL.toString(4));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public List<URLObject> read() {

        File[]files=new File("LinkData").listFiles();
        List<URLObject>lstOfURLData=new LinkedList<>();
        for(File file:files) {
            //Retrieve data from JSON file
            JSONObject data = new JSONObject();
            URLObject urlObject =new URLObject();
            try {
                String content = FileUtils.readFileToString(file, "utf-8");
                data = new JSONObject(content);
            } catch (Exception e) {
                System.err.println("The File does not exist");
                e.printStackTrace();
            }
            if(data.length()>3){
                //Getting data of valid URLs from JSON
                String link=data.get("Url").toString();
                String statusCode=data.get("Status_code").toString();
                String Content_length=data.get("Content_length").toString();
                String date=data.get("Date").toString();

                //Setting data to URLObject for each URL
                urlObject.setUrl(link);
                urlObject.setValid(true);
                urlObject.setStatusCode(Integer.parseInt(statusCode));
                urlObject.setContentLength(Long.parseLong(Content_length));
                urlObject.setDate(date);
            }else{
                //Getting data of invalid URLs from JSON
                String link=data.get("Url").toString();
                String error=data.get("Error").toString();

                //Setting data to URLObject for each URL
                urlObject.setUrl(link);
                urlObject.setError(error);
            }
            lstOfURLData.add(urlObject);
        }
        return lstOfURLData;
    }

    public Map<Integer,Integer> readResponse() {
        //Retrieve data from JSON file
        JSONArray JSONCodes=new JSONArray();
        try{
            String content = FileUtils.readFileToString(new File("Response_Count.json"), "utf-8");
            JSONCodes = new JSONArray(content);
        } catch (Exception e){
            System.err.println("The File does not exist");
            e.printStackTrace();
        }

        Map<Integer,Integer>responseCodes=new LinkedHashMap<>();
        for (int i=0;i<JSONCodes.length();i++){
            //Getting data of valid URLs from JSON
            Integer count=(Integer) ((JSONObject)JSONCodes.get(i)).get("Number_of_responses");
            Integer statusCode=(Integer) ((JSONObject)JSONCodes.get(i)).get("Status_code");

            //Setting data to URLObject for each URL
            responseCodes.put(statusCode,count);
        }
        return responseCodes;
    }

    @Override
    public void print(){
        //Retrieve data from JSON file
        JSONArray URLList=new JSONArray();
        try{
            String content = FileUtils.readFileToString(new File("Response_Count.json"), "utf-8");
            URLList = new JSONArray(content);
        } catch (Exception e){
            System.err.println("The File does not exist");
            e.printStackTrace();
        }
        System.out.println(URLList.toString(4));
    }

    @Override
    public void printLink(){
        //Retrieve data from JSON file
        JSONObject URLData=new JSONObject();
        File[]files=new File("LinkData").listFiles();
        for(File file:files){
            try{
                String content = FileUtils.readFileToString(file, "utf-8");
                URLData = new JSONObject(content);
            } catch (Exception e){
                System.err.println("The File does not exist");
                e.printStackTrace();
            }
            System.out.println(URLData.toString(4));
        }
    }
}