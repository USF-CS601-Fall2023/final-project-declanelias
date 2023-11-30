package server;

import HotelData.Reviews;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteReviewServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reviewId = request.getParameter("reviewId");
        String hotelId = request.getParameter("hotelId");

        System.out.println(reviewId);
        System.out.println(hotelId);

        Reviews reviews = (Reviews) getServletContext().getAttribute("reviews");
        reviews.removeFromIdMap(hotelId, reviewId);
        response.sendRedirect("/hotel?hotelId=" + hotelId);
    }



}
