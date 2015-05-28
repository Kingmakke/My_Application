package com.hs_osnabrueck.swe_app.myapplication.common;

import java.util.Vector;

public class POI {

    private int id;
    private String name;
    private String description;
    private double gps_latitude;
    private double gps_longitude;
    private Vector<String> imageLinks;
    private Vector<String> webLinks;
    private Vector<String> image;
    private Beacon beacon;
    private Vector<Course> course;

    public POI(String json){
        json = json.substring(1,json.length()-1);
        String[] result = json.split(",");
        String temp = "";
        for(int i = 0; i < result.length; i++){
            result[i] = result[i].substring(result[i].indexOf(":")+1);
            result[i] = result[i].replaceAll("\"", "");
        }
        this.id = Integer.parseInt(result[0]);
        this.beacon = new Beacon("SensorTag", result[1], -120);
        this.name = result[2];
        this.description = result[3];
        this.gps_latitude = Double.parseDouble(result[4]);
        this.gps_longitude = Double.parseDouble(result[5]);


    }

    public POI(Beacon beacon, Vector<Course> course, String description, double gps_latitude, double gps_longitude, int id, Vector<String> image, Vector<String> imageLinks, String name, Vector<String> webLinks) {
        this.beacon = beacon;
        this.course = course;
        this.description = description;
        this.gps_latitude = gps_latitude;
        this.gps_longitude = gps_longitude;
        this.id = id;
        this.image = image;
        this.imageLinks = imageLinks;
        this.name = name;
        this.webLinks = webLinks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getGps_latitude() {
        return gps_latitude;
    }

    public void setGps_latitude(double gps_latitude) {
        this.gps_latitude = gps_latitude;
    }

    public double getGps_longitude() {
        return gps_longitude;
    }

    public void setGps_longitude(double gps_longitude) {
        this.gps_longitude = gps_longitude;
    }

    public Vector<String> getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(Vector<String> imageLinks) {
        this.imageLinks = imageLinks;
    }

    public Vector<String> getWebLinks() {
        return webLinks;
    }

    public void setWebLinks(Vector<String> webLinks) {
        this.webLinks = webLinks;
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }

    public Vector<String> getImage() {
        return image;
    }

    public void setImage(Vector<String> image) {
        this.image = image;
    }

    public Vector<Course> getCourse() {
        return course;
    }

    public void setCourse(Vector<Course> course) {
        this.course = course;
    }
}
