package com.hs_osnabrueck.swe_app.myapplication.common;

/**
 * this class defines how a course class looks like
 */
public class Course {
    private int id;
    private String name;
    private String description;

    /**
     * Course constructor
     * @param description
     * @param id
     * @param name
     */
    public Course(String description, int id, String name) {
        this.description = description;
        this.id = id;
        this.name = name;
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
     * @param id course id
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
     * @param name course name
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
     * @param description course description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
