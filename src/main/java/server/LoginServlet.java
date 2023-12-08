package server;

import org.apache.velocity.app.VelocityEngine;
import server.Database.DatabaseHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Logs in the user
 */
@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet implements AuthenticationServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VelocityEngine ve = (VelocityEngine) getServletContext().getAttribute("templateEngine");
        doGetHelper(request, response, "templates/login.html", ve);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<String> usernameAndPassword = getUserNameAndPassword(request);
        String user = usernameAndPassword.get(0);
        String password = usernameAndPassword.get(1);

        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        boolean flag = dbHandler.authenticateUser(user, password);

        PrintWriter out = response.getWriter();
        if (flag) {
            HttpSession session = request.getSession();
            session.setAttribute("username", user);
            sendValidJson(out);
        } else {
            sendInvalidJson("Invalid username or password", out);
        }
    }
}
