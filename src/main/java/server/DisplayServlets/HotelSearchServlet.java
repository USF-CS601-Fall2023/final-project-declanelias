package server.DisplayServlets;

import HotelData.Hotel;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import server.Database.DatabaseHandler;
import server.ServletHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Handles endpoint /hotelSearch which searches for a hotel given a keyword
 */
public class HotelSearchServlet extends HttpServlet {

    VelocityEngine ve;
    VelocityContext context;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletHelper helper = new ServletHelper(request, response);
        helper.doGet("templates/hotelSearch.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String word = request.getParameter("word");
        word = word.toLowerCase();

        System.out.println(word);

        DatabaseHandler dbHandler = DatabaseHandler.getInstance();

        Set<Hotel> hotels = dbHandler.getHotelsByKeyword(word);

        JsonObject json = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        hotels.forEach(h -> jsonArray.add(h.toJson()));
        json.add("hotels", jsonArray);

        response.getWriter().println(json);
    }
}
