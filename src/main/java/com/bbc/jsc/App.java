package com.bbc.jsc;

public class App {

    public void begin(){
        UserInput userInput = new UserInput();
        userInput.UserGUI();

        InputHandler handler = new InputHandler(userInput.getInputLst());
        handler.createJSON();
    }
}
