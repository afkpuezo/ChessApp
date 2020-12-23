/**
 * This class is for manual testing/experimentation with integrating hibernate and spring.
 * 
 * @author Andrew Curry
 */
package com.revature.experiment;

import java.util.List;

import com.revature.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("demoService")
public class DemoService {
    
    @Autowired
    private DemoRepo dr;

    public List<User> getAllUsers(){
        return dr.getAllUsers();
    }
}
