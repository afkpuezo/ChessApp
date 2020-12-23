/**
 * This class will be used by the UserController interface/classes to put the relevant
 * data into one json message. It can be unpacked to create the User object.
 */
package com.revature.model;

public class UserWithPassword {

    // ---------------------
    // INSTANCE VARIABLES
    // ---------------------

    private int id;
    private String username;
    private String email;
    private String barePassword;

    // ---------------------
    // CONSTRUCTOR(S)
    // ---------------------

    /**
     * Does NOT intialize any fields.
     */
    public UserWithPassword(){}

    /** 
     * Does NOT validate any of the data.
     * 
    */
    public UserWithPassword(int id, String username, String email, String barePassword){
        this.id = id;
        this.username = username;
        this.email = email;
        this.barePassword = barePassword;
    }

    // ---------------------
    // METHODS
    // ---------------------

    /**
     * Creates a new User object based on the relevant data inside this object.
     * 
     * @return
     */
    public User makeUser(){
        return new User(id, username, email);
    }

    // ---------------------
    // GETTERS AND SETTERS
    // ---------------------

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBarePassword() {
        return this.barePassword;
    }

    public void setBarePassword(String barePassword) {
        this.barePassword = barePassword;
    }
}
