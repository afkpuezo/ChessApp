/**
 * This class contains tests for the UserRepositoryImpl class.
 * 
 * It currently has a clusmy way of getting around my problems with Spring.
 * 
 * NOTE: currently, UserRepoImpl only implements limited functionality.
 * 
 * @author Andrew Curry
 */
package com.revature.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import com.revature.model.User;
import com.revature.model.UserPassword;
import com.revature.repository.impl.UserRepositoryImpl;
import com.revature.repository.interfaces.UserRepository;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

@Ignore // broken
public class TestUserRepoImpl {

    // ---------------------
    // SETUP / SUPPORT
    // ---------------------
    
    private final String fileName = "testHibernate.cfg.xml";
    private UserRepository urepo; // will actually be Impl
    private SessionFactory sfactory; // used to manuaully change/check data, NOT by repo

    @Before
    @SuppressWarnings(value="all") // what could go wrong?
    public void setup(){
        urepo = new UserRepositoryImpl();
        urepo.useOutsideSessionFactory(
                new Configuration().configure(fileName).buildSessionFactory());
        //sfactory = new Configuration().configure(fileName).buildSessionFactory();
        //ContextLoader conLoader = new ContextLoader();
    }

    // ---------------------
    // checkExists() TESTS
    // ---------------------

    @Test
    public void testCheckExists() throws RepositoryException {
        // look for user(s) that aren't there
        assertFalse(urepo.checkExists(12345));
        assertFalse(urepo.checkExists("notAUser"));
        User notAUser = new User(12345, "notAUser", "not@email.com");
        assertFalse(urepo.checkExists(notAUser));
        // now put one in there and see if we can find it
        User realUser = new User("user", "real@email.com");
        Session session = sfactory.openSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(realUser);
        tx.commit();
        assertTrue(urepo.checkExists(realUser));
        assertTrue(urepo.checkExists(realUser.getId()));
        assertTrue(urepo.checkExists(realUser.getUsername()));
    }

    // ---------------------
    // register() TESTS
    // ---------------------
    
    @Test
    @SuppressWarnings(value="unchecked")
    public void testRegister() throws RepositoryException {
        // register a new user
        User  addMe = new User("addMe", "add@email.com");
        String addPassword = "password";
        User added = urepo.register(addMe, addPassword);
        assertNotNull(added);
        assertEquals(addMe.getUsername(), added.getUsername());
        assertEquals(addMe.getEmail(), added.getEmail());
        // lets manually verify that it exists
        Session session = sfactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        Criteria crit = session.createCriteria(User.class);
        crit.add(Restrictions.eq("username", "addMe"));
        List<User> userList = crit.list();
        tx.commit();
        assertEquals(1, userList.size());
        // now what happens when we try to register a user that already exists?
        boolean wasCaught = false;
        try{
            urepo.register(addMe, "password");
        }
        catch(RepositoryException e){
            wasCaught = true;
        }
        assertTrue(wasCaught);
    }

    // ---------------------
    // findUser() TESTS
    // ---------------------

    /**
     * Tests by id, by username, and by user
     * 
     * @throws RepositoryException
     */
    @Test
    public void testFindUser() throws RepositoryException {
        // try finding one that doesn't exist
        User target = new User("notFound", "not@email.com");
        User notFound;
        notFound = urepo.findUser("notFound");
        assertNull(notFound);
        notFound = urepo.findUser(12345);
        assertNull(notFound);
        notFound = urepo.findUser(target);
        assertNull(notFound);
        // put one in there to be found
        target.setUsername("found");
        Session session = sfactory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(target);
        tx.commit();
        User found;
        found = urepo.findUser(target);
        assertNotNull(found);
        found = urepo.findUser(target.getId());
        assertNotNull(found);
        found = urepo.findUser(target.getUsername());
        assertNotNull(found);
    }

    // ---------------------
    // checkPassword() TESTS
    // ---------------------

    @Test
    public void testCheckPassword() throws RepositoryException {
        // look for a user that doesn't exist
        User notFound = new User("notFound");
        boolean wasCaught;
        wasCaught = false;
        try{
            urepo.checkPassword(notFound, "n/a");
        } catch(RepositoryException e){
            if (!e.getMessage().startsWith("Hibernate")) wasCaught = true;
        }
        assertTrue(wasCaught);
        // put in a user without a password
        User userNoPass = new User("noPass");
        Session session = sfactory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(userNoPass);
        tx.commit();
        wasCaught = false;
        try{
            urepo.checkPassword(userNoPass, "noPassFound");
        } catch(RepositoryException e){
            if (!e.getMessage().startsWith("Hibernate")) wasCaught = true;
        }
        assertTrue(wasCaught);
        // put in a user WITH a password
        User good = new User("user");
        session = sfactory.openSession();
        tx = session.beginTransaction();
        session.save(good);
        String encryptedPassword = BCrypt.hashpw("password", BCrypt.gensalt());
        UserPassword goodPass = new UserPassword(good, encryptedPassword);
        session.save(goodPass);
        tx.commit();
        // now check it
        assertTrue(urepo.checkPassword(good, "password"));
        assertFalse(urepo.checkPassword(good, "wrong"));
    }

    // ---------------------
    // update() TESTS
    // ---------------------

    @Test
    public void testUpdate() throws RepositoryException{
        // if we try to update a user that isn't persisted, it should fail
        boolean caught = false;
        User u;
        try{
            u = new User(4, "user", "email@email.com");
            urepo.update(u);
        } catch (RepositoryException e){
            if (!e.getMessage().startsWith("HibernateException"))
                caught = true;  
        }
        assertTrue(caught);
    }
}
