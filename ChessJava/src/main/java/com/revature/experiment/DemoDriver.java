/**
 * This class is for manual testing/experimentation with integrating hibernate and spring.
 * 
 * @author Andrew Curry
 */
package com.revature.experiment;

import java.util.List;

import com.revature.model.User;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DemoDriver {

    public static void main(String[] args){
        System.out.println("-----DemoDriver Start-----");
        ConfigurableApplicationContext appContext = new ClassPathXmlApplicationContext(
                "testApplicationContext.xml");
        DemoService ds = appContext.getBean("demoService", DemoService.class);
        List<User> userList = ds.getAllUsers();
        System.out.println("----------");
        System.out.println("Number of users found in database: " + userList.size());
        System.out.println("----------");
        appContext.close();
        System.out.println("-----DemoDriver End-----");
    }
}
