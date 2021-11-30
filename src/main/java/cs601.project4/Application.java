package cs601.project4;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.MultipartConfigElement;


public class Application {
    final public static String CONNECTION_STRING = "jdbc:mysql://localhost:3306/project4?user=root&password=2281997163";
    public static void main(String[] args)throws Exception {
        Server server = new Server(8888);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(LoginServlet.class, "/login");
        context.addServlet(HomeServlet.class, "/home");
        context.addServlet(CreateEventServlet.class, "/createEvent")
                .getRegistration().setMultipartConfig(new MultipartConfigElement("images"));
        context.addServlet(ImageServlet.class, "/images");
        context.addServlet(MyEventsServlet.class, "/myEvents");
        context.addServlet(AllEventsServlet.class, "/allEvents");
        context.addServlet(BuyTicketServlet.class, "/buyTicket");
        context.addServlet(Transaction.class, "/transaction");
        context.addServlet(UpdateServlet.class, "/update")
                .getRegistration().setMultipartConfig(new MultipartConfigElement("images"));
        context.addServlet(AccountServlet.class, "/account");
        context.addServlet(LogoutServlet.class, "/logout");
        server.setHandler(context);
        server.start();

    }
}
