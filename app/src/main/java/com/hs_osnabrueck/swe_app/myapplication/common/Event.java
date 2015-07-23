package com.hs_osnabrueck.swe_app.myapplication.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * this class defines how an event object looks like
 */
public class Event {

    private String date;
    private String title;
    private String link;
    private String description;
    private String category;
    private String content;

    /**
     * Event constructor (transorms the date to european format)
     * @param category event category
     * @param date event date
     * @param description event description
     * @param link event link
     * @param title event title
     */
    public Event(String category, String date, String description, String link, String title, String content) {
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
        this.content = content;
    }

    /**
     * returns the date
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * sets the date
     * @param date course date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * returns the description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * sets the description
     * @param description course description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * returns the category
     * @return category
     */
    public String getCategory() {
        return category;
    }

    /**
     * sets the category
     * @param category course category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * returns the link
     * @return link
     */
    public String getLink() {return link;}

    /**
     * sets the link
     * @param link course link
     */
    public void setLink(String link) {this.link = link;}

    /**
     * returns the title
     * @return title
     */
    public String getTitle() {return title;}

    /**
     * sets the title
     * @param title course title
     */
    public void setTitle(String title) {this.title = title;}

    /**
     * returns the content
     * @return content
     */
    public String getContent() {
        return content;
    }


    /**
     * sets the content
     * @param content course content
     */
    public void setContent(String content) {
        this.content = content;
    }
}
