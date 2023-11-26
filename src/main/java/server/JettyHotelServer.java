package server;

import HotelData.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.apache.velocity.app.VelocityEngine;

import java.util.Set;

public class JettyHotelServer {
	public static final int PORT = 8080;


	public static void main(String[] args) throws Exception {

		HotelData hotels = new ThreadSafeHotels();
		Reviews reviews = new ThreadSafeReviews(Set.of("a", "the", "is", "are", "were", "and"));
		loadHotelInfo(args, hotels, reviews);

		Server server = new Server(PORT);

		ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		handler.addServlet( "server.RegisterServlet", "/register");
		handler.addServlet("server.LoginServlet", "/login");
		handler.addServlet(HotelSearchServlet.class, "/hotelSearch");
		handler.addServlet(HotelServlet.class, "/hotel");


		// initialize Velocity
		VelocityEngine velocity = new VelocityEngine();
		velocity.init();

		handler.setAttribute("templateEngine", velocity);
		handler.setAttribute("hotels", hotels);
		handler.setAttribute("reviews", reviews);
		server.setHandler(handler);

		server.setHandler(handler);
		server.start();
		server.join();
	}


	private static void loadHotelInfo(String[] args, HotelData hotels, Reviews reviews) {
		CommandLineParser cp = new CommandLineParser();
		cp.parse(args, Set.of("hotels", "reviews", "threads"));

		String hotelPath = cp.getArg("hotels");
		String reviewPath = cp.getArg("reviews");
		String numThreads = cp.getArg("threads");

		// FILL IN CODE:
		// Unlike the servers, the drivers will be specific to the hotel/reviews project.

		// Load hotel and review data to ThreadSafeHotelData (or whatever you called such class(es) earlier)
		FileParser fp = new FileParser(Integer.parseInt(numThreads), true);
		fp.addHotels(hotelPath, hotels);
		fp.addReviews(reviewPath, reviews);
	}
}