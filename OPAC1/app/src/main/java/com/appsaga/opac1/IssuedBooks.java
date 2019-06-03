package com.appsaga.opac1;

import java.io.Serializable;

public class IssuedBooks implements Serializable {

    String name;
    String accession;
    String author_name;
    String publisher_name;

    public IssuedBooks(String name, String accession, String author_name, String publisher_name) {
        this.name = name;
        this.accession = accession;
        this.author_name = author_name;
        this.publisher_name = publisher_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getPublisher_name() {
        return publisher_name;
    }

    public void setPublisher_name(String publisher_name) {
        this.publisher_name = publisher_name;
    }
}
