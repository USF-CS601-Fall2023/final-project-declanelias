package server;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import server.Database.DatabaseHandler;
import server.Database.Link;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

public class DisplayLinkServlet extends HttpServlet implements HotelServlet {

    private String username;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        username = (String) session.getAttribute("username");

        if (username == null) {
            response.sendRedirect("/loginRequired");
        }

        Set<Link> links = DatabaseHandler.getInstance().getLinkHistory(username);

        VelocityEngine ve = (VelocityEngine) getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        context.put("links", links);
        doGetHelper(request, response, "templates/linkHistory.html", ve, context);


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DatabaseHandler.getInstance().clearLinkHistory(username);
    }
}
