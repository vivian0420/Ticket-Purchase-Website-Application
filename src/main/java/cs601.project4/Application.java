package cs601.project4;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;


public class Application {
    final public static String CONNECTION_STRING = "jdbc:mysql://localhost:3306/project4?user=root&password=2281997163";
    public static void main(String[] args)throws Exception {
        Server server = new Server(8888);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(LoginServlet.class, "/login");
        context.addServlet(HomeServlet.class, "/home");
        context.addServlet(CreateEventServlet.class, "/createEvent");
        context.addServlet(MyEventsServlet.class, "/myEvents");
        context.addServlet(AllEventsServlet.class, "/allEvents");
        context.addServlet(BuyTicketServlet.class, "/buyTicket");
        context.addServlet(MyTicketServlet.class, "/myTickets");
        context.addServlet(UpdateServlet.class, "/update");
        context.addServlet(AccountServlet.class, "/account");
        server.setHandler(context);
        server.start();

    }
}
