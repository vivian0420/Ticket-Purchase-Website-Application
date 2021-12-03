package cs601.project4;

import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");
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
            String myTickets = getContent(userId, req, conn);
            resp.getWriter().write(HomeHtml.getHomeHtml(userName, myTickets));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");
        String transferEmail = req.getParameter("email");
        int ticketId = Integer.parseInt(req.getParameter("ticketID"));
        try(Connection conn = DriverManager.getConnection(getConnectionToken(req))) {
            PreparedStatement transferIdQuery = conn.prepareStatement("SELECT user_id FROM User WHERE email=?");
            transferIdQuery.setString(1,transferEmail);
            ResultSet transferIdResult = transferIdQuery.executeQuery();
            if(!transferIdResult.next()) {
                resp.setStatus(302);
                resp.setHeader("location", "/transaction?error=" + URLEncoder.encode("Email " + transferEmail + " not found", StandardCharsets.UTF_8));
                return;
            }
            int transferId = transferIdResult.getInt("user_id");

            PreparedStatement transferUpdate = conn.prepareStatement("UPDATE ticket SET user_id=? WHERE ticket_id=?");
            transferUpdate.setInt(1,transferId);
            transferUpdate.setInt(2,ticketId);
            transferUpdate.executeUpdate();

            resp.setStatus(302);
            resp.setHeader("location", "/transaction");


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private String getContent(int id, HttpServletRequest req, Connection conn) throws SQLException {
        PreparedStatement ticketQuery = conn.prepareStatement("SELECT e.event_id, e.eventname, e.start_time, e.end_time, t.price, " +
                " t.ticket_id, t.ticket_code FROM Events e, ticket t WHERE t.user_id=? AND e.event_id=t.event_id");
        ticketQuery.setInt(1,id);
        ResultSet ticketSet = ticketQuery.executeQuery();
        int count = 1;
        String htmlTable = "<table  style='border-spacing: 13px;'>";
        htmlTable += "<h2>My Transaction:</h2>";
        if (req.getParameter("error") != null) {
            htmlTable += "<p style='background-color:tomato;'>" + req.getParameter("error") + "</p>";
        }
        htmlTable += "<tr style='text-align: center'><td><b>" + "N0." + "</b></td><td><b>" + "Event Name " + "</b></td><td><b>" + "Start time " + "</b></td><td><b>" + "End time " +
                " </b></td><td><b>" + "Price " + "</b></td><td><b>" + "Ticket code " + "</b></td><td>" + " " + "</td><td>" + "  " + "</td></tr>";
        while (ticketSet.next()) {
            htmlTable += "<form action='/myTickets' method='post' accept-charset='utf-8' onsubmit='return window.confirm(\"Confirm to transfer?\");'><tr>";
            htmlTable += "<td>" + count++ + "</td>";
            htmlTable += "<td>" + "<a href='/buyTicket?event_id=" + ticketSet.getInt("event_id") +
                    "'\">" + ticketSet.getString("eventName") + "</a></td>";
            htmlTable += "<td>" + ticketSet.getTimestamp("start_time") + "</td>";
            htmlTable += "<td>" + ticketSet.getTimestamp("end_time") + "</td>";
            htmlTable += "<td>" + ticketSet.getDouble("price") + "</td>";
            htmlTable += "<td>" + ticketSet.getString("ticket_code") + "</td>";
            htmlTable += "<td>" + "<input style='width: 200px;' id='email' type='text' placeholder='Enter email address to transfer' name='email'>" + "</td>";
            htmlTable += "<td><input type='hidden' name='ticketID' value='" + ticketSet.getInt("ticket_id") + "' /></td>";
            htmlTable += "<td><input id='transfer' type='submit' value='Transfer'></td></tr></form>";
        }
        htmlTable += "</table>";

        return htmlTable;
    }
    public String getConnectionToken(HttpServletRequest req) {
        return ((JsonObject) req.getServletContext().getAttribute("config_key")).get("connection").getAsString();
    }
}
