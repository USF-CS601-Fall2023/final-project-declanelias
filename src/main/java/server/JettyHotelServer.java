package server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class JettyHotelServer {
	public static final int PORT = 8080;
	private Server jettyServer;
	private ServletContextHandler handler;


	public JettyHotelServer() {
		jettyServer = new Server(PORT);
	}


	public void start() throws Exception {
		jettyServer.start();
		jettyServer.join();
	}


	public void addMapping(String path, String className){
		// FILL IN CODE: call the method on the handler that adds the mapping between the path and the servlet
		handler.addServlet(className, path);
	}


	public static void main(String[] args)  {
		// FILL IN CODE, and add more classes as needed

		JettyHotelServer server = new JettyHotelServer();

		server.addMapping("/register", "RegisterServlet");

		try {
			server.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}


	}
}