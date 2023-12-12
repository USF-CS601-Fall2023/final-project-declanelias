package server.AuthenticationServlets;

import server.ServletHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Displays if user tries to access a page that requires login without being logged in
 */
public class LoginRequiredServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletHelper helper = new ServletHelper(request, response);
        helper.doGet("templates/notLoggedIn.html");
    }
}
