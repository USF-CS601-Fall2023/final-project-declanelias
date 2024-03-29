package server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.apache.velocity.app.VelocityEngine;
import server.AuthenticationServlets.LoginRequiredServlet;
import server.AuthenticationServlets.LoginServlet;
import server.AuthenticationServlets.LogoutServlet;
import server.AuthenticationServlets.RegisterServlet;
import server.DisplayServlets.DisplayLinkServlet;
import server.DisplayServlets.FavoriteHotelServlet;
import server.DisplayServlets.HotelDisplayServlet;
import server.DisplayServlets.HotelSearchServlet;
import server.HotelInfoServlets.*;

/**
 * Main class that runs the jetty server
 */
public class JettyHotelServer {
	public static final int PORT = 8080;

	private static VelocityEngine velocity;
	private static HandlerList handlers;

	public static void main(String[] args) throws Exception {
		initVelocity();
		createHandler();
		startServer();
	}

	private static void startServer() throws Exception {
		Server server = new Server(PORT);
		server.setHandler(handlers);
		server.start();
		server.join();
	}
	private static void initVelocity() {
		velocity = new VelocityEngine();
		velocity.init();
	}

	private static void createHandler() {
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(true);
		resourceHandler.setResourceBase("templates");

		ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		handler.addServlet(RegisterServlet.class, "/register");
		handler.addServlet(LoginServlet.class, "/login");
		handler.addServlet(HotelSearchServlet.class, "/hotelSearch");
		handler.addServlet(HotelDisplayServlet.class, "/hotel");
		handler.addServlet(LogoutServlet.class, "/logout");
		handler.addServlet(DeleteReviewServlet.class, "/deleteReview");
		handler.addServlet(EditReviewServlet.class, "/editReview");
		handler.addServlet(SaveLink.class, "/saveLink");
		handler.addServlet(GetWeatherServlet.class, "/getWeather");
		handler.addServlet(FavoriteHotelServlet.class, "/favorites");
		handler.addServlet(DisplayLinkServlet.class, "/history");
		handler.addServlet(AddReviewServlet.class, "/addReview");
		handler.addServlet(GetReviewsServlet.class, "/getReviews");
		handler.addServlet(HotelSearchServlet.class, "/home");
		handler.addServlet(LoginRequiredServlet.class, "/loginRequired");
		handler.addServlet(RemoveFavoriteServlet.class, "/removeFavorite");
		handler.addServlet(AddFavoriteServlet.class, "/addFavorite");

		handler.setAttribute("templateEngine", velocity);

		handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resourceHandler, handler });
	}
}