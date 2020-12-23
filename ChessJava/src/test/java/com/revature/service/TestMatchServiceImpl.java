/**
 * This class contains unit tests for the MatchServiceImpl class
 * 
 * NOTE: MatchServiceImpl is not yet fully implemented
 * 
 * @author Andrew Curry
 */
package com.revature.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.revature.model.MatchRecord;
import com.revature.model.User;
import com.revature.model.MatchRecord.MatchStatus;
import com.revature.repository.RepositoryException;
import com.revature.repository.interfaces.MatchRepository;
import com.revature.service.impl.MatchServiceImpl;
import com.revature.service.interfaces.MatchService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class TestMatchServiceImpl {
    
    // ---------------------
    // SETUP / SUPPORT
    // ---------------------

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private MatchService mService; // will actually be impl
    private MatchRepository mRepo; // will be mocked

    @Before
    @SuppressWarnings(value="all") // what could go wrong?
    public void setup(){
        mRepo = mock(MatchRepository.class);
        mService = new MatchServiceImpl();
        mService.useOutsideRepository(mRepo);
    }

    // ---------------------
    // TESTS
    // ---------------------

    // ---------------------
    // save() TESTS
    // ---------------------

    // currently not testing this because it just calls the repo

    // ---------------------
    // find-by-mr/id/code TESTS
    // ---------------------

    // currently not testing these because it just calls the repo

    // ---------------------
    // acceptCode() TESTS
    // ---------------------

    /**
     * Expects success
     */
    @Test
    public void testAcceptCode() throws RepositoryException, ServiceException{
        User u = new User(1, "user", "email@fake.com");
        int code = 12345;
        MatchRecord mr = new MatchRecord();
        mr.setStatus(MatchStatus.PENDING);
        when(mRepo.checkExistsByCode(code)).thenReturn(true);
        when(mRepo.findMatchRecordByCode(code)).thenReturn(mr);
        mService.acceptCode(u, code);
        // see if the game was updated
        assertEquals(u.getId(), mr.getBlackUser().getId());
        assertEquals(MatchStatus.ONGOING, mr.getStatus());
        // NOTE: technically doesn't listen to see if the repo is asked to persist mr
        //verify(mRepo.save(mr), times(1));
    }

    /**
     * Expects failure due to game not existing
     */
    @Test
    public void testAcceptCodeNotFound() throws RepositoryException, ServiceException{
        User u = new User(1, "user", "email@fake.com");
        int code = 12345;
        when(mRepo.checkExistsByCode(code)).thenReturn(false);
        boolean caught = false;
        try{
            mService.acceptCode(u, code);
        } catch(ServiceException e){
            if (!e.getMessage().startsWith("RepositoryException"))
                caught = true;
        }
        assertTrue(caught);
    }

    /**
     * Expects failure due to game not being pending
     */
    @Test
    public void testAcceptCodeNotPending() throws RepositoryException, ServiceException{
        User u = new User(1, "user", "email@fake.com");
        int code = 12345;
        MatchRecord mr = new MatchRecord();
        mr.setStatus(MatchStatus.ONGOING);
        when(mRepo.checkExistsByCode(code)).thenReturn(true);
        when(mRepo.findMatchRecordByCode(code)).thenReturn(mr);
        boolean caught = false;
        try{
            mService.acceptCode(u, code);
        } catch(ServiceException e){
            if (!e.getMessage().startsWith("RepositoryException"))
                caught = true;
        }
        assertTrue(caught);
    }

    /**
     * Expects failure due to RepoException
     */
    @Test
    public void testAcceptCodeRepoExcept() throws RepositoryException, ServiceException{
        User u = new User(1, "user", "email@fake.com");
        int code = 12345;
        MatchRecord mr = new MatchRecord();
        mr.setStatus(MatchStatus.ONGOING);
        when(mRepo.checkExistsByCode(code)).thenThrow(new RepositoryException("test"));
        boolean caught = false;
        try{
            mService.acceptCode(u, code);
        } catch(ServiceException e){
            if (e.getMessage().contains("test"))
                caught = true;
        }
        assertTrue(caught);
    }

    // ---------------------
    // makeGame() TESTS
    // ---------------------

    /**
     * Expects success
     */
    @Test
    public void testMakeGame() throws RepositoryException, ServiceException {
        User u = new User(1, "white", "white@email.com");
        int code = 54321;
        when(mRepo.checkExistsByCode(code)).thenReturn(false);
        mService.makeGame(u, code);
        // if no errors, we're good
    }

    /**
     * Expects failure due to game existing
     */
    @Test
    public void testMakeGameAlreadyExists() throws RepositoryException, ServiceException {
        User u = new User(1, "white", "white@email.com");
        int code = 54321;
        when(mRepo.checkExistsByCode(code)).thenReturn(true);
        boolean caught = false;
        try{
            mService.makeGame(u, code);
        } catch(ServiceException e){
            if (!e.getMessage().startsWith("RepositoryException"))
                caught = true;
        }
        assertTrue(caught);
    }

    /**
     * Expects failure due to repo exception
     */
    @Test
    public void testMakeGameRepoExcept() throws RepositoryException, ServiceException {
        User u = new User(1, "white", "white@email.com");
        int code = 54321;
        when(mRepo.checkExistsByCode(code)).thenThrow(new RepositoryException("test"));
        boolean caught = false;
        try{
            mService.makeGame(u, code);
        } catch(ServiceException e){
            if (e.getMessage().contains("test"))
                caught = true;
        }
        assertTrue(caught);
    }
}
