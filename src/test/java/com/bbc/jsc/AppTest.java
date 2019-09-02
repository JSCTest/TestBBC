package com.bbc.jsc;

import org.junit.*;
import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest {

    //test input to see if stored
    @Test
    public void testInput(){
        String input = "    * http://www.bbc.co.uk/iplayer\n" +
                "https://google.com\n" +
                "bad://address\n" +
                "http://www.bbc.co.uk/missing/thing\n" +
                "http://not.exists.bbc.co.uk/\n" +
                "http://www.oracle.com/technetwork/java/javase/downloads/index.html";
        Assert.assertEquals(App.getInput(),input);
    }

    //test input to see if separated correctly
    @Test
    public void testInputLst(){
        String[]input = {"    * http://www.bbc.co.uk/iplayer",
                "https://google.com",
                "bad://address",
                "http://www.bbc.co.uk/missing/thing",
                "http://not.exists.bbc.co.uk/",
                "http://www.oracle.com/technetwork/java/javase/downloads/index.html"};
        Assert.assertEquals(App.getInputLst(),input);
    }

    //test input to see if invalid input is spotted
    @Test
    public void testInvalidInput(){

    }

    //test input to see if valid input is spotted
    @Test
    public void testValidInput(){

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
