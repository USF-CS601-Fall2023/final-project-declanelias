package server;

import HotelData.*;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.apache.velocity.app.VelocityEngine;

import java.util.Set;

/**
 * Main class that runs the jetty server
 */
public class JettyHotelServer {
	public static final int PORT = 8080;

	private static HotelData hotels;
	private static Reviews reviews;
	private static VelocityEngine velocity;
	private static HandlerList handlers;

	public static void main(String[] args) throws Exception {
		loadHotelInfo(args);
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
		handler.addServlet(LoginServlet.class, "/*");

		handler.setAttribute("templateEngine", velocity);
		handler.setAttribute("hotels", hotels);
		handler.setAttribute("reviews", reviews);

		handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resourceHandler, handler });
	}


	private static void loadHotelInfo(String[] args) {
		hotels = new ThreadSafeHotels();
		reviews = new ThreadSafeReviews(Set.of("a", "the", "is", "are", "were", "and"));

		CommandLineParser cp = new CommandLineParser();
		cp.parse(args, Set.of("hotels", "reviews", "threads"));

		String hotelPath = cp.getArg("hotels");
		String reviewPath = cp.getArg("reviews");
		String numThreads = cp.getArg("threads");

		FileParser fp = new FileParser(Integer.parseInt(numThreads), true);
		fp.addHotels(hotelPath, hotels);
		fp.addReviews(reviewPath, reviews);
	}
}