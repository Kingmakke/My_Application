package com.hs_osnabrueck.swe_app.myapplication;

public class Veranstaltung {

    private String date;
    private String name;
    private String description;

    public Veranstaltung(String date, String name, String description){
        this.date = date;
        this.name = name;
        this.description = description;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
