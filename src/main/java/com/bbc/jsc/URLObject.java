package com.bbc.jsc;

public class URLObject {

    //------------ INSTANCE VARIABLES -------------
    private String url;
    private int statusCode;
    private long contentLength;
    private String date;
    private String error="No Error";
    private boolean isValid;

    //------------ CONSTRUCTORS -------------
    public URLObject(){}

    public URLObject(String url, int statusCode, long contentLength, String date, boolean isValid) {
        this.url = url;
        this.statusCode = statusCode;
        this.contentLength = contentLength;
        this.date = date;
        this.isValid = isValid;
    }

    public URLObject(String url, boolean isValid, String error) {
        this.url = url;
        this.isValid = isValid;
        this.error=error;
    }

    //------------ GETTERS & SETTERS -------------
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean getValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }


}
