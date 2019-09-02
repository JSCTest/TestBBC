package com.bbc.jsc;

/**
 * Created by Jagjeet on 02/09/2019.
 */

import java.net.URL;
import org.apache.commons.validator.*;
public class InputHandler {
    private String[]lstOfURLs;

    public InputHandler(String[]lstOfURLs){
        this.lstOfURLs=lstOfURLs;
    }

    public void checkURLs(){
        for(String url:lstOfURLs){
            if(isURLValid(url)){

            }else{

            }
        }

    }

    public boolean isURLValid(String url){
        UrlValidator urlValidator = new UrlValidator();
        if(urlValidator.isValid(url)) return true;
        else System.err.println("The URL \""+url +"\" is invalid ");
        return false;
    }
}
