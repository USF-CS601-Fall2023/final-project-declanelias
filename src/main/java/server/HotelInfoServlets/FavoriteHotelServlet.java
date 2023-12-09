package server.HotelInfoServlets;

import HotelData.Hotel;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import server.Database.DatabaseHandler;
import server.Database.Link;
import server.HotelServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class FavoriteHotelServlet extends HttpServlet implements HotelServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getSession().getAttribute("username");
        if (username == null) {
            response.sendRedirect("/loginRequired");
        }


        Set<Hotel> hotels = DatabaseHandler.getInstance().getFavoriteHotels(username);

        VelocityEngine ve = (VelocityEngine) getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        context.put("username", username);
        context.put("hotels", hotels);
        doGetHelper(request, response, "templates/favoriteHotels.html", ve, context);
    }
}
