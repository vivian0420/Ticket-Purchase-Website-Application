package cs601.project4;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.MultipartConfigElement;
import java.io.FileReader;


public class Application {

    public static void main(String[] args) {
        try {
            startup();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @throws Exception
     */
    public static void startup() throws Exception {
        Gson gson = new Gson();
        JsonObject config = gson.fromJson(new FileReader("config.json"), JsonObject.class);
        Server server = new Server(8888);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setAttribute("config_key", config);
        context.addServlet(LoginServlet.class, "/login");
        context.addServlet(HomeServlet.class, "/home");
        context.addServlet(CreateEventServlet.class, "/createEvent")
                .getRegistration().setMultipartConfig(new MultipartConfigElement("images"));
        context.addServlet(ImageServlet.class, "/images");
        context.addServlet(MyEventsServlet.class, "/myEvents");
        context.addServlet(AllEventsServlet.class, "/allEvents");
        context.addServlet(BuyTicketServlet.class, "/buyTicket");
        context.addServlet(TransactionServlet.class, "/transaction");
        context.addServlet(UpdateServlet.class, "/update")
                .getRegistration().setMultipartConfig(new MultipartConfigElement("images"));
        context.addServlet(AccountServlet.class, "/account");
        context.addServlet(LogoutServlet.class, "/logout");
        server.setHandler(context);
        server.start();
    }
}
