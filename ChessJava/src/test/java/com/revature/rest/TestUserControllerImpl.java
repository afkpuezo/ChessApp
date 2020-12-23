/**
 * This class contains unit tests for the UserControllerImpl class.
 * 
 * NOTE: it is not complete. There are many methods not yet specified.
 * 
 * @author Andrew Curry
 */
package com.revature.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.revature.model.User;
import com.revature.model.UserWithPassword;
import com.revature.rest.impl.UserControllerImpl;
import com.revature.rest.interfaces.UserController;
import com.revature.service.ServiceException;
import com.revature.service.interfaces.UserService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class TestUserControllerImpl {
    
    // ---------------------
    // SETUP / SUPPORT
    // ---------------------

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private UserController uCon; // actually impl
    private UserService uService;

    @Before
    public void setup(){
        uService = mock(UserService.class);
        uCon = new UserControllerImpl();
        uCon.useOutsideService(uService);
    }

    // ---------------------
    // TEST METHODS
    // ---------------------

    // ---------------------
    // register() TESTS
    // ---------------------

    /**
     * Expects a successful registration
     * 
     * @throws ServiceException
     */
    @Test
    public void testRegister() throws ServiceException{
        UserWithPassword uwp 
                = new UserWithPassword(0, "username", "email@fake.com", "pass");
        User expected = new User(1, "username", "email@fake.com");
        when(uService.register(Mockito.any(User.class), Mockito.anyString())).thenReturn(
                expected);
        User actual = uCon.registerUser(uwp);
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUsername(), actual.getUsername());
    }

    /**
     * Expects a failed registration
     * 
     * @throws ServiceException
     */
    @Test
    public void testRegisterFailed() throws ServiceException{
        UserWithPassword uwp 
                = new UserWithPassword(0, "username", "email@fake.com", "pass");
        when(uService.register(Mockito.any(User.class), Mockito.anyString())).thenReturn(
                null);
        User actual = uCon.registerUser(uwp);
        assertNull(actual);
    }

    /**
     * Expects a null due to service exception
     * 
     * @throws ServiceException
     */
    @Test
    public void testRegisterServiceExcept() throws ServiceException{
        UserWithPassword uwp 
                = new UserWithPassword(0, "username", "email@fake.com", "pass");
        when(uService.register(Mockito.any(User.class), Mockito.anyString())).thenThrow(
                new ServiceException());
        User actual = uCon.registerUser(uwp);
        assertNull(actual);
    }

    // ---------------------
    // logIn() TESTS
    // ---------------------

    /**
     * Expects a successful log in.
     * 
     * @throws ServiceException
     */
    @Test
    public void testLogIn() throws ServiceException {
        User expected = new User(1, "expected", "email@email.com");
        when(uService.logIn(Mockito.any(User.class), Mockito.anyString())).thenReturn(
                expected);
        String[] args = {"username", "barePassword"};
        User actual = uCon.logIn(args);
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUsername(), actual.getUsername());
    }

    /**
     * Expects a failed log in.
     * 
     * @throws ServiceException
     */
    @Test
    public void testLogInFail() throws ServiceException {
        when(uService.logIn(Mockito.any(User.class), Mockito.anyString())).thenReturn(
                null);
        String[] args = {"username", "barePassword"};
        User actual = uCon.logIn(args);
        assertNull(actual);
    }

    /**
     * Expects a failed log in due to a service exception
     * 
     * @throws ServiceException
     */
    @Test
    public void testLogInServExcept() throws ServiceException {
        when(uService.logIn(Mockito.any(User.class), Mockito.anyString())).thenThrow(
                new ServiceException());
        String[] args = {"username", "barePassword"};
        User actual = uCon.logIn(args);
        assertNull(actual);
    }
}
