package cs601.project4;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class Application {
    public static void main(String[] args)throws Exception {
        Server server = new Server(8888);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(LoginServlet.class, "/login");
        context.addServlet(HomeServlet.class, "/home");
        context.addServlet(CreateEventServlet.class, "/createEvent");
        server.setHandler(context);
        server.start();

    }
}
