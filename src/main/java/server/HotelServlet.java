package server;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Interface for servlets that handle anything to do with hotels
 */
public interface HotelServlet {

    /**
     * Takes care of common code between hotel servlets
     *
     * @param request
     * @param response
     * @param htmlFile html file to be displayed
     * @param ve velocity engine
     * @param context context to get hotel data from
     * @throws IOException
     */
    default void doGetHelper(HttpServletRequest request, HttpServletResponse response, String htmlFile, VelocityEngine ve, VelocityContext context) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
//        String username = (String) session.getAttribute("username");

//        checkSession(username, response);

        Template template = ve.getTemplate(htmlFile);
//        context.put("name", username);

        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        out.println(writer);
    }

    private void checkSession(String username, HttpServletResponse response) throws IOException {
        if (username == null) {
            response.sendRedirect("/login");
        }
    }


}
