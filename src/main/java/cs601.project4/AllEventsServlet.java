package cs601.project4;

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

import static cs601.project4.Application.CONNECTION_STRING;

public class AllEventsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String session = "";
        for (Cookie cookie : req.getCookies()) {
            if (cookie.getName().equals("session")) {
                session = cookie.getValue();
            }
        }
        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING)) {
            PreparedStatement userQuery = conn.prepareStatement("SELECT u.name, u.user_id FROM User u, User_session s WHERE session=? AND u.user_id=s.user_id and s.active = 1 and expiration > current_timestamp()");
            userQuery.setString(1, session);
            ResultSet userSet = userQuery.executeQuery();
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

    private String getContent(int id, Connection conn) throws SQLException {

        PreparedStatement eventsQuery = conn.prepareStatement("SELECT event_id, eventname, capacity, start_time, end_time," +
                " price, concat(address1, ' ',  address2, ' ', city, ' ', state, ' ', zipcode) as address, description FROM Events");
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
}
