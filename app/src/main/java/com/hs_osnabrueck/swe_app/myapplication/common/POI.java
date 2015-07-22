package com.hs_osnabrueck.swe_app.myapplication.common;

import java.util.Vector;

/**
 *
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
     *
     * @param beaconId
     * @param description
     * @param gps_latitude
     * @param gps_longitude
     * @param id
     * @param name
     * @param institut
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
     *
     * @param beaconId
     * @param course
     * @param description
     * @param gps_latitude
     * @param gps_longitude
     * @param id
     * @param imageLinks
     * @param name
     * @param webLinks
     * @param institut
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
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
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
    public double getGps_latitude() {
        return gps_latitude;
    }

    /**
     *
     * @param gps_latitude
     */
    public void setGps_latitude(double gps_latitude) {
        this.gps_latitude = gps_latitude;
    }

    /**
     *
     * @return
     */
    public double getGps_longitude() {
        return gps_longitude;
    }

    /**
     *
     * @param gps_longitude
     */
    public void setGps_longitude(double gps_longitude) {
        this.gps_longitude = gps_longitude;
    }

    /**
     *
     * @return
     */
    public Vector<String> getImageLinks() {
        return imageLinks;
    }

    /**
     *
     * @param imageLinks
     */
    public void setImageLinks(Vector<String> imageLinks) {
        this.imageLinks = imageLinks;
    }

    /**
     *
     * @return
     */
    public Vector<String> getWebLinks() {
        return webLinks;
    }

    /**
     *
     * @param webLinks
     */
    public void setWebLinks(Vector<String> webLinks) {
        this.webLinks = webLinks;
    }

    /**
     *
     * @return
     */
    public String getBeaconId() {
        return beaconId;
    }

    /**
     *
     * @param beaconId
     */
    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    /**
     *
     * @return
     */
    public Vector<Course> getCourse() {
        return course;
    }

    /**
     *
     * @param course
     */
    public void setCourse(Vector<Course> course) {
        this.course = course;
    }

    /**
     *
     * @return
     */
    public String getInstitut() {
        return institut;
    }

    /**
     *
     * @param institut
     */
    public void setInstitut(String institut) {
        this.institut = institut;
    }
}
