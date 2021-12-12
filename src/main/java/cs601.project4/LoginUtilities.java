package cs601.project4;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginUtilities {
    /**
     * Helper method. Return user query set, which help application to check if the current user has login or not.
     * @param req Http request
     * @param conn connection
     * @return user query set
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public static ResultSet getUserQuerySet(HttpServletRequest req, Connection conn) throws SQLException {
        String session = "";
        if (req.getCookies() == null) {
            session = "invalid";
        } else {
            for (Cookie cookie : req.getCookies()) {
                if (cookie.getName().equals("session")) {
                    session = cookie.getValue();
                }
            }
        }
        PreparedStatement userQuery = conn.prepareStatement("SELECT u.name, u.user_id FROM User u, User_session s WHERE session=? AND u.user_id=s.user_id and s.active = 1 and expiration > current_timestamp()");
        userQuery.setString(1, session);
        return userQuery.executeQuery();

    }
}
