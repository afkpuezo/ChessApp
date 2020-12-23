/**
 * This class handles http requests related to matches.
 * 
 * NOTE: it is not complete. There are many methods not yet specified.
 * 
 * @author Andrew Curry
 */
package com.revature.rest.impl;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.revature.model.MatchRecord;
import com.revature.model.User;
import com.revature.rest.interfaces.MatchController;
import com.revature.service.ServiceException;
import com.revature.service.interfaces.MatchService;
import com.revature.service.interfaces.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@Controller("matchController")
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:4100"})
public class MatchControllerImpl implements MatchController {

    // ---------------------
    // INSTANCE VARIABLES
    // ---------------------

    @Autowired
    private MatchService mService;

    @Autowired
    private UserService uService;

    // ---------------------
    // UTILITY / TESTING METHODS
    // ---------------------

    @Override
    public void useOutsideService(MatchService mService) {
        this.mService = mService;
    }

    // ---------------------
    // REQUEST-HANDLING METHODS
    // ---------------------

    
    @PostMapping("/hello2")
    public @ResponseBody String helloWorld(){
    	System.out.println("hello worked");
        return "Hello world222!"; 
    }
    
    
    /*
     * need @ResponseStatus(HttpStatus.OK) or wont work
     * let template = {
          user: "user",
          code: code
      } -- look at testmakeGame for out to get elements
      -- user lowercase here
     */
    @PostMapping("/testGetMove")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String testGM(@RequestBody String req){
    	
    	System.out.println("Get Move"+ req);
        return "a4b5 a7a6"; 
    }
    
    
    /*
     * let template = {
             code: code,
             moves: moves
           }
     */
    @PostMapping("/testrecordMove")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody boolean testRM(@RequestBody String req){
    	
    	System.out.println("Recording Move: "+req);
        return true; 
    }
    
    /*
     * let template = {
          whiteUser: "user",
          code: code
      }
      --to get string convert to json, then use get and toString()
     */
    @PostMapping("/testmakeGame")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody boolean testMG( 
            @RequestBody String req){
    	Gson gson=new Gson();
    	JsonObject json = new Gson().fromJson(req, JsonObject.class);
    	//JsonElement j = json.get("whiteUser");
    	String w = json.get("whiteUser").getAsString();
        String code = json.get("code").getAsString();
    	System.out.println(req);
        System.out.println("username is: " + w + ", and code is: " + code);
        System.out.println("is username.equals to user?: " + w.equals("user"));
    	
        return false; 
    }
    
    @PostMapping("/checkMatchRecord")
    @Override
    public @ResponseBody MatchRecord checkMatchRecord(@RequestBody int id) {
        return null;
    }

    /**
     * Returns the entire moveHistory string from the requested MatchRecord
     * Returns null if failure
     * 
     * let template = {user: "user",code: code}
     * 
     * @param req
     * @return
     */
    @PostMapping("/getMove")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public @ResponseBody String getMove(@RequestBody String req) {
        try{
            JsonObject json = new Gson().fromJson(req, JsonObject.class);
            String codeString = json.get("code").getAsString();
            int code = Integer.parseInt(codeString);
            MatchRecord mr = mService.findMatchRecordByCode(code);
            if (mr == null) return null;
            return mr.getMoveHistory();
        } catch(ServiceException e){
            return null;
        }
    }

    /**
     * Replaces the entire moveHistory string on the requested MatchRecord
     * Returns false if failure
     * 
     * let template = {code: code, moves: moves}
     * 
     * @param req
     * @return
     */
    @PostMapping("/recordMove")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public @ResponseBody boolean recordMove(@RequestBody String req) {
        try{
            JsonObject json = new Gson().fromJson(req, JsonObject.class);
            String codeString = json.get("code").getAsString();
            int code = Integer.parseInt(codeString);
            MatchRecord mr = mService.findMatchRecordByCode(code);
            if (mr == null) return false;
            String moveHistory = json.get("moves").getAsString();
            mr.setMoveHistory(moveHistory);
            mService.save(mr);
            return true; // if no error, success
        } catch(ServiceException e){
            return false;
        }
    }

    /**
     * Starts a new game with the given code, with the given player(username) as white
     * Returns false if failure
     * 
     * let template = {whiteUser: "user",code: code}
     * 
     * @param req
     * @return
     */
    @PostMapping("/makeGame")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public @ResponseBody boolean makeGame(@RequestBody String req) {
    	try{
            JsonObject json = new Gson().fromJson(req, JsonObject.class);
            String username = json.get("whiteUser").getAsString();
            User u = uService.findUser(username);
            if (u == null) return false;
            String codeString = json.get("code").getAsString();
            int code = Integer.parseInt(codeString);
            mService.makeGame(u, code);
            return true; // if no error, success
        } catch(ServiceException e){
        	//System.out.println("make game fail");
            return false;
        }
    }

