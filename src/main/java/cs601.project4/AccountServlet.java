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

public class AccountServlet extends HttpServlet {
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
            String myAccount = getContent(userName, userId, conn);
            resp.getWriter().write(myAccount);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING)) {
            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String givenName = req.getParameter("given_name");
            String familyName = req.getParameter("family_name");
            PreparedStatement updateUser = conn.prepareStatement("UPDATE User SET name=?, email=?, given_name=?, family_name=?");
            updateUser.setString(1,name);
            updateUser.setString(2,email);
            updateUser.setString(3,givenName);
            updateUser.setString(4,familyName);
            updateUser.executeUpdate();

            resp.setStatus(302);
            resp.setHeader("location", "/account");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }



    private String getContent(String userName, int id, Connection conn) throws SQLException {
        PreparedStatement userQuery = conn.prepareStatement("SELECT u.user_id, u.name, u.email, u.given_name, u.family_name " +
                " FROM User u, User_session s WHERE u.user_id=?");
        userQuery.setInt(1, id);
        ResultSet userResult = userQuery.executeQuery();
        userResult.next();
        String htmlTable = "<table>";
        htmlTable += "<h3>My Account:</h3>";
        htmlTable += "<form action='/account' method='post' accept-charset='utf-8'><tr>" + "<td><b>" + "Name: " + "</b></td>" + "<td><input style='margin-left:10px;' type='text' value='" + userResult.getString("name") + "' name='name'/></td>" + "</tr>";
        htmlTable += "<tr>" + "<td><b>" + "Email: " + "</b></td>" + "<td><input style='margin-left:10px;' type='text' value='" + userResult.getString("email") + "' name='email'/></td>" + "</tr>";
        htmlTable += "<tr>" + "<td><b>" + "Given_name: " + "</b></td>" + "<td><input style='margin-left:10px;' type='text' value='" + userResult.getString("given_name") + "' name='given_name'/></td>" + "</tr>";
        htmlTable += "<tr>" + "<td><b>" + "Family_name: " + "</b></td>" + "<td><input style='margin-left:10px;' type='text' value='" + userResult.getString("family_name") + "' name='family_name'/></td>" + "</tr>";
        htmlTable += "<input type='hidden' name='eventID' value='" + userResult.getInt("user_id") + "' />";
        htmlTable += "<input type='hidden' name='formAction' id='formAction' value='UPDATE'/>";
        htmlTable += "<tr><td><button id='update' type='submit' style='margin-top: 10px;' onclick='form_update()'>Update</button></td></tr></form>";
        htmlTable += "</table>";

        String content = HomeHtml.getHomeHtml(userName,htmlTable);
        return content;
    }

}
