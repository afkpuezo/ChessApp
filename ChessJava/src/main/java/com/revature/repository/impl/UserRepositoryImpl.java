/**
 * This class implements the required methods relating to persisting and reading User
 * data from the database.
 * 
 * @author Andrew Curry
 */
package com.revature.repository.impl;

import java.util.List;

import com.revature.model.User;
import com.revature.model.UserPassword;
import com.revature.repository.RepositoryException;
import com.revature.repository.interfaces.UserRepository;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import org.mindrot.jbcrypt.BCrypt;

@Repository("userRepository")
@Transactional
public class UserRepositoryImpl implements UserRepository {

    // ---------------------
    // INSTANCE VARIABLES
    // ---------------------

    @Autowired
    private SessionFactory sessionFactory;

    // ---------------------
    // INTERFACE METHODS
    // ---------------------

    @Override
    public void useOutsideSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

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
    @Override
    public void update(User user) throws RepositoryException {
        try{
            if(!checkExists(user)) 
                throw new RepositoryException(
                    "In update: User <" 
                    + user.getUsername() 
                    + "> + not found; use register for new users.");
            Session session = sessionFactory.getCurrentSession();
            //Transaction tx = session.beginTransaction();
            session.update(user);
            //tx.commit();
        }catch(HibernateException e){
            throw new RepositoryException("HibernateException: " + e.getMessage());
        }
    }

    /**
     * Persists the given user to the database.
     * Use this method to write a brand new user to the database.
     * 
     * Throws RepositoryException if the user is already in the DB - use update() for
     * existing users
     * 
     * @param user : a new user
     * @param securePassword : encrypted
     * @return the id of the user after being persisted.
     * @throws RepositoryException
     */
    @Override
    public User register(User user, String barePassword) throws RepositoryException {
        try{
            if(checkExists(user)) throw new RepositoryException("User already exists");
            Session session = sessionFactory.getCurrentSession();
            //Transaction tx = session.beginTransaction();
            session.save(user);
            UserPassword upass = new UserPassword(user, encryptPassword(barePassword));
            session.save(upass);
            //tx.commit();
            return user;
        } catch(HibernateException e){
            throw new RepositoryException("HibernateException: " + e.getMessage());
        }
    }

    /**
     * Returns a user from the database corresponding to info in the given user.
     * NOTE: currently only checks by id and/or username
     * If no such user exists, returns null.
     * 
     * @param id
     * @return
     * @throws RepositoryException : if there is a problem with the database
     */
    public User findUser(User user) throws RepositoryException{
        User found = findUser(user.getId());
        if (found != null) return found;
        else return findUser(user.getUsername());
    }

    /**
     * Returns the user corresponding to the given id.
     * If no such user exists, returns null.
     * 
     * @param id
     * @return
     * @throws RepositoryException : if there is a problem with the database
     */
    @Override
    public User findUser(int id) throws RepositoryException {
        try{
            Session session = sessionFactory.getCurrentSession();
            //Transaction tx = session.beginTransaction();
            User u = (User)session.get(User.class, id);
            //tx.commit();
            return u;
        } catch(HibernateException e){
            throw new RepositoryException("HibernateException: " + e.getMessage());
        }
    }

    /**
     * Returns the user corresponding to the given username.
     * If no such user exists, returns null.
     * 
     * @param username
     * @return
     * @throws RepositoryException : if there is a problem with the database
     */
    @Override
    @SuppressWarnings(value="unchecked")
    public User findUser(String username) throws RepositoryException {
        try{
            Session session = sessionFactory.getCurrentSession();
            //Transaction tx = session.beginTransaction();
            Criteria crit = session.createCriteria(User.class);
            crit.add(Restrictions.eq("username", username));
            List<User> userList = crit.list();
            //tx.commit();
            return (userList.isEmpty()) ? null : userList.get(0);
        } catch(HibernateException e){
            throw new RepositoryException("HibernateException: " + e.getMessage());
        }
    }

