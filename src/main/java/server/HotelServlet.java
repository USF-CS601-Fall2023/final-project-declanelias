package server;

import HotelData.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class HotelServlet extends HttpServlet {

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

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        context.put("name", username);


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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();

        String username = (String) session.getAttribute("username");

        String title = request.getParameter("title");
        String text = request.getParameter("review");
        String date = LocalDate.now().toString();


        try {
            HotelReview review = new HotelReview(
                    Integer.parseInt(hotelId), title, text,
                    username, date, 5);

            System.out.println(review);

            reviews.addToIdMap(review);
            response.sendRedirect("/hotel?hotelId=" + hotelId);
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }

    }
}
