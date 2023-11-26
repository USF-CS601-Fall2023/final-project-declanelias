package server;

import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class RegisterServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println();
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        out.printf("<html>%n%n");
        out.printf("<head><title>%s</title></head>%n", "Form");

        out.printf("<body>%n");
        // display HTML Form
        printForm(request, response);

        out.printf("%n</body>%n");
        out.printf("</html>%n");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);

        String username = request.getParameter("username");
        username = StringEscapeUtils.escapeHtml4(username);

        String password = request.getParameter("password");
        password = StringEscapeUtils.escapeHtml4(password);


        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        boolean usernameExists = dbHandler.checkIfUsernameExists(username);
        boolean validUsername = validateUsername(username);
        boolean validPassword = validatePassword(password);

        PrintWriter out = response.getWriter();

        if (usernameExists) {
            out.println("<script type=\"text/javascript\">");
            out.println("alert('username already exists');");
            out.println("submitForm()");
            out.println("</script>");
        } else if (!validUsername) {
            out.println("invalid username");
        } else if (!validPassword) {
            out.println("invalid password");
        } else {
            dbHandler.registerUser(username, password);

            out.println("Successfully registered the user " + username);
            // TODO redirect to login
            response.sendRedirect("/login");
        }
    }

    private boolean validateUsername(String username) {
        return username.matches("[a-zA-Z][a-zA-Z0-9.-_]{0,29}");
    }

    private boolean validatePassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d]).{8,}$");
    }

    private static void printForm(HttpServletRequest request, HttpServletResponse response) throws IOException {

        PrintWriter out = response.getWriter();

        out.printf("<form method=\"post\" action=\"%s\">%n", request.getServletPath());
        out.printf("Enter username:<br><input type=\"text\" name=\"username\"><br>");
        out.printf("Enter password:<br><input type=\"password\" name=\"password\"><br>");
        out.printf("<p><input type=\"submit\" value=\"Enter\"></p>\n%n");
        out.printf("</form>\n%n");
    }
}
