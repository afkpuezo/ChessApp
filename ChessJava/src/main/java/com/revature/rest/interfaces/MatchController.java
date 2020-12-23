/**
 * This class handles http requests related to match records.
 * 
 * NOTE: it is not complete. There are many methods not yet specified.
 * 
 * @author Andrew Curry
 */
package com.revature.rest.interfaces;

import java.util.List;

import com.revature.model.MatchRecord;
import com.revature.model.User;
import com.revature.service.interfaces.MatchService;

public interface MatchController {

    // ---------------------
    // UTILITY / TESTING METHODS
    // ---------------------

    /**
     * Used to replace the automatically-injected spring bean. Used for testing.
     * 
     * @param mrService
     */
    public void useOutsideService(MatchService mrService);

    // ---------------------
    // REQUEST-HANDLING METHODS
    // ---------------------

    /**
     * Retrieves the up-to-date MatchRecord object matching the given id. If not found, 
     * returns null.
     * 
     * @param id
     * @return
     */
    public MatchRecord checkMatchRecord(int id);

    /**
     * Returns the entire moveHistory string from the requested MatchRecord
     * Returns null if failure
     * 
     * @param req
     * @return
     */
    public String getMove(String req);

    /**
     * Replaces the entire moveHistory string on the requested MatchRecord
     * Returns false if failure
     * 
     * @param req
     * @return
     */
    public boolean recordMove(String req);

    /**
     * Starts a new game with the given code, with the given player as white
     * Returns false if failure
     * 
     * @param req
     * @return
     */
    public boolean makeGame(String req);

    /**
     * Adds the given player as black to the pending game indicated by the given code.
     * Returns false if failure
     * 
     * @param req
     * @return
     */
    public boolean findGame(String req);

    /**
     * Returns a list of every match which has the status PENDING (waiting for a player).
     * Returns an empty list if there are no pending games.
     * Returns null if there is a problem.
     * 
     * Intended for GET
     * 
     * @return
     */
    public List<MatchRecord> getAllPendingGames();

    /**
     * Returns a list of every match in which the given user is one of the players.
     * Returns an empty list if there are no such games.
     * Returns null if there is a problem.
     * 
     * Intended for POST
     * 
     * @param user
     * @return
     */
    public List<MatchRecord> getAllGamesWithPlayer(User user);

    /**
     * Given a game code, finds the game with that code, then returns a string containing
     * the usernames of the two players in it, seperated by a space.
     * If there is only one player (if the game is pending), only that user's username is
     * returned.
     * 
     * Returns null if there is a problem.
     * 
     * Intended for POST
     * 
     * let template = {code: code}
     * 
     * @param code
     * @throws ServiceException
     */
    public String getPlayerStringByCode(String req);

    /**
     * Given a game code and a username, makes that user the winner of that game.
     * Will fail if the game or username are not found, if the user is not one of the
     * players in the game, or if the game is not ONGOING.
     * 
     * Intended for POST
     * 
     * let template = {code: code, user: username}
     * 
     * @param req
     * @return
     */
    public boolean recordGameWinner(String req);

    /**
     * Given a user, return a list of all the FINISHED games that player is/was in.
     * Returns an empty list if no such games are found.
     * 
     * Returns null on failure/error, such as if the user is not found.
     * 
     * Intended for POST
     * 
     * @param user
     * @return
     */
    public List<MatchRecord> getMatchHistoryOfPlayer(User user);
}
