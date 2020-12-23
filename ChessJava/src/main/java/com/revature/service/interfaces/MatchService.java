/**
 * This interface describes the methods needed to service requests related to MatchRecords
 * 
 * @author Andrew Curry
 */
package com.revature.service.interfaces;

import java.security.Provider.Service;
import java.util.List;

import com.revature.model.MatchRecord;
import com.revature.model.User;
import com.revature.repository.interfaces.MatchRepository;
import com.revature.service.ServiceException;

public interface MatchService {
        
    // ---------------------
    // TESTER/HELPER METHODS
    // ---------------------

    /**
     * Injects the given mRepo to replace the one given by spring.
     * Used for testing.
     * 
     * @param mRepo
     */
    public void useOutsideRepository(MatchRepository mRepo);

    // ---------------------
    // SERVICE METHODS
    // ---------------------

    /**
     * Persists the given match record to the database.
     * Works with new AND already-existing entries.
     * 
     * Assumes the fields of the matchRecord are valid.
     * 
     * Throws ServiceException if there are problems communicating with the database.
     * 
     * @param mr
     * @throws ServiceException
     */
    public void save(MatchRecord mr) throws ServiceException;

    /**
     * Returns the match record corresponding to the id or code of the given mr. Returns
     * null if none found
     * 
     * Throws ServiceException if there is a problem with the database.
     * 
     * @param mr
     * @return
     * @throws ServiceException
     */
    public MatchRecord findMatchRecord(MatchRecord mr) throws ServiceException;

    /**
     * Returns the match record corresponding to the given id.
     * If no such match record exists, returns null.
     * 
     * Throws ServiceException if there is a problem with the database.
     * 
     * @param id
     * @return
     * @throws ServiceException
     */
    public MatchRecord findMatchRecordById(int id) throws ServiceException;

    /**
     * Returns the match record corresponding to the given code.
     * If no such match record exists, returns null.
     * 
     * Throws ServiceException if there is a problem with the database.
     * 
     * @param code
     * @return
     * @throws ServiceException
     */
    public MatchRecord findMatchRecordByCode(int code) throws ServiceException;
    
    /**
     * Finds all match records.
     * If no match records exist, returns an empty list.
     * 
     * Throws ServiceException if there is a problem with the database.
     * 
     * @return
     * @throws ServiceException
     */
    public List<MatchRecord> findAllMatchRecords() throws ServiceException;

    /**
     * Finds all pending match records.
     * If no such match records exist, returns an empty list.
     * 
     * Throws ServiceException if there is a problem with the database.
     * 
     * @return
     * @throws ServiceException
     */
    public List<MatchRecord> findAllPendingMatchRecords() throws ServiceException;

    /**
     * Finds all ongoing match records.
     * If no such match records exist, returns an empty list.
     * 
     * Throws ServiceException if there is a problem with the database.
     * 
     * @return
     * @throws ServiceException
     */
    public List<MatchRecord> findAllOngoingMatchRecords() throws ServiceException;

    /**
     * Finds all finished match records.
     * If no such match records exist, returns an empty list.
     * 
     * Throws ServiceException if there is a problem with the database.
     * 
     * @return
     * @throws ServiceException
     */
    public List<MatchRecord> findAllFinishedMatchRecords() throws ServiceException;
    
    /**
     * Finds all match records where the given user is one of the players.
     * If no such match records exist, returns an empty list.
     * 
     * Throws ServiceException if there is a problem with the database, such as if
     * the user does not exist.
     * 
     * @param user
     * @return
     */
    public List<MatchRecord> findAllMatchRecordsWithPlayer(User player) 
            throws ServiceException;
    
    /**
     * Finds all ongoing match records where the given user is one of the players.
     * If no such match records exist, returns an empty list.
     * 
     * Throws ServiceException if there is a problem with the database, such as if
     * the user does not exist.
     * 
     * @param user
     * @return
     */
    public List<MatchRecord> findAllOngoingMatchRecordsWithPlayer(User player) 
            throws ServiceException;
    
    /**
     * Finds all finished match records where the given user is one of the players.
     * If no such match records exist, returns an empty list.
     * 
     * Throws ServiceException if there is a problem with the database, such as if
     * the user does not exist.
     * 
     * @param user
     * @return
     */
    public List<MatchRecord> findAllFinishedMatchRecordsWithPlayer(User player) 
            throws ServiceException;
    
    /**
     * Finds all finished match records where the given user is/was the winner.
     * 
     * Throws ServiceException if there is a problem with the database, such as if
     * the user does not exist.
     * 
     * @param user
     * @return
     */
    public List<MatchRecord> findAllMatchRecordsWithWinner(User player) 
                throws ServiceException;

    /**
     * Finds all finished match records where the given user is/was the loser.
     * 
     * Throws ServiceException if there is a problem with the database, such as if
     * the user does not exist.
     * 
     * @param user
     * @return
     */
    public List<MatchRecord> findAllMatchRecordsWithLoser(User player) 
                throws ServiceException;

    /**
     * Adds the given player as the blackPlayer of the game indicated by the given code.
     * Throws an exception if the MatchRecord does not exist, or if it already has a
     * black player.
     * 
     * @param player
     * @param code
     * @throws ServiceException
     */
    public void acceptCode(User player, int code) throws ServiceException;

    /**
     * Creates a new MatchRecord, with the given code, the given player as white, and in
     * the PENDING status.
     * 
     * Throws exception if the code is not unique.
     * 
     * @param player
     * @param code
     * @throws ServiceException
     */
    public void makeGame(User player, int code) throws ServiceException;

    /**
     * Given a game code, finds the game with that code, then returns a string containing
     * the usernames of the two players in it, seperated by a space.
     * If there is only one player (if the game is pending), only that user's username is
     * returned.
     * 
     * Throws exception if there is a problem.
     * 
     * @param code
     * @throws ServiceException
     */
    public String getPlayerStringByCode(int code) throws ServiceException;

    /**
     * Marks the given user as the winner of the given game.
     * 
     * Throws exception if there is a problem, such as if the user is not one of the
     * players of the game, or if the username and/or code are invalid, or if the game is
     * not ONGOING.
     * 
     * @param code
     * @param username
     * @throws ServiceException
     */
    public void recordMatchWinner(int code, String username) throws ServiceException;

}
