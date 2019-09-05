package com.bbc.jsc;

public class DocumentFactory {
    public Document getDocument(String docType){
        switch (docType.toUpperCase()){
            case "JSON":
                return new JSONDocument();
            default:
                return null;
        }
    }
}
