package com.bbc.jsc;

import org.apache.commons.io.FileUtils;
import org.junit.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class AppTest {

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
    private List<String>smallLst = Arrays.asList("http://www.bbc.co.uk/iplayer",
                                                 "https://google.com",
                                                 "bad://address");
    // test getters and setters of UserInput class
    @Test
    public void testInputLstGetSet(){
        UserInput userInput = new UserInput();
        userInput.setInputLst(inArray);
        userInput.setDocType("json");
        Assert.assertEquals(inLst,userInput.getInputLst());
        Assert.assertEquals("json",userInput.getDocType());
    }

    // test user input to see if stored
    @Test
    public void testInputLst(){
        UserInput userInput = new UserInput();
        String input="http://www.bbc.co.uk/iplayer\nhttps://google.com\nbad://address\nhttp://www.bbc.co.uk/missing/thing\nhttp://not.exists.bbc.co.uk/\nhttp://www.oracle.com/technetwork/java/javase/downloads/index.html\ndone\njson";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        userInput.UserGUI();
        System.setIn(System.in);
        Assert.assertEquals(inLst,userInput.getInputLst());
        Assert.assertEquals("json",userInput.getDocType());
    }

    // test empty input for URL
    @Test
    public void testEmptyURL(){
        UserInput userInput = new UserInput();
        String input="http://www.bbc.co.uk/iplayer\n\nhttps://google.com\nbad://address\n\n\nhttp://www.bbc.co.uk/missing/thing\nhttp://not.exists.bbc.co.uk/\nhttp://www.oracle.com/technetwork/java/javase/downloads/index.html\n\ndone\njson";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        userInput.UserGUI();
        System.setIn(System.in);
        Assert.assertEquals(inLst,userInput.getInputLst());
    }

    // test duplicate input for URL
    @Test
    public void testDuplicateURL(){
        UserInput userInput = new UserInput();
        String input="http://www.bbc.co.uk/iplayer\nhttps://google.com\nbad://address\nhttps://google.com\nhttp://www.bbc.co.uk/missing/thing\nhttp://not.exists.bbc.co.uk/\nhttp://www.oracle.com/technetwork/java/javase/downloads/index.html\n" +
                "http://www.bbc.co.uk/missing/thing\n" +
                "http://www.bbc.co.uk/missing/thing\nbad://address\ndone\njson";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        userInput.UserGUI();
        System.setIn(System.in);
        Assert.assertEquals(inLst,userInput.getInputLst());
    }

    //test input to see if invalid input is spotted
    @Test
    public void testInvalidInput(){
        InputHandler handler =new InputHandler(inLst);
        Assert.assertEquals(false,handler.isValidURL("bad://address"));
        Assert.assertEquals(false,handler.isValidURL("https:// www.google.com"));
    }

    //test input to see if valid input is spotted
    @Test
    public void testValidInput(){
        InputHandler handler =new InputHandler(inLst);
        Assert.assertEquals(true,handler.isValidURL("https://google.com"));
        Assert.assertEquals(true,handler.isValidURL("http://www.bbc.co.uk/iplayer"));
        Assert.assertEquals(true,handler.isValidURL("http://www.oracle.com/technetwork/java/javase/downloads/index.html"));
    }

    //test URLObject class
    @Test
    public void testURLObject(){
        String date=new Date().toString();
        URLObject validUrlObject = new URLObject("https://google.com",200,1234,date,true);
        URLObject invalidUrlObject = new URLObject("bad://address",false,"Invalid URL");

        Assert.assertEquals("https://google.com",validUrlObject.getUrl());
        Assert.assertEquals(200,validUrlObject.getStatusCode());
        Assert.assertEquals(1234,validUrlObject.getContentLength());
        Assert.assertEquals(date,validUrlObject.getDate());
        Assert.assertEquals(true,validUrlObject.getValid());

        Assert.assertEquals("bad://address",invalidUrlObject.getUrl());
        Assert.assertEquals(false,invalidUrlObject.getValid());
        Assert.assertEquals("Invalid URL",invalidUrlObject.getError());
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

            //Get Property value from method
            InputHandler handler =new InputHandler(inLst);
            URLObject properties=handler.getProperties(url);

            //Comparing values for the properties
            Assert.assertEquals(url,properties.getUrl());
            Assert.assertEquals(true,properties.getValid());
            Assert.assertEquals(code,properties.getStatusCode());



            //---------------- Connect to 2nd URL ----------------
            url = "https://google.com";
            link = new URL(url);
            connection = (HttpURLConnection) link.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            //Get Properties
            code=connection.getResponseCode();

            //Get Property value from method
            properties=handler.getProperties(url);

            //Comparing values for the properties
            Assert.assertEquals(url,properties.getUrl());
            Assert.assertEquals(true,properties.getValid());
            Assert.assertEquals(code,properties.getStatusCode());


            //---------------- Connect to 3rd URL ----------------
            //Get Property value from method
            properties=handler.getProperties("bad://address");

            //Comparing values for the properties
            Assert.assertEquals("bad://address",properties.getUrl());
            Assert.assertEquals(false,properties.getValid());
            Assert.assertEquals("Invalid URL",properties.getError());

        }catch(Exception e){
            System.err.println("The URL is Invalid");
        }

    }

    //test DocFactory class to see if correct object returned
    @Test
    public void testDocFactory(){
        Document document=new DocumentFactory().getDocument("json");
        boolean isCorrect=false;
        if(document instanceof JSONDocument) isCorrect=true;
        Assert.assertEquals(true,isCorrect);
    }

    //test to see if json file exists
    @Test
    public void testJSONExists(){

        //Get the data JSON file should contain
        String date=new Date().toString();
        List<URLObject>lstOfProperties=new LinkedList<>();
        URLObject validUrlObject = new URLObject("https://google.com",200,1234,date,true);
        URLObject validUrlObject2 = new URLObject("https://www.bbc.co.uk/iplayer",300,134,date,true);
        URLObject invalidUrlObject = new URLObject("bad://address",false,"Invalid URL");
        lstOfProperties.add(validUrlObject);
        lstOfProperties.add(validUrlObject2);
        lstOfProperties.add(invalidUrlObject);

        //test to make sure response file doesn't exist yet
        File file = new File("Response_Count.json");
        file.delete();
        Assert.assertEquals(false,file.exists());

        //test to make sure LinkData directory is empty
        File fileURLs = new File("LinkData");
        try {
            FileUtils.cleanDirectory(fileURLs);
        } catch(IllegalArgumentException e){
            System.err.println("Directory doesn't exist\nCreating directory");
            fileURLs.mkdir();
        }
        catch (IOException e) {
            System.err.println("Unsuccessful cleaning of directory");
        }
        Assert.assertEquals(0,fileURLs.listFiles().length);

        //Create the JSON Documents
        JSONDocument jsonDocument=new JSONDocument();
        jsonDocument.convert(lstOfProperties);

        //test to see if files have been created
        Assert.assertEquals(true,file.exists());
        Assert.assertEquals(3,fileURLs.listFiles().length);
    }






    //test to see if json file contents
    @Test
    public void testJSONRead(){

        //Get the data JSON file should contain
        String date=new Date().toString();
        List<URLObject>lstOfProperties=new LinkedList<>();
        URLObject validUrlObject = new URLObject("https://google.com",200,1234,date,true);
        URLObject validUrlObject2 = new URLObject("https://www.bbc.co.uk/iplayer",300,134,date,true);
        URLObject invalidUrlObject = new URLObject("bad://address",false,"Invalid URL");
        lstOfProperties.add(validUrlObject);
        lstOfProperties.add(validUrlObject2);
        lstOfProperties.add(invalidUrlObject);

        //Create the JSON Document
        Document jsonDocument=new JSONDocument();
        jsonDocument.convert(lstOfProperties);
        List<URLObject>lstOfJSONLinks= jsonDocument.read();

        //Check if data from JSON file matches the actual data
        for (int i=0;i<lstOfProperties.size();i++){
            if(lstOfProperties.get(i).getValid()){
                //Comparing values for the properties
                Assert.assertEquals(lstOfProperties.get(i).getUrl(),lstOfJSONLinks.get(i).getUrl());
                Assert.assertEquals(lstOfProperties.get(i).getStatusCode(),lstOfJSONLinks.get(i).getStatusCode());
                Assert.assertEquals(lstOfProperties.get(i).getContentLength(),lstOfJSONLinks.get(i).getContentLength());
                Assert.assertEquals(lstOfProperties.get(i).getDate(),lstOfJSONLinks.get(i).getDate());
            }else{
                //Comparing values for the properties
                Assert.assertEquals(lstOfProperties.get(i).getUrl(),lstOfJSONLinks.get(i).getUrl());
                Assert.assertEquals(lstOfProperties.get(i).getError(),lstOfJSONLinks.get(i).getError());
            }
        }
    }

    //test to see if json file contents
    @Test
    public void testJSONResponseRead(){

        //Creating data for JSON files
        String date=new Date().toString();
        List<URLObject>lstOfProperties=new LinkedList<>();
        URLObject validUrlObject = new URLObject("https://google.com",200,1234,date,true);
        URLObject validUrlObject2 = new URLObject("https://www.bbc.co.uk/iplayer",300,134,date,true);
        URLObject validUrlObject3 = new URLObject("http://www.oracle.com/technetwork/java/javase/downloads/index.html",300,1344,date,true);
        URLObject invalidUrlObject = new URLObject("bad://address",false,"Invalid URL");
        lstOfProperties.add(validUrlObject);
        lstOfProperties.add(validUrlObject2);
        lstOfProperties.add(invalidUrlObject);

        //Creating map to contain data response doc should contain
        Map<Integer,Integer>expResponse=new LinkedHashMap<>();
        expResponse.put(200,1);
        expResponse.put(300,1);

        //Create the JSON Document
        JSONDocument jsonDocument=new JSONDocument();
        jsonDocument.convert(lstOfProperties);
        Map<Integer,Integer>lstOfResponse= jsonDocument.readResponse();

        //Check if data from JSON file matches the actual data
        for (int code:expResponse.keySet()){
            Assert.assertEquals(expResponse.get(code),lstOfResponse.get(code));
        }
    }

}
