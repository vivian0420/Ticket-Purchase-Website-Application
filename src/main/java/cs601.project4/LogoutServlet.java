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
import java.sql.SQLException;

import static cs601.project4.Application.CONNECTION_STRING;

public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String session = "";
        for(Cookie cookie: req.getCookies()) {
            if(cookie.getName().equals("session")) {
                session = cookie.getValue();
            }
        }
        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING)){
            PreparedStatement updateActive = conn.prepareStatement("UPDATE User_session SET active=0 WHERE session=?");
            updateActive.setString(1,session);
            updateActive.executeUpdate();

            resp.setStatus(302);
            resp.setHeader("location", "/login");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }



}
