package com.bbc.jsc;

import org.junit.*;
import junit.framework.TestCase;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class AppTest {

    String input = "http://www.bbc.co.uk/iplayer\n" +
            "https://google.com\n" +
            "bad://address\n" +
            "http://www.bbc.co.uk/missing/thing\n" +
            "http://not.exists.bbc.co.uk/\n" +
            "http://www.oracle.com/technetwork/java/javase/downloads/index.html";

    String[]inLst = {"http://www.bbc.co.uk/iplayer",
            "https://google.com",
            "bad://address",
            "http://www.bbc.co.uk/missing/thing",
            "http://not.exists.bbc.co.uk/",
            "http://www.oracle.com/technetwork/java/javase/downloads/index.html"};

    App app = new App();

    //test input to see if stored
    @Test
    public void testInput(){
        app.setInput(input);
        Assert.assertEquals(app.getInput(),input);
    }

    //test input to see if separated correctly
    @Test
    public void testInputLst(){
        app.setInput(input);
        Assert.assertEquals(app.getInputLst(),inLst);
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

    //test input to see if slow/non-responsive requests are handled
    @Test
    public void testSlowRequest(){

    }

    //test if properties from requests are recorded and are correct
    @Test
    public void testProperties(){
        try {
            //Connect to 1st URL
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
            List<String>properties=handler.getProperties(url);

            //Comparing values for the properties
            Assert.assertEquals(url,properties.get(0));
            Assert.assertEquals(true,Boolean.parseBoolean(properties.get(1)));
            Assert.assertEquals(code,Integer.parseInt(properties.get(2)));
            //Assert.assertEquals(conLength,Long.parseLong(properties.get(3)));
            Assert.assertEquals(date,Long.parseLong(properties.get(4)));

            //Connect to 2nd URL
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
            Assert.assertEquals(url,properties.get(0));
            Assert.assertEquals(true,Boolean.parseBoolean(properties.get(1)));
            Assert.assertEquals(code,Integer.parseInt(properties.get(2)));
            //Assert.assertEquals(conLength,Long.parseLong(properties.get(3)));
            Assert.assertEquals(date,Long.parseLong(properties.get(4)));

            //Connect to 3rd URL
            //Get Property value from method
            properties=handler.getProperties("bad://address");

            //Comparing values for the properties
            Assert.assertEquals("bad://address",properties.get(0));
            Assert.assertEquals(false,Boolean.parseBoolean(properties.get(1)));
            Assert.assertEquals("Invalid URL",properties.get(2));

        }catch(Exception e){
            System.err.println("The URL is Invalid");
        }

    }

    //test to see if json file exists
    @Test
    public void testJSON(){

    }

}
