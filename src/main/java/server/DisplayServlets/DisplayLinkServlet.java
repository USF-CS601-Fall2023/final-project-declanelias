package server.DisplayServlets;

import server.Database.DatabaseHandler;
import server.Database.Link;
import server.ServletHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Displays the link historu
 */
public class DisplayLinkServlet extends HttpServlet {

    private String username;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletHelper helper = new ServletHelper(request, response);
        username = helper.getUsername();
        Set<Link> links = DatabaseHandler.getInstance().getLinkHistory(username);

        helper.checkLoginStatus();
        helper.addContext("links", links);
        helper.doGet("templates/linkHistory.html");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DatabaseHandler.getInstance().clearLinkHistory(username);
    }
}
