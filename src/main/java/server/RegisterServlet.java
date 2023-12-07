package server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * Servlet to register a new user
 */
public class RegisterServlet extends HttpServlet implements AuthenticationServlet{


    /**
     * Handles get request to the server
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VelocityEngine ve = (VelocityEngine) getServletContext().getAttribute("templateEngine");
        doGetHelper(request, response, "templates/register.html", ve);
    }

    /**
     * Handles post request to the server
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<String> usernameAndPassword = getUserNameAndPassword(request);
        String user = usernameAndPassword.get(0);
        String password = usernameAndPassword.get(1);

        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        boolean usernameExists = dbHandler.checkIfUsernameExists(user);
        boolean validUsername = validateUsername(user);
        boolean validPassword = validatePassword(password);

        PrintWriter out = response.getWriter();

        if (!validUsername && !validPassword) {
            sendInvalidJson("Invalid username and password", out);
        } else if (!validUsername) {
            sendInvalidJson("Invalid username", out);
        } else if (!validPassword){
            sendInvalidJson("Invalid password", out);
        } else if (usernameExists) {
            sendInvalidJson("Username taken", out);
        } else {
            dbHandler.registerUser(user, password);
            sendValidJson(out);
        }
    }

    private boolean validateUsername(String username) {
        return username.matches("[a-zA-Z][a-zA-Z0-9.-_]{0,29}");
    }

    private boolean validatePassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d]).{8,}$");
    }
}
