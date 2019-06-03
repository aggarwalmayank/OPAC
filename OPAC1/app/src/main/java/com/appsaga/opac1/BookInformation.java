package com.appsaga.opac1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookInformation implements Serializable {

    String name;
    int id;
    String author;
    String publisher;
    String call_no;
    HashMap<String,Copies> copies;

    public BookInformation(String name, int id, String author, String publisher, String call_no, HashMap<String, Copies> copies) {
        this.name = name;
        this.id = id;
        this.author = author;
        this.publisher = publisher;
        this.call_no = call_no;
        this.copies = copies;
    }

    public BookInformation()
    {

    }

   /* public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }*/

    public HashMap<String, Copies> getCopies() {
        return copies;
    }

    public void setCopies(HashMap<String, Copies> copies) {
        this.copies = copies;
    }

    public String getCall_no() {
        return call_no;
    }

    public void setCall_no(String call_no) {
        this.call_no = call_no;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
