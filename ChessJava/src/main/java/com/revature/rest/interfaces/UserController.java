/**
 * This interface describes the methods needed for the UserController, which bridges the
 * gap between the dispatcher servlet and UserService.
 * 
 * NOTE: it is not complete. There are many methods not yet specified.
 * 
 * @author Andrew Curry
 */
package com.revature.rest.interfaces;

import java.util.List;

//import javax.servlet.http.HttpServletRequest;
import com.revature.model.User;
import com.revature.model.UserWithPassword;
import com.revature.service.interfaces.UserService;

public interface UserController {

    // ---------------------
    // UTILITY / TESTING METHODS
    // ---------------------

    /**
     * Used to replace the automatically-injected spring bean. Used for testing.
     * 
     * @param uService
     */
    public void useOutsideService(UserService uService);

    // ---------------------
    // REQUEST-HANDLING METHODS
    // ---------------------

    /**
     * Handles registering a new user to the system. Must be a new user, not already in
     * the database.
     * 
     * Intended for POST
     * 
     * @param user
     * @param barePassword
     * @return
     */
    public User registerUser(UserWithPassword uwp);

    /**
     * Handles a user logging in to the system. Returns the user object if successful,
     * false otherwise.
     * 
     * Intended for POST
     * 
     * @param username
     * @param barePassword
     * @return
     */
    public User logIn(String[] args);

    /**
     * Returns a list of all of the registered users.
     * 
     * Returns an empty list if there are no users.
     * Returns null if there was a problem.
     * 
     * Intended for get.
     * 
     * @return
     */
    public List<User> getAllusers();
}
