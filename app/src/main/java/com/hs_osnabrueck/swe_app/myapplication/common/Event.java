package com.hs_osnabrueck.swe_app.myapplication.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 */
public class Event {

    private String date;
    private String title;
    private String link;
    private String description;
    private String category;

    /**
     *
     * @param category
     * @param date
     * @param description
     * @param link
     * @param title
     */
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

    /**
     *
     * @return
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     */
    public String getCategory() {
        return category;
    }

    /**
     *
     * @param category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     *
     * @return
     */
    public String getLink() {return link;}

    /**
     *
     * @param link
     */
    public void setLink(String link) {this.link = link;}

    /**
     *
     * @return
     */
    public String getTitle() {return title;}

    /**
     *
     * @param title
     */
    public void setTitle(String title) {this.title = title;}
}
