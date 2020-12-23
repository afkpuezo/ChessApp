/**
 * Thrown by a Repository when there is a problem communicating with
 * the database.
 * 
 */
package com.revature.repository;

public class RepositoryException extends Exception{

    // for serializable
    private static final long serialVersionUID = 0L; // makes compiler happy

    // ---------------------
    // CONSTRUCTOR(S)
    // ---------------------

    public RepositoryException(){
        super();
    }

    public RepositoryException(String message){
        super(message);
    }
}
