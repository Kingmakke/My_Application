package com.hs_osnabrueck.swe_app.myapplication.common;

/**
 * Building class defines how a building object looks like
 */
public class Building {
    private int id;
    private String name;
    private String description;
    private double latitude;
    private double longitude;

    /**
     * Building constructor
     * @param id building id
     * @param name building name
     * @param description building description
     * @param latitude building latitude
     * @param longitude building longitude
     */
    public Building(int id, String name, String description,  double latitude, double longitude) {
        this.description = description;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
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
     * @param description building description
     */
    public void setDescription(String description) {
        this.description = description;
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
     * @param id building id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * returns the latitude
     * @return latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * sets the latitude
     * @param latitude building latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * returns the longitude
     * @return longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * sets the longitude
     * @param longitude building longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     *returns the name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * sets the name
     * @param name building name
     */
    public void setName(String name) {
        this.name = name;
    }
}
