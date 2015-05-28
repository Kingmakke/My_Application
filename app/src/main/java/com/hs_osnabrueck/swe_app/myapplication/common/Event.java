package com.hs_osnabrueck.swe_app.myapplication.common;

public class Event {

    private String date;
    private String title;
    private String link;
    private String description;
    private String category;
    private String content;

    public Event(String json){
        json = json.substring(1,json.length()-1);
        String[] result = json.split("\",\"");
        for(int i = 0; i < result.length; i++){
            result[i] = result[i].substring(result[i].indexOf(":\"") + 1);
            result[i] = result[i].replaceAll("\"", "");
        }

        this.title = result[0];
        this.link = result[1];
        this.description = result[2];
        this.content = result[3];
        this.category = result[4];
        //this.date = result[5];
        this.date = "5-4-2015";

    }

    public String getName() {return title;}

    public void setName(String name) {this.title = name;}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return content;
    }

    public void setDescription(String description) {
        this.content = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() { return content;}

    public void setContent(String content) {this.content = content;}

    public String getLink() {return link;}

    public void setLink(String link) {this.link = link;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}
}
