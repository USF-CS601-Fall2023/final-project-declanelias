package server.DisplayServlets;

import HotelData.Hotel;
import server.Database.DatabaseHandler;
import server.ServletHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;


/**
 * Displays favorite hotels
 */
public class FavoriteHotelServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletHelper helper = new ServletHelper(request, response);
        String username = helper.getUsername();
        Set<Hotel> hotels = DatabaseHandler.getInstance().getFavoriteHotels(username);

        helper.checkLoginStatus();
        helper.addContext("hotels", hotels);
        helper.doGet("templates/favoriteHotels.html");
    }
}
