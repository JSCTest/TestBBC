package com.bbc.jsc;

import java.util.*;

public class UserInput {

    //------------ INSTANCE VARIABLES -------------
    private List<String>inputLst=new LinkedList<>();
    private String input="";

    public void UserGUI(){
        System.out.println("Please enter the list of URLs");
        Scanner scanner = new Scanner(System.in);
        setInput(scanner.nextLine());
        setInputLst(getInput().split("\n"));
    }

    public void setInput(String input){
        this.input=input;
    }

    public String getInput() {
        return input;
    }

    public void setInputLst(String[]links){
        inputLst.addAll(Arrays.asList(links));
    }

    public List<String> getInputLst(){
        return inputLst;
    }
}
