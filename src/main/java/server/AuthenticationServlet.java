package server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * Interface implemented by register and login servlets
 */
public interface AuthenticationServlet {

    /**
     * Takes care of the get request for both servlets since the code is the same
     *
     * @param request html request
     * @param response html response
     * @param htmlFile html template file
     * @param ve VelocityEngine to display the template
     * @throws IOException
     */
    default void doGetHelper(HttpServletRequest request, HttpServletResponse response, String htmlFile, VelocityEngine ve) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        if (session.getAttribute("username") != null) {
            response.sendRedirect("/hotelSearch");
        } else {
            VelocityContext context = new VelocityContext();

            Template template = ve.getTemplate(htmlFile);

            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            out.println(writer);
        }
    }

    /**
     * Returns a list of size 2 containing the username and password from the request
     *
     * @param request html request
     * @return list of size 2. First entry is username, second entry is password.
     * @throws IOException
     */
    default List<String> getUserNameAndPassword(HttpServletRequest request) throws IOException {
        String requestBody = request.getReader().readLine();
        JsonParser parser = new JsonParser();
        JsonObject json = (JsonObject) parser.parse(requestBody);

        String user = json.get("username").toString().replace("\"", "");
        String password = json.get("password").toString().replace("\"", "");
        user = StringEscapeUtils.escapeHtml4(user);
        password = StringEscapeUtils.escapeHtml4(password);

        return List.of(user, password);
    }

    /**
     * sends invalid json to javascript if a username or password is invalid
     *
     * @param message message to be displayed
     * @param out PrintWriter to display the message
     */
    default void sendInvalidJson(String message, PrintWriter out) {
        JsonObject obj = new JsonObject();
        obj.addProperty("success", false);
        obj.addProperty("message", message);
        out.println(obj);
    }

    /**
     * Sends valid json to javascript if username and password are valid
     *
     * @param out PrintWriter
     */
    default void sendValidJson(PrintWriter out) {
        JsonObject obj = new JsonObject();
        obj.addProperty("success", true);
        out.println(obj);
    }

}
