package cs601.project4.backend;

import cs601.project4.database.DBCPDataSource;
import cs601.project4.frontend.HomeHtml;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * AccountServlet, handle showing user account and allows users to modify user information
 */
public class AccountServlet extends HttpServlet {

    /**
     * Get user account information. Check user session and active to see if the user has been login or not,
     * if not, force the user to the login page. Otherwise, display the user information.
     *
     * @param req  Http request
     * @param resp Http response
     * @throws ServletException exception that a servlet can throw when it encounters difficulty
     * @throws IOException  exceptions produced by failed or interrupted I/O operations.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try (Connection conn = DBCPDataSource.getConnection()) {
            ResultSet userSet = LoginUtilities.getUserQuerySet(req, conn);
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

    /**
     * Allow users to modify user information. Read user's information from http request and update it to the user table(DB).
     * After finishing updating, redirect the user to the user account page.
     *
     * @param req Http request
     * @param resp Http response
     * @throws ServletException exception that a servlet can throw when it encounters difficulty
     * @throws IOException  exceptions produced by failed or interrupted I/O operations.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection conn = DBCPDataSource.getConnection()) {
            ResultSet userSet = LoginUtilities.getUserQuerySet(req, conn);
            if (!userSet.next()) {
                resp.setHeader("location", "/login");
                resp.setStatus(302);
                resp.getWriter().write("<html>302 Found</html>");
                return;
            }
            int userId = Integer.parseInt(userSet.getString("user_id"));
            String name = req.getParameter("name");
            String givenName = req.getParameter("given_name");
            String familyName = req.getParameter("family_name");
            PreparedStatement updateUser = conn.prepareStatement("UPDATE User SET name=?, given_name=?, family_name=? WHERE user_id=?");
            updateUser.setString(1, name);
            updateUser.setString(2, givenName);
            updateUser.setString(3, familyName);
            updateUser.setInt(4, userId);
            updateUser.executeUpdate();

            resp.setStatus(302);
            resp.setHeader("location", "/account");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Get content method, which select the user's information from database and return a table of user's information that
     * will be displayed on the UI page when user click "Account" button.
     *
     * @param userName the current user's user name
     * @param id the current user's id
     * @param conn a connection to the given database URL.
     * @return the content that will be shown on the UI page.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    private String getContent(String userName, int id, Connection conn) throws SQLException {
        String selectString = "SELECT u.user_id, u.name, u.email, u.given_name, u.family_name " +
                " FROM User u, User_session s WHERE u.user_id=?";
        PreparedStatement userQuery = conn.prepareStatement(selectString);
        userQuery.setInt(1, id);
        ResultSet userResult = userQuery.executeQuery();
        userResult.next();
        String htmlTable = "<table>";
        htmlTable += "<h3>My Account:</h3>";
        htmlTable += "<form action='/account' method='post' accept-charset='utf-8'><tr>" + "<td><b>" + "Name: " + "</b></td>" + "<td><input style='margin-left:10px;' type='text' value='" + userResult.getString("name") + "' name='name'/></td>" + "</tr>";
        htmlTable += "<tr>" + "<td><b>" + "Email: " + "</b></td>" + "<td><input style='margin-left:10px;' readonly='readonly' type='text' value='" + userResult.getString("email") + "' name='email'/></td>" + "</tr>";
        htmlTable += "<tr>" + "<td><b>" + "Given_name: " + "</b></td>" + "<td><input style='margin-left:10px;' type='text' value='" + userResult.getString("given_name") + "' name='given_name'/></td>" + "</tr>";
        htmlTable += "<tr>" + "<td><b>" + "Family_name: " + "</b></td>" + "<td><input style='margin-left:10px;' type='text' value='" + userResult.getString("family_name") + "' name='family_name'/></td>" + "</tr>";
        htmlTable += "<input type='hidden' name='userID' value='" + userResult.getInt("user_id") + "' />";
        htmlTable += "<tr><td><button id='update' type='submit' style='margin-top: 10px;' >Update</button></td></tr></form>";
        htmlTable += "</table>";

        String content = HomeHtml.getHomeHtml(userName, htmlTable);
        return content;
    }


}
