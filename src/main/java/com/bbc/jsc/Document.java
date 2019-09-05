package com.bbc.jsc;

import java.util.List;

public interface Document {
    public void convert(List<URLObject>lstOfURLData);
    public List<URLObject> read();
    public void print();
}
