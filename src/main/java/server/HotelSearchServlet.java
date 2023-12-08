package server;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import server.Database.DatabaseHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles endpoint /hotelSearch which searches for a hotel given a keyword
 */
public class HotelSearchServlet extends HttpServlet implements HotelServlet{

    VelocityEngine ve;
    VelocityContext context;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ve = (VelocityEngine) getServletContext().getAttribute("templateEngine");
        context = new VelocityContext();

        doGetHelper(request, response, "templates/hotelSearch.html", ve, context);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String word = request.getParameter("word");
        word = word.toLowerCase();

        DatabaseHandler dbHandler = DatabaseHandler.getInstance();

        context.put("hotels", dbHandler.getHotelsByKeyword(word));

        doGetHelper(request, response, "templates/hotelInfo.html", ve, context);
    }
}
