package com.revature.service.impl;

import java.util.List;

import com.revature.model.MatchRecord;
import com.revature.model.User;
import com.revature.model.MatchRecord.MatchStatus;
import com.revature.repository.RepositoryException;
import com.revature.repository.interfaces.MatchRepository;
import com.revature.repository.interfaces.UserRepository;
import com.revature.repository.interfaces.MatchRepository.MatchStatusFilter;
import com.revature.service.ServiceException;
import com.revature.service.interfaces.MatchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("matchService")
public class MatchServiceImpl implements MatchService {

    // ---------------------
    // INSTANCE VARIABLES
    // ---------------------

    @Autowired
    private MatchRepository mRepo;

    @Autowired
    private UserRepository uRepo;

    // ---------------------
    // TESTER/HELPER METHODS
    // ---------------------

    @Override
    public void useOutsideRepository(MatchRepository mRepo) {
        this.mRepo = mRepo;
    }

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
    @Override
    public void save(MatchRecord mr) throws ServiceException {
        try{
            mRepo.save(mr);
        } catch(RepositoryException e){
            throw new ServiceException("RepositoryException: " + e.getMessage());
        }
    }

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
    @Override
    public MatchRecord findMatchRecord(MatchRecord mr) throws ServiceException {
        try{
            return mRepo.findMatchRecord(mr);
        } catch(RepositoryException e){
            throw new ServiceException("RepositoryException: " + e.getMessage());
        }
    }

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
    @Override
    public MatchRecord findMatchRecordById(int id) throws ServiceException {
        try{
            return mRepo.findMatchRecordById(id);
        } catch(RepositoryException e){
            throw new ServiceException("RepositoryException: " + e.getMessage());
        }
    }

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
    @Override
    public MatchRecord findMatchRecordByCode(int code) throws ServiceException {
        try{
            return mRepo.findMatchRecordByCode(code);
        } catch(RepositoryException e){
            throw new ServiceException("RepositoryException: " + e.getMessage());
        }
    }

    
    @Override
    public List<MatchRecord> findAllMatchRecords() throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Finds all pending match records.
     * If no such match records exist, returns an empty list.
     * 
     * Throws ServiceException if there is a problem with the database.
     * 
     * @return
     * @throws ServiceException
     */
    @Override
    public List<MatchRecord> findAllPendingMatchRecords() throws ServiceException{
        try{
            return mRepo.findMatchRecordsBy(MatchStatusFilter.PENDING);
        } catch(RepositoryException e){
            throw new ServiceException("RepositoryException: " + e.getMessage());
        }
    }

    @Override
    public List<MatchRecord> findAllOngoingMatchRecords() throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    
    @Override
    public List<MatchRecord> findAllFinishedMatchRecords() throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

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
    @Override
    public List<MatchRecord> findAllMatchRecordsWithPlayer(User player) throws ServiceException {
        try{
            if (!uRepo.checkExists(player))
                throw new ServiceException(
                        "findAllMatchRecordsWithPlayer: User <" 
                        + player.getUsername() + "> not found.");
            return mRepo.findMatchRecordsBy(player);
        } catch(RepositoryException e){
            throw new ServiceException("RepositoryException: " + e.getMessage());
        }
    }

    @Override
    public List<MatchRecord> findAllOngoingMatchRecordsWithPlayer(User player) throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

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
    @Override
    public List<MatchRecord> findAllFinishedMatchRecordsWithPlayer(User player) 
        throws ServiceException {
        try{
            if (!uRepo.checkExists(player))
                throw new ServiceException(
                    "findAllFinishedMatchRecordsWithPlayer: User <" 
                    + player.getUsername() + "> not found.");
            return mRepo.findMatchRecordsBy(player, MatchStatusFilter.FINISHED);
        } catch(RepositoryException e){
            throw new ServiceException("RepositoryException: " + e.getMessage());
        }
    }

    @Override
    public List<MatchRecord> findAllMatchRecordsWithWinner(User player) throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<MatchRecord> findAllMatchRecordsWithLoser(User player) throws ServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Adds the given player as the blackPlayer of the game indicated by the given code.
     * Throws an exception if the MatchRecord does not exist, or if it already has a
     * black player - or, if the player accepting the code and trying to join is also the
     * white player
     * 
     * @param blackPlayer
     * @param code
     * @throws ServiceException
     */
    @Override
    public void acceptCode(User player, int code) throws ServiceException {
        try{
            if (!mRepo.checkExistsByCode(code))
                throw new ServiceException("No game with code <" + code + "> found.");
            MatchRecord mr = mRepo.findMatchRecordByCode(code);
            if (mr.getStatus() != MatchStatus.PENDING)
            throw new ServiceException("Game with code <" + code + "> is not PENDING.");
            mr.setBlackUser(player);
            mr.setStatus(MatchStatus.ONGOING);
            save(mr);
        } catch(RepositoryException e){
            throw new ServiceException("RepositoryException: " + e.getMessage());
        }
    }

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
    public void makeGame(User player, int code) throws ServiceException{
        try{
            if (mRepo.checkExistsByCode(code))
                throw new ServiceException("Game w/ code <" + code + "> already exists.");
            MatchRecord mr = new MatchRecord();
            mr.setWhiteUser(player);
            mr.setCode(code);
            mr.setStatus(MatchStatus.PENDING);
            mr.setMoveHistory("");
            save(mr);
        } catch(RepositoryException e){
            throw new ServiceException("RepositoryException: " + e.getMessage());
        }
    }

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
    public String getPlayerStringByCode(int code) throws ServiceException{
        try{
            if (!mRepo.checkExistsByCode(code))
                throw new ServiceException("Game w/ code <" + code + "> not found.");
            MatchRecord mr = findMatchRecordByCode(code);
            //System.out.println("DEBUG: found game with code: " + code);
            //System.out.println("DEBUG: whiteUser username is: " + mr.getWhiteUser().getUsername());
            //System.out.println("DEBUG: blackUser username is: " + mr.getBlackUser().getUsername());
            String playerString = mr.getWhiteUser().getUsername();
            if (mr.getStatus() != MatchStatus.PENDING)
                playerString = playerString + " " + mr.getBlackUser().getUsername();
            return playerString;
        } catch(RepositoryException e){
            throw new ServiceException("RepositoryException: " + e.getMessage());
        }
    }

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
    public void recordMatchWinner(int code, String username) throws ServiceException{
        try{
            if (!mRepo.checkExistsByCode(code))
                throw new ServiceException("Game w/ code <" + code + "> not found.");
            if (!uRepo.checkExists(username))
                throw new ServiceException("User <" + username + "> not found.");
            MatchRecord mr = findMatchRecordByCode(code);
            if (mr.getStatus() != MatchStatus.ONGOING)
                throw new ServiceException("Game w/ code <" + code + "> is not ONGOING.");
            User u = uRepo.findUser(username);
            if (mr.getWhiteUser().getId() == u.getId())
                mr.setStatus(MatchStatus.WHITE_VICTORY);
            else if (mr.getBlackUser().getId() == u.getId())
                mr.setStatus(MatchStatus.BLACK_VICTORY);
            else throw new ServiceException(
                "recordMatchWinner(): User <" + username 
                + "> is not one of the players in game w/ code <" + code + ">");
            mRepo.save(mr);
        } catch(RepositoryException e){
            throw new ServiceException("RepositoryException: " + e.getMessage());
        }
    }
}
