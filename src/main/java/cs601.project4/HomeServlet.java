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
 * HomeServlet. Display the home page(a bunch of links and three of the most popular events.
 */
public class HomeServlet extends HttpServlet {

    /**
     * Check user session and active to see if the user has been login or not. if not, force the user to the
     * login page. Otherwise, display the home page.
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
            PreparedStatement userQuery = conn.prepareStatement("SELECT name FROM User u, User_session s WHERE session=? AND u.user_id=s.user_id and s.active = 1 and expiration > current_timestamp()");
            userQuery.setString(1, session);
            ResultSet userSet = userQuery.executeQuery();
            if(!userSet.next()) {
                resp.setHeader("location", "/login");
                resp.setStatus(302);
                resp.getWriter().write("<html>302 Found</html>");
                return;
            }
            String userName = userSet.getString("name");

            PreparedStatement pictureQuery = conn.prepareStatement("SELECT sold.event_id, e.image_name, e.eventname FROM events e, (select event_id, count(ticket_id) as sold " +
                    " FROM ticket GROUP by event_id ORDER by count(ticket_id)) sold WHERE e.event_id = sold.event_id ORDER by sold.sold desc limit 3; ");
            ResultSet pictureSet = pictureQuery.executeQuery();
            pictureSet.next();
            String picture1 = pictureSet.getString("image_name");
            int eventId1 = pictureSet.getInt("event_id");
            pictureSet.next();
            String picture2 = pictureSet.getString("image_name");
            int eventId2 = pictureSet.getInt("event_id");
            pictureSet.next();
            String picture3 = pictureSet.getString("image_name");
            int eventId3 = pictureSet.getInt("event_id");
            String homePagePictures = MainPageHtml.getMainPageHtml(String.valueOf(eventId1),picture1,String.valueOf(eventId2),picture2,String.valueOf(eventId3),picture3);
            resp.getWriter().write(HomeHtml.getHomeHtml(userName, homePagePictures));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * @param req http request
     * @return a connection string(url) that allows the application to connect to the database
     */
    public String getConnectionToken(HttpServletRequest req) {
        return ((JsonObject) req.getServletContext().getAttribute("config_key")).get("connection").getAsString();
    }
}
