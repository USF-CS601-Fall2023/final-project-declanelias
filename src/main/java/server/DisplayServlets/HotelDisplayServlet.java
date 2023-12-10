package server.DisplayServlets;

import HotelData.*;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import server.Database.DatabaseHandler;
import server.HotelServlet;
import server.ServletHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        String hotelId = request.getParameter("hotelId");
        Hotel hotel = DatabaseHandler.getInstance().getHotel(hotelId);

        ServletHelper helper = new ServletHelper(request, response);
        helper.addContext("hotel", hotel);
        helper.doGet("templates/hotel.html");
    }
}
