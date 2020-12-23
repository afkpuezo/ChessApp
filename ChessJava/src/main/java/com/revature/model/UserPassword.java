/**
 * Members of this class represent individual entries on the password table.
 * They are kept separate from Users for security reasons. Only the DAO should need to
 * interact with this class.
 * 
 * @author Andrew Curry
 */
package com.revature.model;

import java.io.Serializable;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Table(name="USER_PASSWORD")
public class UserPassword implements Serializable{
    
    // for serializable
    private static final long serialVersionUID = 0L; // makes compiler happy

    // ---------------------
    // INSTANCE VARIABLES
    // ---------------------

    @Id
    @OneToOne
    @JoinColumn(name="PASSWORD_USER_ID", nullable=false, referencedColumnName="ID")
    private User user;

    @Column(name="ENCRYPTED_PASS")
    private String encryptedPass;

    // ---------------------
    // CONSTRUCTOR(S)
    // ---------------------

    /**
     * Does NOT intialize any fields.
     */
    public UserPassword() {}

    /**
     * Does NOT validate any of the given data.
     * 
     */
    public UserPassword(User user, String encryptedPass){
        this.user = user;
        this.encryptedPass = encryptedPass;
    }

    // ---------------------
    // SETTERS AND GETTERS
    // ---------------------

    public User getUser() {
		return this.user;
	}

    /**
     * Does not validate anything on the user account.
     * 
     * @param user
     */
	public void setUser(User user) {
		this.user = user;
	}

    public String getEncryptedPass() {
		return this.encryptedPass;
	}

    /**
     * Does not validate anything on the password.
     * Encryption should be handled by the DAO.
     * 
     * @param user
     */
	public void setEncryptedPass(String encryptedPass) {
		this.encryptedPass = encryptedPass;
	}
}
