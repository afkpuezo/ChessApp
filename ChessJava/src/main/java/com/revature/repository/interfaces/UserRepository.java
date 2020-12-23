/**
 * This interface specifies the methods for DAO functions related to the User class/table.
 * 
 * @author Andrew Curry
 */
package com.revature.repository.interfaces;

import java.util.List;

import com.revature.model.User;
import com.revature.repository.RepositoryException;

import org.hibernate.SessionFactory;

public interface UserRepository {

    // ---------------------
    // METHODS
    // ---------------------

    /**
     * Forces the UserRepository to use the given SessionFactory instead of the one
     * automatically injected by spring. This will likely only be used for testing.
     * 
     * @param sessionFactory
     */
    public void useOutsideSessionFactory(SessionFactory sessionFactory);

    /**
     * Persists the given user to the database.
     * Use this method to update a user that already exists.
     * 
     * Assumes the fields of the user are valid.
     * 
     * Throws RepositoryException if the user is not already in the DB - use register()
     * 
     * @param user : a user that already exists
     * @throws RepositoryException : if there is a problem with the database
     */
    public void update(User user) throws RepositoryException;

    /**
     * Persists the given user to the database.
     * Use this method to write a brand new user to the database.
     * 
     * Throws RepositoryException if the user is already in the DB - use update() for
     * existing users
     * 
     * @param user : a new user
     * @param securePassword : encrypted
     * @return the user after being persisted.
     * @throws RepositoryException
     */
    public User register(User user, String barePassword) throws RepositoryException;

    /**
     * Returns a user from the database corresponding to info in the given user.
     * If no such user exists, returns null.
     * 
     * @param id
     * @return
     * @throws RepositoryException : if there is a problem with the database
     */
    public User findUser(User user) throws RepositoryException;

    /**
     * Returns the user corresponding to the given id.
     * If no such user exists, returns null.
     * 
     * @param id
     * @return
     * @throws RepositoryException : if there is a problem with the database
     */
    public User findUser(int id) throws RepositoryException;

    /**
     * Returns the user corresponding to the given username.
     * If no such user exists, returns null.
     * 
     * @param username
     * @return
     * @throws RepositoryException : if there is a problem with the database
     */
    public User findUser(String username) throws RepositoryException;

    /**
     * Returns a list containing all of the registered users.
     * If no such users exists, returns an empty list.
     * 
     * @return
     * @throws RepositoryException : if there is a problem with the database
     */
    public List<User> findAllUsers() throws RepositoryException;

    /**
     * Determines if the given password corresponds to the given user.
     * 
     * Throws RepositoryException if there is a problem with the database, including if
     * the given user does not exist.
     * 
     * @param user
     * @param attemptedPassword : bare text, unencrypted
     * @return true if the given password unlocks the given user account, false otherwise.
     * @throws RepositoryException
     */
    public boolean checkPassword(User user, String attemptedPassword) 
            throws RepositoryException;

    /**
     * Returns true if either the id or username of the given user is found in the db, and
     * false otherwise.
     * 
     * Throws RepositoryException if there is a problem with the database
     * 
     * @param user
     * @return
     * @throws RepositoryException
     */
    public boolean checkExists(User user) throws RepositoryException;

    /**
     * Returns true if the id is found in the db, and false otherwise.
     * 
     * Throws RepositoryException if there is a problem with the database
     * 
     * @param id
     * @return
     * @throws RepositoryException
     */
    public boolean checkExists(int id) throws RepositoryException;

    /**
     * Returns true if the username is found in the db, and false otherwise.
     * 
     * Throws RepositoryException if there is a problem with the database
     * 
     * @param id
     * @return
     * @throws RepositoryException
     */
    public boolean checkExists(String username) throws RepositoryException;

    /**
     * Changes the password of the given user to the given string.
     * 
     * Throws RepositoryException if there is a problem with the database, such as if
     * the user does not exist.
     * 
     * @param id
     * @return
     * @throws RepositoryException
     */
    public void resetPassword(User user, String newBarePassword) 
            throws RepositoryException;
}
