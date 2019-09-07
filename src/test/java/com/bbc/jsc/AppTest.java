package com.bbc.jsc;

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

    // test user input to see if stored
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
        Assert.assertEquals(false,handler.getProperties("bad://address").getValid());
        Assert.assertEquals(false,handler.getProperties("https:// www.google.com").getValid());
    }

    //test input to see if valid input is spotted
    @Test
    public void testValidInput(){
        InputHandler handler =new InputHandler(inLst);
        Assert.assertEquals(true,handler.getProperties("https://google.com").getValid());
        Assert.assertEquals(true,handler.getProperties("http://www.bbc.co.uk/iplayer").getValid());
        Assert.assertEquals(true,handler.getProperties("http://www.oracle.com/technetwork/java/javase/downloads/index.html").getValid());
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
            String date = new Date(connection.getDate()+1).toString();

            //Get Property value from method
            InputHandler handler =new InputHandler(inLst);
            URLObject properties=handler.getProperties(url);

            //Comparing values for the properties
            Assert.assertEquals(url,properties.getUrl());
            Assert.assertEquals(true,properties.getValid());
            Assert.assertEquals(code,properties.getStatusCode());
            Assert.assertEquals(date,properties.getDate());


            //---------------- Connect to 2nd URL ----------------
            url = "https://google.com";
            link = new URL(url);
            connection = (HttpURLConnection) link.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            //Get Properties
            code=connection.getResponseCode();
            date = new Date(connection.getDate()).toString();

            //Get Property value from method
            properties=handler.getProperties(url);

            //Comparing values for the properties
            Assert.assertEquals(url,properties.getUrl());
            Assert.assertEquals(true,properties.getValid());
            Assert.assertEquals(code,properties.getStatusCode());
            Assert.assertEquals(date,properties.getDate());


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

    //test to see if json file exists
    @Test
    public void testJSON(){
        //Get the data JSON file should contain
        List<String>links = Arrays.asList("https://www.bbc.co.uk/iplayer","https://google.com","bad://address");
        InputHandler handler =new InputHandler(links);
        List<URLObject>lstOfProperties=new LinkedList<>();
        for(String url:links) {
            URLObject urlObject = handler.getProperties(url);
            lstOfProperties.add(urlObject);
        }

        //test to make sure file doesn't exist yet
        File file = new File("Response_Count.json");
        file.delete();
        Assert.assertEquals(false,file.exists());

        //Create the JSON Document
        JSONDocument jsonDocument=new JSONDocument();
        jsonDocument.convert(lstOfProperties);

        //test to see if file has been created
        Assert.assertEquals(true,file.exists());
    }

    //test to see if json file exists
    @Test
    public void testJSONRead(){
        //Get the data JSON file should contain
        List<String>links = Arrays.asList("https://www.bbc.co.uk/iplayer","https://google.com","bad://address");
        InputHandler handler =new InputHandler(links);
        List<URLObject>lstOfProperties=new LinkedList<>();
        for(String url:links) {
            URLObject urlObject = handler.getProperties(url);
            lstOfProperties.add(urlObject);
        }

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

}
