package server.HotelInfoServlets;

import HotelData.Hotel;
import server.Database.DatabaseHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class FavoriteHotelServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = (String) req.getSession().getAttribute("username");
        if (username == null) {
            resp.sendRedirect("/loginRequired");
        }


        Set<Hotel> hotels = DatabaseHandler.getInstance().getFavoriteHotels(username);

        hotels.forEach(System.out::println);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String hotelId = req.getParameter("id");
        String username = (String) req.getSession().getAttribute("username");

        DatabaseHandler.getInstance().addFavoriteHotel(username, hotelId);
    }
}
