package com.bbc.jsc;

import org.junit.*;
import junit.framework.TestCase;

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
        InputHandler handler =new InputHandler();
        Assert.assertEquals(handler.isValid("bad://address"),false);
        Assert.assertEquals(handler.isValid("http://not.exists.bbc.co.uk/"),false);
        Assert.assertEquals(handler.isValid("https:// www.google.com"),false);
    }

    //test input to see if valid input is spotted
    @Test
    public void testValidInput(){
        InputHandler handler =new InputHandler();
        Assert.assertEquals(handler.isValid("https://google.com"),true);
        Assert.assertEquals(handler.isValid("http://www.bbc.co.uk/iplayer"),true);
        Assert.assertEquals(handler.isValid("http://www.oracle.com/technetwork/java/javase/downloads/index.html"),true);
    }

    //test input to see if slow/non-responsive requests are handled
    @Test
    public void testSlowRequest(){

    }

    //test if properties from requests are recorded and are correct
    @Test
    public void testProperties(){

    }

    //test to see if json file exists
    @Test
    public void testJSON(){

    }

}
