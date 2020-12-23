/**
 * This class represents/models user accounts for the application.
 * NOTE: User objects do not store/remember their own passwords. The DAO class/object
 * performs password checking.
 * 
 * @author Andrew Curry
 */
package com.revature.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="USER")
public class User implements Serializable{

    // for serializable
    private static final long serialVersionUID = 0L; // makes compiler happy

    // ---------------------
    // INSTANCE VARIABLES
    // ---------------------

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="USERNAME")
    private String username;

    @Column(name="EMAIL")
    private String email;

    // ---------------------
    // CONSTRUCTOR(S)
    // ---------------------

    /**
     * Does NOT intialize any fields.
     */
    public User(){}

    /** 
     * Does NOT validate any of the data.
     * 
    */
    public User(int id, String username, String email){
        this.id = id;
        this.username = username;
        this.email = email;
    }

    /** 
     * Does NOT validate any of the data.
     * 
    */
    public User(String username, String email){
        this.username = username;
        this.email = email;
    }

    /** 
     * Does NOT validate any of the data.
     * 
    */
    public User(String username){
        this.username = username;
    }

    // ---------------------
    // SETTERS AND GETTERS
    // ---------------------
    
    public int getId() {
        return this.id;
    }

    /**
     * Does not validate the ID.
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    /**
     * Does not validate the username.
     * 
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    /**
     * Does not validate the email.
     * 
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}