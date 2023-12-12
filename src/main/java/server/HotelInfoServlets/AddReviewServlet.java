package server.HotelInfoServlets;

import HotelData.HotelReview;
import server.Database.DatabaseHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Handles adding review to the database
 */
public class AddReviewServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String username = (String) session.getAttribute("username");
        String hotelId = request.getParameter("hotelId");
        String title = request.getParameter("title");
        String text = request.getParameter("review");
        String rating = request.getParameter("rating");
        String date = LocalDate.now().toString();

        double ratingNum = Double.parseDouble(rating);
        if (ratingNum > 5) {
            ratingNum = 5;
        }

        HotelReview review = new HotelReview(
                Integer.parseInt(hotelId), title, text,
                username, date, ratingNum);

        DatabaseHandler.getInstance().addReview(review);
    }
}
