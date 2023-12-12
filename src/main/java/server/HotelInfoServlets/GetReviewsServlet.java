package server.HotelInfoServlets;

import HotelData.HotelReview;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import server.Database.DatabaseHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Gets reviews from database
 */
public class GetReviewsServlet extends HttpServlet {

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

        String username = (String) request.getSession().getAttribute("username");
        String hotelId = request.getParameter("hotelId");
        Set<HotelReview> hotelReviewSet = DatabaseHandler.getInstance().getReviews(hotelId);

        Double averageRating = calcAvgRating(hotelReviewSet);
        JsonArray jsonArray = new JsonArray();
        hotelReviewSet.forEach(review -> jsonArray.add(review.toJson()));

        JsonObject json = new JsonObject();
        json.addProperty("averageRating", averageRating);
        json.addProperty("username", username);
        json.add("reviews", jsonArray);

        response.getWriter().println(json);

    }
}
