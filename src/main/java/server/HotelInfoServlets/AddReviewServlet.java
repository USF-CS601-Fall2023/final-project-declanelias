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


        HotelReview review = new HotelReview(
                Integer.parseInt(hotelId), title, text,
                username, date, Double.parseDouble(rating));

        DatabaseHandler.getInstance().addReview(review);
    }
}
