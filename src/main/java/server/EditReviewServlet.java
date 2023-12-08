package server;

import server.Database.DatabaseHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * sets the review to what the user changed the title and text to
 */
public class EditReviewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("/login");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reviewId = request.getParameter("reviewId");
        String hotelId= request.getParameter("hotelId");
        String text = request.getParameter("editedReview");
        String title = request.getParameter("editedTitle");

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (username.isEmpty()) {
            response.sendRedirect("/login");
        }

        DatabaseHandler
                .getInstance()
                .updateReview(hotelId, reviewId, title, text);
    }
}
