/**
 * This class implements the UserService interface, providing methods to resolve service
 * requests related to the User class/table.
 * 
 * NOTE: currently, it only implements the limited functionality needed to log in and
 * register.
 * 
 * @author Andrew Curry
 */
package com.revature.service.impl;

import java.util.List;

import com.revature.model.User;
import com.revature.repository.RepositoryException;
import com.revature.repository.interfaces.UserRepository;
import com.revature.service.ServiceException;
import com.revature.service.interfaces.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

    // ---------------------
    // INSTANCE VARIABLES
    // ---------------------

    @Autowired
    private UserRepository uRepo;

    // ---------------------
    // HELPER/TESTING METHODS
    // ---------------------
    
    /**
     * Uses the given uRepo instead of the spring-injected bean.
     * Should be used for testing.
     * 
     * @param uRepo
     */
    public void useOutsideRepository(UserRepository uRepo){
        this.uRepo = uRepo;
    }
    
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
    @Override
    public void update(User user) throws ServiceException {
        try{
            if (!uRepo.checkExists(user))
                throw new ServiceException(
                    "USI.update(): user <" 
                    + user.getUsername()
                    + "> not found.");
            uRepo.update(user);
        } catch(RepositoryException e){
            throw new ServiceException("RepositoryException: " + e.getMessage());
        }
    }

    /**
     * Persists the given user to the database.
     * Use this method to write a brand new user to the database.
     * 
     * Will encrypt, but not validate, the given password.
     * 
     * Returns null if the user is already in the DB - use update() for
     * existing users
     * 
     * 
     * @param user : a new user
     * @param barePassword : NOT encrypted yet
     * @return the user after being persisted.
     * @throws ServiceException
     */
    @Override
    public User register(User user, String barePassword) throws ServiceException {
        try{
            return (uRepo.checkExists(user)) ? null : uRepo.register(user, barePassword);
        } catch(RepositoryException e){
            throw new ServiceException("RepositoryException: " + e.getMessage());
        }
    }

    /**
     * Returns the user corresponding to information in the given user (id or username)
     * If no such user exists, returns null.
     * 
     * @param id
     * @return
     * @throws ServiceException : if there is a problem with the database
     */
    @Override
    public User findUser(User u) throws ServiceException{
        try{
            return uRepo.findUser(u);
        } catch(RepositoryException e){
            throw new ServiceException("RepositoryException: " + e.getMessage());
        }
    }

    /**
     * Returns the user corresponding to the given id.
     * If no such user exists, returns null.
     * 
     * @param id
     * @return
     * @throws ServiceException : if there is a problem with the database
     */
    @Override
    public User findUser(int id) throws ServiceException {
        try{
            return uRepo.findUser(id);
        } catch(RepositoryException e){
            throw new ServiceException("RepositoryException: " + e.getMessage());
        }
    }

    /**
     * Returns the user corresponding to the given username.
     * If no such user exists, returns null.
     * 
     * @param username
     * @return
     * @throws ServiceException : if there is a problem with the database
     */
    @Override
    public User findUser(String username) throws ServiceException {
        try{
            return uRepo.findUser(username);
        } catch(RepositoryException e){
            throw new ServiceException("RepositoryException: " + e.getMessage());
        }
    }

    /**
     * Determines if the given password corresponds to the given user. Returns the user
     * if login is successful, null otherwise.
     * 
     * Throws ServiceException if there is a problem with the database.
     * 
     * @param user
     * @param attemptedPassword : bare text, unencrypted
     * @return true if the given password unlocks the given user account, false otherwise.
     * @throws ServiceException
     */
    @Override
    public User logIn(User user, String attemptedPassword) throws ServiceException {
        try{
            User found = uRepo.findUser(user);
            if (found == null) return null; // can't log in to a user that doesnt exist
            else return (uRepo.checkPassword(found, attemptedPassword)) ? found : null;
        } catch(RepositoryException e){
            throw new ServiceException("RepositoryException: " + e.getMessage());
        }
    }

    /**
     * Returns a list of all the users in the database.
     * 
     * Throws ServiceException if there is a problem with the database
     * 
     * @return
     * @throws ServiceException
     */
    @Override
    public List<User> findAllUsers() throws ServiceException {
        try{
            return uRepo.findAllUsers();
        } catch(RepositoryException e){
            throw new ServiceException("RepositoryException: " + e.getMessage());
        }
    }
}
