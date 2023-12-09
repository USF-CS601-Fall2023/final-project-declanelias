package server;

import HotelData.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import server.Database.DatabaseHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

/**
 * Displays the reviews for a hotel
 */
public class HotelDisplayServlet extends HttpServlet implements HotelServlet {

    VelocityEngine ve;
    VelocityContext context;
    String hotelId;
    DatabaseHandler dbHandler = DatabaseHandler.getInstance();

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
        ve = (VelocityEngine) getServletContext().getAttribute("templateEngine");
        context = new VelocityContext();

        hotelId = request.getParameter("hotelId");
        Hotel hotel = dbHandler.getHotel(hotelId);

        context.put("hotel", hotel);

        doGetHelper(request, response, "templates/hotel.html", ve, context);
    }

    /**
     * calculates the average rating of the reviews
     *
     * @param reviews
     * @return average rating
     */
    private double calcAvgRating(Set<HotelReview> reviews) {
        return reviews
                .stream()
                .mapToDouble(HotelReview::getAverageRating)
                .average()
                .orElse(0);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Set<HotelReview> hotelReviewSet = dbHandler.getReviews(hotelId);

        Double averageRating = calcAvgRating(hotelReviewSet);
        JsonArray jsonArray = new JsonArray();
        hotelReviewSet.forEach(review -> jsonArray.add(review.toJson()));

        JsonObject json = new JsonObject();
        json.addProperty("averageRating", averageRating);
        json.add("reviews", jsonArray);
        response.getWriter().println(json);

    }
}
