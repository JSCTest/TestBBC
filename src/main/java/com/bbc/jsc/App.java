package com.bbc.jsc;

import java.util.*;

public class App {

    //------------ INSTANCE VARIABLES -------------
    private String input = "";
    private String[]inputLst;

    public static void main( String[] args ){
        System.out.println("Please enter the list of URLs");
        App app = new App();
        Scanner scanner = new Scanner(System.in);
        app.setInput(scanner.nextLine());
    }

    public void setInput(String input){
        this.input=input;
        this.inputLst=input.split("\n");
    }

    public String getInput(){
        return input;
    }

    public String[] getInputLst(){
        return inputLst;
    }
}
