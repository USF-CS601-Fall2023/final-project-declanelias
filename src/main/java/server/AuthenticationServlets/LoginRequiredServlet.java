package server.AuthenticationServlets;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import server.HotelServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginRequiredServlet extends HttpServlet implements HotelServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VelocityEngine ve = (VelocityEngine) getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        doGetHelper(request, response, "templates/notLoggedIn.html", ve, context);
    }
}
