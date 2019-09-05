package com.bbc.jsc;

import java.util.*;

public class UserInput {

    //------------ INSTANCE VARIABLES -------------
    private List<String>inputLst=new LinkedList<>();
    private String docType="";
    private final List<String>typeOfDocuments =Arrays.asList("json");

    public void UserGUI(){
        System.out.println("Please enter the list of URLs with each URL on a different line \n(When you are done writing URLs enter done)");
        Scanner scanner = new Scanner(System.in);
        String url=scanner.nextLine();
        while(!url.equals("done")) {
            inputLst.add(url);
            url=scanner.nextLine();
        }

        System.out.println("\nPlease enter the type of Document that you want to save the data in");
        System.out.println("\nThe options are: JSON");
        do docType = scanner.nextLine();
        while(!typeOfDocuments.contains(docType));
        setDocType(docType);
    }

    public void UserGUIClosing(){
        System.out.println("The JSON file has been created");
        System.out.println("Do you wish to view the contents of the file (yes or no)");
        Scanner scanner = new Scanner(System.in);
        String print;
        do print=scanner.nextLine();
        while(!(print.equalsIgnoreCase("yes")||print.equalsIgnoreCase("no")));
        if(print.equalsIgnoreCase("yes")) new JSONDocument().print();
        else System.out.println("Goodbye");
    }


    //------------ GETTERS & SETTERS -------------
    public void setInputLst(String[]links){
        inputLst.addAll(Arrays.asList(links));
    }

    public List<String> getInputLst(){
        return inputLst;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }
}
