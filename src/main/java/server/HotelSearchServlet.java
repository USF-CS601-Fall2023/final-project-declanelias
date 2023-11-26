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

public class HotelSearchServlet extends HttpServlet {

    VelocityEngine ve;
    VelocityContext context;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        ve = (VelocityEngine) getServletContext().getAttribute("templateEngine");
        context = new VelocityContext();

        Template template = ve.getTemplate("templates/hotelSearch.html");
        context.put("name", username);

        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        out.println(writer);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();

        String word = request.getParameter("word");
        HotelData hotels = (HotelData) getServletContext().getAttribute("hotels");

        Template template = ve.getTemplate("templates/hotelInfo.html");
        context.put("hotels", hotels.searchByWord(word));
        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        out.println(writer);
    }
}
