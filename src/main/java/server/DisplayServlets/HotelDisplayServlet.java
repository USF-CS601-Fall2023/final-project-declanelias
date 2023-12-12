package server.DisplayServlets;

import HotelData.*;
import server.Database.DatabaseHandler;
import server.ServletHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Displays the reviews for a hotel
 */
public class HotelDisplayServlet extends HttpServlet {

    /**
     * Handles get requests
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        String hotelId = request.getParameter("hotelId");
        Hotel hotel = dbHandler.getHotel(hotelId);

        ServletHelper helper = new ServletHelper(request, response);
        String username = helper.getUsername();

        Set<Hotel> hotels = DatabaseHandler.getInstance().getFavoriteHotels(username);

        boolean isFavorite = hotels.stream()
                .anyMatch(h -> h.getId().equals(hotelId));

        helper.addContext("isFavorite", isFavorite);
        helper.addContext("hotel", hotel);
        helper.doGet("templates/hotel.html");
    }
}
