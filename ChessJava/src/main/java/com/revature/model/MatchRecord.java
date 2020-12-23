/**
 * Members of this class represents the saved state of a single chess match. It has no
 * understanding of the rules of the game, and will not help you actually play a game of
 * chess - it is used to save matches in and retrieve them from the database.
 * 
 * @author Andrew Curry
 */
package com.revature.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="MATCH_RECORD")
public class MatchRecord implements Serializable{
    
    // for serializable
    private static final long serialVersionUID = 0L; // makes compiler happy

    // ---------------------
    // ENUMS
    // ---------------------

    /**
     * This enum describes the state of the game - EG, ongoing or finished
     */
    public enum MatchStatus {
        NONE, // should never be used - indicates problem
        PENDING, // waiting for another play to input the code
        ONGOING, // game is not over yet
        WHITE_VICTORY,
        BLACK_VICTORY,
    }

    // ---------------------
    // INSTANCE VARIABLES
    // ---------------------

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="CODE")
    private int code;

    @ManyToOne
    @JoinColumn(name="WHITE_USER_ID", nullable = false, referencedColumnName = "ID")
    private User whiteUser;

    @ManyToOne
    @JoinColumn(name="BLACK_USER_ID", nullable = true, referencedColumnName = "ID")
    private User blackUser;

    @Column(name="MATCH_STATUS")
    @Enumerated(EnumType.ORDINAL)
    private MatchStatus status;

    @Column(name="MOVE_HISTORY")
    private String moveHistory;

    // ---------------------
    // CONSTRUCTOR(S)
    // ---------------------

    /**
     * Does NOT intialize any fields.
     */
    public MatchRecord(){}

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

    public User getWhiteUser() {
        return this.whiteUser;
    }

    /**
     * Does not validate the user.
     * @param whiteUser
     */
    public void setWhiteUser(User whiteUser) {
        this.whiteUser = whiteUser;
    }

    public User getBlackUser() {
        return this.blackUser;
    }

    /**
     * Does not validate the user.
     * @param blackUser
     */
    public void setBlackUser(User blackUser) {
        this.blackUser = blackUser;
    }

    public MatchStatus getStatus() {
        return this.status;
    }

    /**
     * Does not validate the status.
     * @param status
     */
    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    /**
     * Returns a string containing the move history of this match. MatchRecord has no
     * understanding of the meaning of the string, and does no parsing.
     * 
     * @return
     */
    public String getMoveHistory() {
        return this.moveHistory;
    }

    /**
     * Does not validate the history. Completely replaces the old string.
     * @param moveHistory
     */
    public void setMoveHistory(String moveHistory) {
        this.moveHistory = moveHistory;
    }

    public int getCode() {
        return this.code;
    }

    /**
     * Does NOT validate the code.
     * 
     * @param code
     */
    public void setCode(int code) {
        this.code = code;
    }
}
