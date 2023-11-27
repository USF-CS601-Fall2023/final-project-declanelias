package server;

import HotelData.*;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Set;

public class HotelServlet extends HttpServlet {

    VelocityEngine ve;
    VelocityContext context;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ve = (VelocityEngine) getServletContext().getAttribute("templateEngine");
        context = new VelocityContext();

        String hotelId = request.getParameter("hotelId");
        Reviews reviews = (Reviews) getServletContext().getAttribute("reviews");
        Set<HotelReview> hotelReviewSet = reviews.searchByID(Integer.parseInt(hotelId));
        context.put("reviews", hotelReviewSet);
        context.put("avgRating", calcAvgRating(hotelReviewSet));

        HotelData hotelData = (HotelData) getServletContext().getAttribute("hotels");
        Hotel hotel = hotelData.searchByID(hotelId);
        context.put("hotel", hotel);


        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        Template template = ve.getTemplate("templates/hotel.html");
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        response.getWriter().println(writer);
    }

    private double calcAvgRating(Set<HotelReview> reviews) {
        return reviews
                .stream()
                .mapToDouble(HotelReview::getAverageRating)
                .average()
                .orElse(0);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("got");
    }
}
