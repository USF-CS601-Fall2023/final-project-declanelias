package server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.apache.velocity.app.VelocityEngine;

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
		handler.addServlet(LoginServlet.class, "/*");

		handler.setAttribute("templateEngine", velocity);

		handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { resourceHandler, handler });
	}
}