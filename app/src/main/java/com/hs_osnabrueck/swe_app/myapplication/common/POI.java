package com.hs_osnabrueck.swe_app.myapplication.common;

import java.util.Vector;

/**
 * this class defines how a Point of Interest looks like
 */
public class POI {

    private int id;
    private String name;
    private String description;
    private double gps_latitude;
    private double gps_longitude;
    private String institut;
    private Vector<String> imageLinks;
    private Vector<String> webLinks;
    private String beaconId;
    private Vector<Course> course;

    /**
     * Point of Interest constructor
     * @param beaconId POI beaconID
     * @param description POI description
     * @param gps_latitude POI latitude
     * @param gps_longitude POI longitude
     * @param id POI id
     * @param name POI name
     * @param institut POI institut
     */
    public POI(String beaconId, String description, double gps_latitude, double gps_longitude, int id, String name, String institut) {
        this.beaconId = beaconId;
        this.description = description;
        this.gps_latitude = gps_latitude;
        this.gps_longitude = gps_longitude;
        this.id = id;
        this.name = name;
        this.institut = institut;
    }

    /**
     * Point of Interest constructor
     * @param beaconId POI beaconID
     * @param course POI course
     * @param description POI description
     * @param gps_latitude POI latitude
     * @param gps_longitude POI longitude
     * @param id POI id
     * @param imageLinks POI imagelinks
     * @param name POI name
     * @param webLinks POI weblinks
     * @param institut POI institut
     */
    public POI(String beaconId, Vector<Course> course, String description, double gps_latitude, double gps_longitude, int id, Vector<String> imageLinks, String name, Vector<String> webLinks, String institut) {
        this.beaconId = beaconId;
        this.course = course;
        this.description = description;
        this.gps_latitude = gps_latitude;
        this.gps_longitude = gps_longitude;
        this.id = id;
        this.imageLinks = imageLinks;
        this.name = name;
        this.webLinks = webLinks;
        this.institut = institut;
    }

    /**
     * returns the id
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * sets the id
     * @param id POI id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * returns the name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * sets the name
     * @param name POI name
     */
    public void setName(String name) {
        this.name = name;
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
     * @param description POI description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * returns the latitude
     * @return latitude
     */
    public double getGps_latitude() {
        return gps_latitude;
    }

    /**
     * sets the latitude
     * @param gps_latitude POI latitude
     */
    public void setGps_latitude(double gps_latitude) {
        this.gps_latitude = gps_latitude;
    }

    /**
     * returns the longitude
     * @return longitude
     */
    public double getGps_longitude() {
        return gps_longitude;
    }

    /**
     * sets the longitude
     * @param gps_longitude POI longitude
     */
    public void setGps_longitude(double gps_longitude) {
        this.gps_longitude = gps_longitude;
    }

    /**
     * returns the imagelinks
     * @return imagelinks
     */
    public Vector<String> getImageLinks() {
        return imageLinks;
    }

    /**
     * sets the imagelinks
     * @param imageLinks POI imagelinks
     */
    public void setImageLinks(Vector<String> imageLinks) {
        this.imageLinks = imageLinks;
    }

    /**
     * returns the weblinks
     * @return weblinks
     */
    public Vector<String> getWebLinks() {
        return webLinks;
    }

    /**
     * sets the weblinks
     * @param webLinks POI weblinks
     */
    public void setWebLinks(Vector<String> webLinks) {
        this.webLinks = webLinks;
    }

    /**
     * returns the beaconID
     * @return beaconID
     */
    public String getBeaconId() {
        return beaconId;
    }

    /**
     * sets the beaconID
     * @param beaconId POI beaconID
     */
    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    /**
     * returns the course
     * @return course
     */
    public Vector<Course> getCourse() {
        return course;
    }

    /**
     * sets the course
     * @param course POI course
     */
    public void setCourse(Vector<Course> course) {
        this.course = course;
    }

    /**
     * returns the institut
     * @return institut
     */
    public String getInstitut() {
        return institut;
    }

    /**
     * sets the institut
     * @param institut POI institut
     */
    public void setInstitut(String institut) {
        this.institut = institut;
    }
}
