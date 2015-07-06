package com.hs_osnabrueck.swe_app.myapplication.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Event {

    private String date;
    private String title;
    private String link;
    private String description;
    private String category;


    public Event(String category, String date, String description, String link, String title) {
        this.category = category;
        DateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        Date date2 = null;
        try {
            date2 = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.date = date2.getDate() + "." +  (1 + date2.getMonth()) + "." + (1900 + date2.getYear());
        this.description = description;
        this.link = link;
        this.title = title;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLink() {return link;}

    public void setLink(String link) {this.link = link;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}
}
