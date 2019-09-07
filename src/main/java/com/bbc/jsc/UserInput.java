package com.bbc.jsc;

import java.util.*;

public class UserInput {

    //------------ INSTANCE VARIABLES -------------
    private List<String>inputLst=new LinkedList<>();
    private String docType="";
    private final List<String>typeOfDocuments =Arrays.asList("json");

    public void UserGUI(){
        System.out.println("Please enter the list of URLs with each URL on a different line \n(When you are done writing URLs enter 'done')");
        Scanner scanner = new Scanner(System.in);
        String url=scanner.nextLine();
        while(!url.equals("done")) {
            if(!(url.equals("")||inputLst.contains(url))) inputLst.add(url);
            url=scanner.nextLine();
        }

        System.out.println("\nPlease enter the type of Document that you want to save the data in");
        System.out.println("The options are: JSON");
        do docType = scanner.nextLine();
        while(!typeOfDocuments.contains(docType));
        setDocType(docType);
    }

    public void UserGUIClosing(Document document){
        System.out.println("\n\nThe JSON files have been created");
        System.out.println("\nDo you wish to view the contents of the files (yes or no)");
        Scanner scanner = new Scanner(System.in);
        String print;
        do print=scanner.nextLine();
        while(!(print.equalsIgnoreCase("yes")||print.equalsIgnoreCase("no")));
        System.out.println();
        if(print.equalsIgnoreCase("yes")) document.printLink();

        System.out.println("\n\n---------------------------\n\nDo you wish to view the contents of the Response count file");
        do print=scanner.nextLine();
        while(!(print.equalsIgnoreCase("yes")||print.equalsIgnoreCase("no")));
        System.out.println();
        if(print.equalsIgnoreCase("yes")) document.print();
        System.out.println("\nGoodbye");
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
