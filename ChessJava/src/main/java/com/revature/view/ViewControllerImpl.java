/**
 * This is taken from the SpringMVCAngular demo from class.
 * 
 * @author Andrew Curry (well, really it's Sophia Gavrila)
 */
package com.revature.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("viewController") // the @Controller this marks the class as a ewb request handler
public class ViewControllerImpl implements ViewController{
    
    @RequestMapping(value = { "/", "/index"}, method = RequestMethod.GET)
	public String index() {
		return null;
	}
}
