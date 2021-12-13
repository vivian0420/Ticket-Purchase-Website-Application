package cs601.project4;

import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * AllEventsServlet, display all events to users when users click "All events" button.
 */
public class AllEventsServlet extends HttpServlet {

    /**
     * Display all events. Check user session and active to see if the user has been login or not,
     * if not, force the user to the login page. Otherwise, display the information of all events.
     *
     * @param req  Http request
     * @param resp Http response
     * @throws ServletException exception that a servlet can throw when it encounters difficulty
     * @throws IOException  exceptions produced by failed or interrupted I/O operations.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try (Connection conn = DriverManager.getConnection(getConnectionToken(req))) {
            ResultSet userSet = LoginUtilities.getUserQuerySet(req, conn);
            if (!userSet.next()) {
                resp.setHeader("location", "/login");
                resp.setStatus(302);
                resp.getWriter().write("<html>302 Found</html>");
                return;
            }
            String userName = userSet.getString("name");
            int userId = userSet.getInt("user_id");
            String allEvents = getContent(userId,conn);
            resp.getWriter().write(HomeHtml.getHomeHtml(userName, allEvents));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Get content method, which select the information of all events from database and return a table of events' information that
     * will be displayed on the UI page when user click "All events" button.
     *
     * @param id the current user's id
     * @param conn a connection to the given database URL.
     * @return the content that will be shown on the UI page.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    private String getContent(int id, Connection conn) throws SQLException {

        PreparedStatement eventsQuery = conn.prepareStatement("SELECT event_id, eventname, capacity, start_time, end_time," +
                " price, concat(address1, ' ',  address2, ' ', city, ' ', state, ' ', zipcode) as address, description FROM Events WHERE end_time >= current_timestamp()");
        ResultSet eventSet = eventsQuery.executeQuery();
        int count = 1;
        String htmlTable = "<table  border='1' cellspacing='0' cellpadding='0'>";
        htmlTable += "<h2>All Events:</h2>";
        htmlTable += "<tr bgcolor='#DCDCDC'><td>" + "N0." + "</td><td>" + "Event Name " + "</td><td>" + "Start time " + "</td><td>" + "End time " +
                " </td><td>" + "Capacity " + "</td><td>" + "Sold " + "</td><td>" + "Price " + "</td><td>" + "Description " + "</td><td>" +
                " Address " + "</td><td>"  + "</td></tr>";
        while (eventSet.next()) {
            PreparedStatement countSoldQuery = conn.prepareStatement("SELECT count(1) as sold FROM ticket WHERE event_id=?");
            countSoldQuery.setInt(1, eventSet.getInt("event_id"));
            ResultSet soldSet = countSoldQuery.executeQuery();
            soldSet.next();
            htmlTable += "<tr>";
            htmlTable += "<td>" + count++ + "</td>";
            htmlTable += "<td>" + eventSet.getString("eventName") + "</td>";
            htmlTable += "<td>" + eventSet.getTimestamp("start_time") + "</td>";
            htmlTable += "<td>" + eventSet.getTimestamp("end_time") + "</td>";
            htmlTable += "<td>" + eventSet.getInt("capacity") + "</td>";
            htmlTable += "<td>" + soldSet.getInt("sold") + "</td>";
            htmlTable += "<td>" + eventSet.getDouble("price") + "</td>";
            htmlTable += "<td>" + eventSet.getString("description") + "</td>";
            htmlTable += "<td>" + eventSet.getString("address") + "</td>";
            htmlTable += "<td><a href='/buyTicket?event_id=" + eventSet.getString("event_id") + "'>Purchase</a></td>";
            htmlTable += "</tr>";
        }
        htmlTable += "</table>";
        return htmlTable;
    }

    /**
     * @param req http request
     * @return a connection string(url) that allows the application to connect to the database
     */
    public String getConnectionToken(HttpServletRequest req) {
        return ((JsonObject) req.getServletContext().getAttribute("config_key")).get("connection").getAsString();
    }
}
