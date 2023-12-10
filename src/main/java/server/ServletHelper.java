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

public class ServletHelper {

    private HttpServletResponse response;
    private HttpServletRequest request;
    private VelocityEngine ve;
    private VelocityContext context = new VelocityContext();
    private String username;
    private String loginMessage;
    private HttpSession session;

    public ServletHelper(HttpServletRequest request, HttpServletResponse response) {
        this.response = response;
        this.request = request;

        ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        session = request.getSession();
        addBaseContext();
    }

    private void addBaseContext() {
        username = (String) session.getAttribute("username");
        loginMessage = (String) session.getAttribute("loginMessage");
        context.put("username", username);
        context.put("loginMessage", loginMessage);
        setLoggedInStatus();
    }

    public void checkLoginStatus() throws IOException {
        if (username == null) {
            response.sendRedirect("/loginRequired");
        }
    }

    private void setLoggedInStatus() {
        if (username == null) {
            context.put("isLoggedIn", false);
        } else {
            context.put("isLoggedIn", true);
        }
    }

    public void addContext(String key, Object value) {
        context.put(key, value);
    }

    public void doGet(String htmlFile) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        Template template = ve.getTemplate(htmlFile);

        StringWriter writer = new StringWriter();
        template.merge(context, writer);

        PrintWriter out = response.getWriter();
        out.println(writer);
    }

    public String getUsername() {
        return username;
    }
}