    /**
     * Returns a list containing all of the registered users.
     * If no such users exists, returns an empty list.
     * 
     * @return
     * @throws RepositoryException : if there is a problem with the database
     */
    @SuppressWarnings(value="unchecked")
    @Override
    public List<User> findAllUsers() throws RepositoryException {
        try{
            Session session = sessionFactory.getCurrentSession();
            //Transaction tx = session.beginTransaction();
            Criteria crit = session.createCriteria(User.class);
            List<User> userList = crit.list();
            //tx.commit();
            return userList;
        } catch(HibernateException e){
            throw new RepositoryException("HibernateException: " + e.getMessage());
        }
    }

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
    @Override
    @SuppressWarnings(value="unchecked")
    public boolean checkPassword(User user, String attemptedPassword) 
            throws RepositoryException {
        try{
            if (!checkExists(user)) throw new RepositoryException("User not found");
            Session session = sessionFactory.getCurrentSession();
            //Transaction tx = session.beginTransaction();
            Criteria crit = session.createCriteria(UserPassword.class);
            crit.add(Restrictions.eq("user.id", user.getId()));
            List<UserPassword> passList = crit.list();
            //tx.commit();
            if (passList.isEmpty()) 
                throw new RepositoryException("User does not have a matching password");
            UserPassword upass = passList.get(0);
            return checkPasswordHelp(attemptedPassword, upass.getEncryptedPass());
        } catch(HibernateException e){
            throw new RepositoryException("HibernateException: " + e.getMessage());
        }
    }

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
    @Override
    public boolean checkExists(User user) throws RepositoryException {
        return checkExists(user.getId()) || checkExists(user.getUsername());
    }

    /**
     * Returns true if the id is found in the db, and false otherwise.
     * 
     * Throws RepositoryException if there is a problem with the database
     * 
     * @param id
     * @return
     * @throws RepositoryException
     */
    @Override
    public boolean checkExists(int id) throws RepositoryException {
        try{
            Session session = sessionFactory.getCurrentSession();
            //Transaction tx = session.beginTransaction();
            User u = (User)session.get(User.class, id);
            //tx.commit();
            return u != null;
        } catch(HibernateException e){
            throw new RepositoryException("HibernateException: " + e.getMessage());
        }
    }

    /**
     * Returns true if the username is found in the db, and false otherwise.
     * 
     * Throws RepositoryException if there is a problem with the database
     * 
     * @param id
     * @return
     * @throws RepositoryException
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean checkExists(String username) throws RepositoryException {
        try{
            Session session = sessionFactory.getCurrentSession();
            //Transaction tx = session.beginTransaction();
            Criteria crit = session.createCriteria(User.class);
            crit.add(Restrictions.eq("username", username));
            List<User> userList = crit.list();
            //tx.commit();
            return !userList.isEmpty();
        } catch(HibernateException e){
            throw new RepositoryException("HibernateException: " + e.getMessage());
        }
    }

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
    @Override
    @SuppressWarnings(value="unchecked")
    public void resetPassword(User user, String newBarePassword) throws RepositoryException {
        if (!checkExists(user)) 
            throw new RepositoryException(
                "In URI resetPassword(): User <" 
                + user.getUsername() 
                + "> already exists!");
        try{
            Session session = sessionFactory.getCurrentSession();
            //Transaction tx = session.beginTransaction();
            Criteria crit = session.createCriteria(UserPassword.class);
            crit.add(Restrictions.eq("user.id", user.getId()));
            List<UserPassword> passList = crit.list();
            if (passList.isEmpty()) 
            throw new RepositoryException("User does not have a matching password");
            UserPassword upass = passList.get(0);
            upass.setEncryptedPass(encryptPassword(newBarePassword));
            session.update(upass);
            //tx.commit();
        } catch (HibernateException e){
            throw new RepositoryException("HibernateException: " + e.getMessage());
        }
    }

    // ---------------------
    // HELPER METHODS
    // ---------------------

    /**
     * Uses jbcrypt to hash/salt the given bare password.
     * 
     * @param barePassword
     * @return
     */
    private String encryptPassword(String barePassword) {
        return BCrypt.hashpw(barePassword, BCrypt.gensalt());
    }

    /**
     * Uses jbcrypt to determine if the given un-encrypted password matches the encrypted
     * one.
     * 
     * @param barePassword
     * @return
     */
    private boolean checkPasswordHelp(String bare, String secure){
        return BCrypt.checkpw(bare, secure);
    }
}