    /**
     * Adds the given player as black to the pending game indicated by the given code.
     * Returns false if failure
     * 
     * let template = {user: username,code: code}
     * 
     * @param req
     * @return
     */
    @PostMapping("/findGame")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public @ResponseBody boolean findGame(@RequestBody String req) {
        try{
            JsonObject json = new Gson().fromJson(req, JsonObject.class);
            String username = json.get("user").getAsString();
            System.out.println("DEBUG: about to findUser by username");
            User u = uService.findUser(username);
            if (u == null) return false;
            String codeString = json.get("code").getAsString();
            int code = Integer.parseInt(codeString);
            mService.acceptCode(u, code);
            return true; // if no error, success
        } catch(ServiceException e){
            return false;
        }
    }

    /**
     * Returns a list of every match which has the status PENDING (waiting for a player).
     * Returns an empty list if there are no pending games.
     * Returns null if there is a problem.
     * 
     * @return
     */
    @Override
    @GetMapping("/getAllPendingGames")
    public @ResponseBody List<MatchRecord> getAllPendingGames(){
        try{
            return mService.findAllPendingMatchRecords();
        } catch(ServiceException e){
            return null;
        }
    }

    /**
     * Returns a list of every match in which the given user is one of the players.
     * Returns an empty list if there are no such games.
     * Returns null if there is a problem.
     * 
     * @param user
     * @return
     */
    @Override
    @PostMapping("/getAllGamesWithPlayer")
    public @ResponseBody List<MatchRecord> getAllGamesWithPlayer(@RequestBody User user){
        try{
            return mService.findAllMatchRecordsWithPlayer(user);
        } catch(ServiceException e){
            return null;
        }
    }

    /**
     * Given a game code, finds the game with that code, then returns a string containing
     * the usernames of the two players in it, seperated by a space.
     * If there is only one player (if the game is pending), only that user's username is
     * returned.
     * 
     * Returns null if there is a problem.
     * 
     * let template = {code: code}
     * 
     * @param code
     * @throws ServiceException
     */
    @Override
    @PostMapping("/getPlayerStringByCode")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String getPlayerStringByCode(@RequestBody String req){
        try{
            JsonObject json = new Gson().fromJson(req, JsonObject.class);
            String codeString = json.get("code").getAsString();
            //System.out.println("DEBUG: parsed code as: " + codeString);
            int code = Integer.parseInt(codeString);
            String playerString = mService.getPlayerStringByCode(code);
            //System.out.println("DEBUG: playerString is: " + playerString);
            return playerString;
        } catch(ServiceException e){
            //System.out.println("DEBUG: ServiceException: " + e.getMessage());
            return null;
        }
    }

    /**
     * Given a game code and a username, makes that user the winner of that game.
     * Will fail if the game or username are not found, if the user is not one of the
     * players in the game, or if the game is not ONGOING.
     * 
     * let template = {code: code, user: username}
     * 
     * @param req
     * @return
     */
    @Override
    @PostMapping("/recordGameWinner")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody boolean recordGameWinner(@RequestBody String req){
        try{
            JsonObject json = new Gson().fromJson(req, JsonObject.class);
            String codeString = json.get("code").getAsString();
            int code = Integer.parseInt(codeString);
            String username = json.get("user").getAsString();
            mService.recordMatchWinner(code, username);
            return true; // if no errors, success
        } catch(ServiceException e){
            //System.out.println("DEBUG: ServiceException: " + e.getMessage());
            return false;
        }
    }

    /**
     * Given a user, return a list of all the FINISHED games that player is/was in.
     * Returns an empty list if no such games are found.
     * 
     * Returns null on failure/error, such as if the user is not found.
     * 
     * @param user
     * @return
     */
    @Override
    @PostMapping("/getMatchHistoryOfPlayer")
    public @ResponseBody List<MatchRecord> getMatchHistoryOfPlayer(
        @RequestBody User user){
        try{
            return mService.findAllFinishedMatchRecordsWithPlayer(user);
        } catch(ServiceException e){
            //System.out.println("DEBUG: ServiceException: " + e.getMessage());
            return null;
        }
    }
}
