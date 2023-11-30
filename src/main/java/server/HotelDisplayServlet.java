package server;

import HotelData.*;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.Set;

public class HotelDisplayServlet extends HttpServlet implements HotelServlet {

    VelocityEngine ve;
    VelocityContext context;
    Reviews reviews;
    String hotelId;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ve = (VelocityEngine) getServletContext().getAttribute("templateEngine");
        context = new VelocityContext();

        hotelId = request.getParameter("hotelId");
        reviews = (Reviews) getServletContext().getAttribute("reviews");
        Set<HotelReview> hotelReviewSet = reviews.searchByID(Integer.parseInt(hotelId));
        context.put("reviews", hotelReviewSet);
        context.put("avgRating", calcAvgRating(hotelReviewSet));

        HotelData hotelData = (HotelData) getServletContext().getAttribute("hotels");
        Hotel hotel = hotelData.searchByID(hotelId);
        context.put("hotel", hotel);

        doGetHelper(request, response, "templates/hotel.html", ve, context);
    }

    private double calcAvgRating(Set<HotelReview> reviews) {
        return reviews
                .stream()
                .mapToDouble(HotelReview::getAverageRating)
                .average()
                .orElse(0);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();

        String username = (String) session.getAttribute("username");

        String title = request.getParameter("title");
        String text = request.getParameter("review");
        String rating = request.getParameter("rating");
        String date = LocalDate.now().toString();


        HotelReview review = new HotelReview(
                Integer.parseInt(hotelId), title, text,
                username, date, Double.parseDouble(rating));


        reviews.addToIdMap(review);
        response.sendRedirect("/hotel?hotelId=" + hotelId);

    }
}
