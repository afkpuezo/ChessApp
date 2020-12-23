/**
 * This interface specifies the methods for DAO functions related to the MatchRecord 
 * class/table.
 * 
 * @author Andrew Curry
 */
package com.revature.repository.interfaces;

import java.util.List;

import com.revature.model.MatchRecord;
import com.revature.model.User;
import com.revature.repository.RepositoryException;

import org.hibernate.SessionFactory;


public interface MatchRepository {

    // ---------------------
    // ENUM(S)
    // ---------------------

    /**
     * This enum is used as a parameter in different overloads of findMatchRecordsBy.
     */
    public enum MatchStatusFilter{
        ALL,
        PENDING,
        ONGOING,
        FINISHED,
        WON_BY_GIVEN_USER,
        LOST_BY_GIVEN_USER,
    }

    // ---------------------
    // HELPER/TEST METHODS
    // ---------------------

    /**
     * Used for testing, injects a new sessionFactory.
     * 
     * @param sessionFactory
     */
    public void useOutsideSessionFactory(SessionFactory sessionFactory);
    
    // ---------------------
    // DATA ACCESS METHODS
    // ---------------------

    /**
     * Persists the given match record to the database.
     * Works with new AND already-existing entries.
     * 
     * Assumes the fields of the matchRecord are valid.
     * 
     * Throws RepositoryException if there are problems communicating with the database.
     * 
     * @param mr
     * @throws RepositoryException
     */
    public void save(MatchRecord mr) throws RepositoryException;

    /**
     * Returns a mr from the database that matches either the id or code of the given mr.
     * Null if no such mr found.
     * 
     * Throws exception if there is a problem with the db.
     * 
     * @param mr
     * @return
     * @throws RepositoryException
     */
    public MatchRecord findMatchRecord(MatchRecord mr) throws RepositoryException;

    /**
     * Returns the match record corresponding to the given id.
     * If no such match record exists, returns null.
     * 
     * Throws RepositoryException if there is a problem with the database.
     * 
     * @param id
     * @return
     * @throws RepositoryException
     */
    public MatchRecord findMatchRecordById(int id) throws RepositoryException;

    /**
     * Returns the match record corresponding to the given code.
     * If no such match record exists, returns null.
     * 
     * Throws RepositoryException if there is a problem with the database.
     * 
     * @param id
     * @return
     * @throws RepositoryException
     */
    public MatchRecord findMatchRecordByCode(int code) throws RepositoryException;

    /**
     * Finds all match records where the given user is one of the players.
     * If no such match records exist, returns an empty list.
     * 
     * Throws RepositoryException if there is a problem with the database, such as if
     * the user does not exist.
     * 
     * @param user
     * @return
     */
    public List<MatchRecord> findMatchRecordsBy(User user) throws RepositoryException;

    /**
     * Finds all match records matching the given status filter.
     * Only supports ALL, PENDING, ONGOING, and FINISHED.
     * 
     * @param filter
     * @return
     */
    public List<MatchRecord> findMatchRecordsBy(MatchStatusFilter filter) 
            throws RepositoryException;

    /**
     * Finds all match records where the given user is one of the players, AND the status
     * matches the given status filter.
     * Supports all filter types.
     * 
     * @param user
     * @param filter
     * @return
     * @throws RepositoryException
     */
    public List<MatchRecord> findMatchRecordsBy(User user, MatchStatusFilter filter) 
            throws RepositoryException;

    /**
     * Determines if the db has a MatchRecord matching either the id or code of the given
     * mr.
     * 
     * @param mr
     * @return
     * @throws RepositoryException 
     */
    public boolean checkExists(MatchRecord mr) throws RepositoryException;

    /**
     * Determines if the db has a MatchRecord matching the given id
     * 
     * @param mr
     * @return
     * @throws RepositoryException 
     */
    public boolean checkExistsById(int id) throws RepositoryException;

    /**
     * Determines if the db has a MatchRecord matching the given code
     * 
     * @param mr
     * @return
     * @throws RepositoryException 
     */
    public boolean checkExistsByCode(int code) throws RepositoryException;
    
}
