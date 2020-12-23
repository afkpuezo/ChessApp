/**
 * Thrown by classes in the service layer, when there is a problem with the repository,
 * or when a requested action is impossible.
 */
package com.revature.service;

public class ServiceException extends Exception{

    // for serializable
    private static final long serialVersionUID = 0L; // makes compiler happy
    
    // ---------------------
    // CONSTRUCTOR(S)
    // ---------------------

    public ServiceException(){
        super();
    }

    public ServiceException(String message){
        super(message);
    }
}
