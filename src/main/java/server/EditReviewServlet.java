package server;

import HotelData.HotelReview;
import HotelData.Reviews;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class EditReviewServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reviewId = request.getParameter("reviewId");
        String hotelId= request.getParameter("hotelId");
        String text = request.getParameter("editedReview");
        String title = request.getParameter("editedTitle");

        Reviews reviews = (Reviews) getServletContext().getAttribute("reviews");
        reviews.editReview(hotelId, reviewId, text, title);



    }
}
