package server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.apache.velocity.app.VelocityEngine;

public class JettyHotelServer {
	public static final int PORT = 8080;


	public static void main(String[] args) throws Exception {
		Server server = new Server(PORT);

		ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		handler.addServlet( "server.RegisterServlet", "/register");
		handler.addServlet("server.LoginServlet", "/login");
		handler.addServlet(HotelSearchServlet.class, "/hotelSearch");


		// initialize Velocity
		VelocityEngine velocity = new VelocityEngine();
		velocity.init();

		// Set velocity as an attribute of the context so that we can access it
		// from servlets
		handler.setAttribute("templateEngine", velocity);
		server.setHandler(handler);

		server.setHandler(handler);
		server.start();
		server.join();
	}
}