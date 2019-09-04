package com.bbc.jsc;

import org.junit.*;
import junit.framework.TestCase;

import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.commons.io.FileUtils;

public class AppTest {

    private String input = "http://www.bbc.co.uk/iplayer\n" +
                           "https://google.com\n" +
                           "bad://address\n" +
                           "http://www.bbc.co.uk/missing/thing\n" +
                           "http://not.exists.bbc.co.uk/\n" +
                           "http://www.oracle.com/technetwork/java/javase/downloads/index.html";

    private String[]inArray = {"http://www.bbc.co.uk/iplayer",
                               "https://google.com",
                               "bad://address",
                               "http://www.bbc.co.uk/missing/thing",
                               "http://not.exists.bbc.co.uk/",
                               "http://www.oracle.com/technetwork/java/javase/downloads/index.html"};

    private List<String>inLst = Arrays.asList("http://www.bbc.co.uk/iplayer",
                                              "https://google.com",
                                              "bad://address",
                                              "http://www.bbc.co.uk/missing/thing",
                                              "http://not.exists.bbc.co.uk/",
                                              "http://www.oracle.com/technetwork/java/javase/downloads/index.html");

    //test input to see if stored
    @Test
    public void testInput(){
        UserInput userInput = new UserInput();
        userInput.setInput(input);
        Assert.assertEquals(input,userInput.getInput());
    }

    //test input to see if separated correctly
    @Test
    public void testInputLst(){
        UserInput userInput = new UserInput();
        userInput.setInputLst(inArray);
        Assert.assertEquals(inLst,userInput.getInputLst());
    }

    //test input to see if invalid input is spotted
    @Test
    public void testInvalidInput(){
        InputHandler handler =new InputHandler(inLst);
        Assert.assertEquals(false,handler.isURLValid("bad://address"));
        Assert.assertEquals(false,handler.isURLValid("https:// www.google.com"));
    }

    //test input to see if valid input is spotted
    @Test
    public void testValidInput(){
        InputHandler handler =new InputHandler(inLst);
        Assert.assertEquals(handler.isURLValid("https://google.com"),true);
        Assert.assertEquals(handler.isURLValid("http://www.bbc.co.uk/iplayer"),true);
        Assert.assertEquals(handler.isURLValid("http://www.oracle.com/technetwork/java/javase/downloads/index.html"),true);
    }





    //test input to see if valid input is spotted
    @Test
    public void testMixedInput(){
        InputHandler handler =new InputHandler(inLst);
        Assert.assertEquals(handler.isURLValid("https://google.com"),true);
        Assert.assertEquals(handler.isURLValid("http://www.bbc.co.uk/iplayer"),true);
        Assert.assertEquals(handler.isURLValid("http://www.oracle.com/technetwork/java/javase/downloads/index.html"),true);
    }





    //test input to see if slow/non-responsive requests are handled
    @Test
    public void testSlowRequest(){

    }

    //test if properties from requests are recorded and are correct
    @Test
    public void testProperties(){
        try {
            //---------------- Connect to 1st URL ----------------
            String url = "https://www.bbc.co.uk/iplayer";
            URL link = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) link.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            //Get Properties
            int code=connection.getResponseCode();
            long conLength=connection.getContentLengthLong();
            long date = connection.getDate();

            //Get Property value from method
            InputHandler handler =new InputHandler(inLst);
            Map<String, String> properties=handler.getProperties(url);

            //Comparing values for the properties
            Assert.assertEquals(url,properties.get("Url"));
            Assert.assertEquals(true,Boolean.parseBoolean(properties.get("Valid")));
            Assert.assertEquals(code,Integer.parseInt(properties.get("Status_code")));
            //Assert.assertEquals(conLength,Long.parseLong(properties.get("Content_length")));
            Assert.assertEquals(date,Long.parseLong(properties.get("Date")));


            //---------------- Connect to 2nd URL ----------------
            url = "https://google.com";
            link = new URL(url);
            connection = (HttpURLConnection) link.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            //Get Properties
            code=connection.getResponseCode();
            conLength=connection.getContentLengthLong();
            date = connection.getDate();

            //Get Property value from method
            properties=handler.getProperties(url);

            //Comparing values for the properties
            Assert.assertEquals(url,properties.get("Url"));
            Assert.assertEquals(true,Boolean.parseBoolean(properties.get("Valid")));
            Assert.assertEquals(code,Integer.parseInt(properties.get("Status_code")));
            //Assert.assertEquals(conLength,Long.parseLong(properties.get("Content_length")));
            Assert.assertEquals(date,Long.parseLong(properties.get("Date")));


            //---------------- Connect to 3rd URL ----------------
            //Get Property value from method
            properties=handler.getProperties("bad://address");

            //Comparing values for the properties
            Assert.assertEquals("bad://address",properties.get("Url"));
            Assert.assertEquals(false,Boolean.parseBoolean(properties.get("Valid")));
            Assert.assertEquals("Invalid URL",properties.get("Error"));

        }catch(Exception e){
            System.err.println("The URL is Invalid");
        }

    }

    //test to see if json file exists
    @Test
    public void testJSON(){
        //Get the data JSON file should contain
        List<String>links = Arrays.asList("https://www.bbc.co.uk/iplayer","https://google.com","bad://address");
        InputHandler handler =new InputHandler(links);
        List<Map<String,String>>lstOfProperties=new LinkedList();
        for(String url:links) {
            Map<String, String> properties = handler.getProperties(url);
            lstOfProperties.add(properties);
            handler.toJSON(properties);
        }

        //Create the JSON Document
        handler.createJSONDocument();

        //Check if the file has been created
        File file = new File("LinkProperties.json");
        Assert.assertEquals(true,file.exists());

        //Retrieve data from JSON file
        String content="";
        JSONArray URLList=new JSONArray();
        try{
            content = FileUtils.readFileToString(file, "utf-8");
            URLList = new JSONArray(content);
        } catch (Exception e){
            System.err.println("The File does not exist");
            e.printStackTrace();
        }

        //Check if data from JSON file matches the actual data
        for (int i=0;i<lstOfProperties.size();i++){
            if(((JSONObject)URLList.get(i)).length()>3){
                String link=((JSONObject)URLList.get(i)).get("Url").toString();
                String statusCode=((JSONObject)URLList.get(i)).get("Status_code").toString();
                String Content_length=((JSONObject)URLList.get(i)).get("Content_length").toString();
                String date=((JSONObject)URLList.get(i)).get("Date").toString();

                //Comparing values for the properties
                Assert.assertEquals(lstOfProperties.get(i).get("Url"),link);
                Assert.assertEquals(lstOfProperties.get(i).get("Status_code"),statusCode);
                Assert.assertEquals(lstOfProperties.get(i).get("Content_length"),Content_length);
                Assert.assertEquals(lstOfProperties.get(i).get("Date"),date);
            }else{
                String link=((JSONObject)URLList.get(i)).get("Url").toString();
                String error=((JSONObject)URLList.get(i)).get("Error").toString();

                //Comparing values for the properties
                Assert.assertEquals(lstOfProperties.get(i).get("Url"),link);
                Assert.assertEquals(lstOfProperties.get(i).get("Error"),error);
            }
        }
    }

}
