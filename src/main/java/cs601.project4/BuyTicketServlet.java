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
import java.util.UUID;

import static cs601.project4.Application.CONNECTION_STRING;

public class BuyTicketServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int eventId = Integer.parseInt(req.getParameter("event_id"));
        String session = "";
        for (Cookie cookie : req.getCookies()) {
            if (cookie.getName().equals("session")) {
                session = cookie.getValue();
            }
        }

        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING)) {

            PreparedStatement userQuery = conn.prepareStatement("SELECT u.name FROM User u, User_session s WHERE session=? AND u.user_id=s.user_id and s.active = 1 and expiration > current_timestamp()");
            userQuery.setString(1, session);
            ResultSet userSet = userQuery.executeQuery();
            if (!userSet.next()) {
                resp.setHeader("location", "/login");
                resp.setStatus(302);
                resp.getWriter().write("<html>302 Found</html>");
                return;
            }
            String userName = userSet.getString("name");
            String buyTicket = getContent(eventId, conn);
            resp.getWriter().write(HomeHtml.getHomeHtml(userName,buyTicket));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String session = "";
        for (Cookie cookie : req.getCookies()) {
            if (cookie.getName().equals("session")) {
                session = cookie.getValue();
            }
        }
        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING)) {
            PreparedStatement userQuery = conn.prepareStatement("SELECT user_id FROM User_session WHERE session=?");
            userQuery.setString(1, session);
            ResultSet userSet = userQuery.executeQuery();
            userSet.next();
            int userId = userSet.getInt("user_id");
            int eventId = Integer.parseInt(req.getParameter("eventID"));
            int numberOfTickets = Integer.parseInt(req.getParameter("numberOfTickets"));
            PreparedStatement priceQuery = conn.prepareStatement("SELECT price FROM Events WHERE event_id=?");
            priceQuery.setInt(1,eventId);
            ResultSet priceSet = priceQuery.executeQuery();
            priceSet.next();
            double price = priceSet.getDouble("price");
            for(int i = 0; i < numberOfTickets; i++){
                PreparedStatement insertTicket = conn.prepareStatement("INSERT into ticket(event_id, user_id, price,ticket_code," +
                        " purchased_on) VALUES(?,?,?,?,current_timestamp())");
                insertTicket.setInt(1,eventId);
                insertTicket.setInt(2,userId);
                insertTicket.setDouble(3,price);
                insertTicket.setString(4, String.valueOf(UUID.randomUUID()));
                insertTicket.executeUpdate();
            }
            resp.setStatus(302);
            resp.setHeader("location", "/allEvents");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private String getContent(int id, Connection conn) throws SQLException {
        PreparedStatement eventQuery = conn.prepareStatement("SELECT event_id, eventname, capacity, start_time, end_time," +
                " price, concat(address1, ' ',  address2, ' ', city, ' ', state, ' ', zipcode) as address, description,image_name FROM Events WHERE event_id=?");
        eventQuery.setInt(1,id);
        ResultSet eventSet = eventQuery.executeQuery();
        eventSet.next();

        PreparedStatement countSoldQuery = conn.prepareStatement("SELECT count(1) as sold FROM ticket WHERE event_id=?");
        countSoldQuery.setInt(1, id);
        ResultSet soldSet = countSoldQuery.executeQuery();
        soldSet.next();

        String htmlTable = "<h2>Purchase tickets:</h2><table><tr><td><table>";
        htmlTable += "";
        htmlTable += "<form action='/buyTicket' method='post' accept-charset='utf-8' onsubmit='return window.confirm(\"Confirm your order?\");'><tr>" + "<td><b>" + "Event Name: " + "</b></td>" + "<td>" + eventSet.getString("eventname") + "</td>" +
                " </tr>";
        htmlTable += "<tr>" + "<td><b>" + "Start Time: " + "</b></td>" + "<td>" + eventSet.getString("start_time") + "</td>" + "</tr>";
        htmlTable += "<tr>" + "<td><b>" + "End Time: " + "</b></td>" + "<td>" + eventSet.getString("end_time") + "</td>" + "</tr>";
        htmlTable += "<tr>" + "<td><b>" + "Capacity: " + "</b></td>" + "<td>" + eventSet.getInt("capacity") + "</td>" + "</tr>";
        htmlTable += "<tr>" + "<td><b>" + "Sold: " + "</b></td>" + "<td>" + soldSet.getInt("sold") + "</td>" + "</tr>";
        htmlTable += "<tr>" + "<td><b>" + "Price: " + "</b></td>" + "<td>" + eventSet.getDouble("price") + "</td>" + "</tr>";
        htmlTable += "<tr>" + "<td><b>" + "Address: " + "</b></td>" + "<td>" + eventSet.getString("address") + "</td>" + "</tr>";
        htmlTable += "<tr>" + "<td><b>" + "Description: " + "</b></td>" + "<td style='white-space:pre-line; word-wrap:break-word;'>" + eventSet.getString("description") + "</td>" + "</tr>";
        htmlTable += "<tr>" + "<td><b>" + "Number of Tickets: " + "</b></td>" + "<td><input style='margin-left:10px;' type='number' name='numberOfTickets'" +
                " max=" + (eventSet.getInt("capacity") - soldSet.getInt("sold")) + "></td>" + "</tr>";
        htmlTable += "<input type='hidden' name='eventID' value='" + eventSet.getInt("event_id") + "' />";
        htmlTable += "<tr><td><input id='confirm' type='submit' style='margin-top: 10px;' value='Confirm'></td></tr></form>";
        htmlTable += "</table></td>";
        htmlTable += "<td><image  width='450px' src='/images?image_name=" + eventSet.getString("image_name") +"'/></td>";
        htmlTable += "</tr></table>";
        return htmlTable;
    }
}
