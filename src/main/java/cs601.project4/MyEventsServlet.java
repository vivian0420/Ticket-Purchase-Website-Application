package cs601.project4;

import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
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
 * MyEventsServlet. Display all the events that the current user created.
 */
public class MyEventsServlet extends HttpServlet {

    /**
     * Display all the events that the current user created. Check user session and active to see if the user has been login or not,
     * if not, force the user to the login page. Otherwise, display all the events that the current user created.
     *
     * @param req  Http request
     * @param resp Http response
     * @throws ServletException exception that a servlet can throw when it encounters difficulty
     * @throws IOException  exceptions produced by failed or interrupted I/O operations.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String session = "";
        for(Cookie cookie: req.getCookies()) {
            if(cookie.getName().equals("session")) {
                session = cookie.getValue();
            }
        }
        try(Connection conn = DriverManager.getConnection(getConnectionToken(req))) {
            PreparedStatement userQuery = conn.prepareStatement("SELECT u.name, u.user_id FROM User u, User_session s WHERE session=? AND u.user_id=s.user_id and s.active = 1 and expiration > current_timestamp()");
            userQuery.setString(1, session);
            ResultSet userSet = userQuery.executeQuery();
            if(!userSet.next()) {
                resp.setHeader("location", "/login");
                resp.setStatus(302);
                resp.getWriter().write("<html>302 Found</html>");
                return;
            }
            String userName = userSet.getString("name");
            int userId = userSet.getInt("user_id");
            String myEvents = getContent(userId, conn);
            resp.getWriter().write(HomeHtml.getHomeHtml(userName, myEvents));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * Get content method, which select the information of all events that the current user created from database and
     * return a table of events' information that will be displayed on the UI page when the users click "My events" button.
     *
     * @param id the current user's id
     * @param conn a connection to the given database URL.
     * @return the content that will be shown on the UI page.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    private String getContent(int id, Connection conn) throws SQLException {
        PreparedStatement eventsQuery = conn.prepareStatement("SELECT e.event_id, e.eventName, e.start_time, e.end_time, e.capacity, e.price, e.description, e.address1, " +
                " e.address2, e.city, e.state, e.zipcode FROM Events e WHERE e.createbyuserid=? ");
        eventsQuery.setInt(1, id);
        ResultSet eventSet = eventsQuery.executeQuery();
        String htmlTable = "";
        if(!eventSet.isBeforeFirst()) {
            htmlTable = "<h2>You haven't created any event yet!</h2>";
            return htmlTable;
        }
        int count = 1;
        htmlTable = "<table  border='1' cellspacing='0' cellpadding='0'>";
        htmlTable += "<h2>My Events:</h2>";
        htmlTable += "<tr bgcolor='#DCDCDC'><td>" + "N0." + "</td><td>" + "Event Name " + "</td><td>" + "Start time " + "</td><td>" + "End time " +
                " </td><td>" + "Capacity " + "</td><td>" + "Price " + "</td><td>" + "Description " + "</td><td>" + "Address1 " + "</td><td>" + "Address2 " + "</td><td>" +
                " City " + "</td><td>" + "State " + "</td><td>" + "Zipcode " + "</td></tr>";

        while (eventSet.next()) {
            htmlTable += "<tr>";
            htmlTable += "<td>" + count++ + "</td>";
            htmlTable += "<td>" + "<a href=\"/update?eventID=" + eventSet.getInt("event_id") +
                    "\">" + eventSet.getString("eventName") + "</a></td>";
            htmlTable += "<td>" + eventSet.getTimestamp("start_time") + "</td>";
            htmlTable += "<td>" + eventSet.getTimestamp("end_time") + "</td>";
            htmlTable += "<td>" + eventSet.getInt("capacity") + "</td>";
            htmlTable += "<td>" + eventSet.getDouble("price") + "</td>";
            htmlTable += "<td>" + eventSet.getString("description") + "</td>";
            htmlTable += "<td>" + eventSet.getString("address1") + "</td>";
            htmlTable += "<td>" + eventSet.getString("address2") + "</td>";
            htmlTable += "<td>" + eventSet.getString("city") + "</td>";
            htmlTable += "<td>" + eventSet.getString("state") + "</td>";
            htmlTable += "<td>" + eventSet.getString("zipcode") + "</td>";
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
