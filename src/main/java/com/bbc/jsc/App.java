package com.bbc.jsc;

import java.util.List;

public class App {

    public void begin(){
        UserInput userInput = new UserInput();
        userInput.UserGUI();
        List<String>input=userInput.getInputLst();
        if(input.size()==0){
            System.out.println("No URLs entered\n\nGoodbye");
        }else {
            InputHandler handler = new InputHandler(input);

            Document document = new DocumentFactory().getDocument(userInput.getDocType());
            document.convert(handler.getURLData());

            userInput.UserGUIClosing(document);
        }
    }
}
