package server;

import org.apache.velocity.VelocityContext;

public class ServletHelper {

    public static void setLoggedInStatus(String username, VelocityContext context) {
        if (username == null) {
            context.put("isLoggedIn", false);
        } else {
            context.put("isLoggedIn", true);
            context.put("username", username);
        }
    }

}
