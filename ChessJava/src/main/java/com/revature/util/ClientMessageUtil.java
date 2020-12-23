/**
 * This is taken from the SpringMVCAngular demo from class.
 * 
 * @author Andrew Curry (well, really it's Sophia Gavrila)
 */
package com.revature.util;

import com.revature.ajax.ClientMessage;

public class ClientMessageUtil {

        public static final ClientMessage REGISTRATION_SUCCESSFUL 
            = new ClientMessage("REGISTRATION SUCCESSFUL");
            
        public static final ClientMessage SOMETHING_WRONG 
            = new ClientMessage("SOMETHING WENT WRONG");
            
        public static final ClientMessage LOG_IN_SUCCESSFUL 
            = new ClientMessage("LOG IN SUCCESSFUL");
}