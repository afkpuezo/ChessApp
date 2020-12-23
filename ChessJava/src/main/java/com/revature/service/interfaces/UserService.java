/**
 * This interface describes the methods needed to service requests related to Users.
 * @author Andrew Curry
 */
package com.revature.service.interfaces;

import java.util.List;

import com.revature.model.User;
import com.revature.repository.interfaces.UserRepository;
import com.revature.service.ServiceException;

public interface UserService {

    // ---------------------
    // HELPER/TESTING METHODS
    // ---------------------

    /**
     * Uses the given urepo instead of the spring-injected bean.
     * Should be used for testing.
     * 
     * @param urepo
     */
    public void useOutsideRepository(UserRepository urepo);

    // ---------------------
    // SERVICE METHODS
    // ---------------------

    /**
     * Persists the given user to the database.
     * Use this method to update a user that already exists.
     * 
     * Assumes the fields of the user are valid.
     * 
     * Throws ServiceException if the user is not already in the DB - use register()
     * 
     * @param user : a user that already exists
     * @throws ServiceException : if there is a problem with the database, or if user is
     *                              not already present in DB
     */
    public void update(User user) throws ServiceException;

    /**
     * Persists the given user to the database.
     * Use this method to write a brand new user to the database.
     * 
     * Will encrypt, but not validate, the given password.
     * 
     * Returns null if the user is already in the DB - use update() for
     * existing users
     * 
     * @param user : a new user
     * @param barePassword : NOT encrypted yet
     * @return the user after being persisted.
     * @throws ServiceException
     */
    public User register(User user, String barePassword) throws ServiceException;

    /**
     * Returns the user corresponding to information in the given user (id or username)
     * If no such user exists, returns null.
     * 
     * @param id
     * @return
     * @throws ServiceException : if there is a problem with the database
     */
    public User findUser(User u) throws ServiceException;

    /**
     * Returns the user corresponding to the given id.
     * If no such user exists, returns null.
     * 
     * @param id
     * @return
     * @throws ServiceException : if there is a problem with the database
     */
    public User findUser(int id) throws ServiceException;

    /**
     * Returns the user corresponding to the given username.
     * If no such user exists, returns null.
     * 
     * @param username
     * @return
     * @throws ServiceException : if there is a problem with the database
     */
    public User findUser(String username) throws ServiceException;

    /**
     * Determines if the given password corresponds to the given user. Returns the user
     * if login is successful, null otherwise.
     * 
     * Throws ServiceException if there is a problem with the database, including if
     * the given user does not exist.
     * 
     * @param user
     * @param attemptedPassword : bare text, unencrypted
     * @return true if the given password unlocks the given user account, false otherwise.
     * @throws ServiceException
     */
    public User logIn(User user, String attemptedPassword) 
            throws ServiceException;

    /**
     * Returns a list of all the users in the database.
     * 
     * Throws ServiceException if there is a problem with the database
     * 
     * @return
     * @throws ServiceException
     */
    public List<User> findAllUsers() throws ServiceException;
}
