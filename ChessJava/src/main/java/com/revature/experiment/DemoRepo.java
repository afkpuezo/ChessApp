/**
 * This class is for manual testing/experimentation with integrating hibernate and spring.
 * 
 * @author Andrew Curry
 */
package com.revature.experiment;

import java.util.List;

import com.revature.model.User;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("demoRepo")
@Transactional
public class DemoRepo {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    @SuppressWarnings(value="unchecked")
    public List<User> getAllUsers(){
        Session session = sessionFactory.getCurrentSession();
        return (List<User>)session.createCriteria(User.class).list();
    }
}
