package server;

import HotelData.HotelData;
import HotelData.Reviews;
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

/**
 * Handles endpoint /hotelSearch which searches for a hotel given a keyword
 */
public class HotelSearchServlet extends HttpServlet implements HotelServlet{

    VelocityEngine ve;
    VelocityContext context;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ve = (VelocityEngine) getServletContext().getAttribute("templateEngine");
        context = new VelocityContext();

        doGetHelper(request, response, "templates/hotelSearch.html", ve, context);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String word = request.getParameter("word");
        word = word.toLowerCase();
        HotelData hotels = (HotelData) getServletContext().getAttribute("hotels");

        context.put("hotels", hotels.searchByWord(word));

        doGetHelper(request, response, "templates/hotelInfo.html", ve, context);
    }
}
