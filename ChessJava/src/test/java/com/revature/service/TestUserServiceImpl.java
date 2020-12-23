/**
 * This class contains unit tests for the UserServiceImpl class
 * 
 * NOTE: UserServiceImpl is not yet fully implemented
 * 
 * @author Andrew Curry
 */
package com.revature.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.revature.model.User;
import com.revature.repository.RepositoryException;
import com.revature.repository.interfaces.UserRepository;
import com.revature.service.impl.UserServiceImpl;
import com.revature.service.interfaces.UserService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class TestUserServiceImpl {
    
    // ---------------------
    // SETUP / SUPPORT
    // ---------------------

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private UserService uService; // will actually be Impl
    private UserRepository uRepo;

    @Before
    @SuppressWarnings(value="all") // what could go wrong?
    public void setup(){
        uRepo = mock(UserRepository.class);
        uService = new UserServiceImpl();
        uService.useOutsideRepository(uRepo);
    }

    // ---------------------
    // TESTS
    // ---------------------

    // ---------------------
    // register() TESTS
    // ---------------------

    /**
     * Expects a successful registration
     * 
     * @throws ServiceException
     * @throws RepositoryException
     */
    @Test
    public void testRegister() throws ServiceException, RepositoryException{
        User uIn = new User("user", "email");
        String bp = "password";
        User uOut = new User(1, "user", "email");
        when(uRepo.checkExists(uIn)).thenReturn(false);
        when(uRepo.register(uIn, bp)).thenReturn(uOut);
        User result = uService.register(uIn, bp);
        assertNotNull(result);
        assertEquals(result.getId(), uOut.getId());
        assertEquals(result.getUsername(), uOut.getUsername());
    }

    /**
     * Expects a failed registration due to the user already existing
     * 
     * @throws ServiceException
     * @throws RepositoryException
     */
    @Test
    public void testRegisterAlreadyExists() throws ServiceException, RepositoryException{
        User uIn = new User("user", "email");
        String bp = "password";
        when(uRepo.checkExists(uIn)).thenReturn(false);
        User result = uService.register(uIn, bp);
        assertNull(result);
    }

    /**
     * Expects a failed registration due to RepoExcept
     * 
     * @throws ServiceException
     * @throws RepositoryException
     */
    @Test
    public void testRegisterRepoExcept() throws RepositoryException{
        boolean caught = false;
        try{
            User uIn = new User("user", "email");
            String bp = "password";
            when(uRepo.checkExists(uIn)).thenReturn(false);
            when(uRepo.register(uIn, bp)).thenThrow(new RepositoryException());
            uService.register(uIn, bp);
        } catch (ServiceException e){
            caught = true;
        }
        assertTrue(caught);
    }

    // ---------------------
    // logIn() TESTS
    // ---------------------

    /**
     * Expects a successful log in.
     */
    @Test
    public void testLogIn() throws RepositoryException, ServiceException {
        User sent = new User("user");
        String ap = "password";
        User expected = new User(1, "user", "user@email.com");
        when(uRepo.findUser(sent)).thenReturn(expected);
        when(uRepo.checkPassword(expected, ap)).thenReturn(true);
        User actual = uService.logIn(sent, ap);
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUsername(), actual.getUsername());
    }

    /**
     * Expects a failed log in, due to no user found
     */
    @Test
    public void testLogInNotFound() throws RepositoryException, ServiceException {
        User sent = new User("user");
        String ap = "password";
        User actual = uService.logIn(sent, ap);
        assertNull(actual);
    }

    /**
     * Expects a failed log in, due to wrong password
     */
    @Test
    public void testLogInBadPass() throws RepositoryException, ServiceException {
        User sent = new User("user");
        String ap = "password";
        User expected = new User(1, "user", "user@email.com");
        when(uRepo.findUser(sent)).thenReturn(expected);
        when(uRepo.checkPassword(expected, ap)).thenReturn(false);
        User actual = uService.logIn(sent, ap);
        assertNull(actual);
    }

    /**
     * Expects a failed log in, due to repository exception
     */
    @Test
    public void testLogInRepoException() throws RepositoryException {
        boolean caught = false;
        try {
            User sent = new User("user");
            String ap = "password";
            when(uRepo.findUser(sent)).thenThrow(new RepositoryException());
            uService.logIn(sent, ap);
        } catch (ServiceException e){
            caught = e.getMessage().startsWith("RepositoryException");
        }
        assertTrue(caught);
    }

    // ---------------------
    // update() TESTS
    // ---------------------

    /**
     * Expects a successful update
     * 
     * @throws RepositoryException
     */
    @Test
    public void testUpdate() throws RepositoryException, ServiceException {
        User u = new User(1, "username", "email@e.com");
        when(uRepo.checkExists(u)).thenReturn(true);
        uService.update(u);
        // if no errors, success
    }

    /**
     * Expects a failed update due to the user not being found
     * 
     * @throws RepositoryException
     */
    @Test
    public void testUpdateNotFound() throws RepositoryException {
        User u = new User(1, "username", "email@e.com");
        when(uRepo.checkExists(u)).thenReturn(false);
        boolean caught = false;
        try{
            uService.update(u);
        } catch(ServiceException e){
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * Expects a failed update due to a repo exception
     * 
     * @throws RepositoryException
     */
    @Test
    public void testUpdateRepoExcept() throws RepositoryException {
        User u = new User(1, "username", "email@e.com");
        when(uRepo.checkExists(u)).thenThrow(new RepositoryException());
        boolean caught = false;
        try{
            uService.update(u);
        } catch(ServiceException e){
            if (e.getMessage().startsWith("RepositoryException"))
                caught = true;
        }
        assertTrue(caught);
    }

    // ---------------------
    // findUser() TESTS
    // ---------------------

    // currently, not testing findUser methods because they literally just pass a param
    // to the repo method

    // ---------------------
    // findAllUsers() TESTS
    // ---------------------

    // currently, not testing the findAllUsers method because it literally just calls the
    // repo method
}
