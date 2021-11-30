package cs601.project4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import static cs601.project4.Application.CONNECTION_STRING;

public class UpdateServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int eventID = Integer.parseInt(req.getParameter("eventID"));
        String session = "";
        for(Cookie cookie: req.getCookies()) {
            if(cookie.getName().equals("session")) {
                session = cookie.getValue();
            }
        }
        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING)){
            PreparedStatement userQuery = conn.prepareStatement("SELECT u.name FROM User_session s, User u WHERE u.user_id = s.user_id and s.session=? and s.active = 1 and expiration > current_timestamp()");
            userQuery.setString(1, session);
            ResultSet userSet = userQuery.executeQuery();
            if(!userSet.next()) {
                resp.setHeader("location", "/login");
                resp.setStatus(302);
                resp.getWriter().write("<html>302 Found</html>");
                return;
            }
            String userName = userSet.getString("name");
            String content = getContent(userName, eventID, conn);
            resp.getWriter().write(content);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING)) {
            LOGGER.info(req.getParameterMap().toString());
            if (req.getParameter("formAction").equals("UPDATE")) {
                int event_id = Integer.parseInt(req.getParameter("eventID"));
                String eventname = req.getParameter("eventname");
                int capacity = Integer.parseInt(req.getParameter("capacity"));
                double price = Double.parseDouble(req.getParameter("price"));
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                df.setTimeZone(TimeZone.getTimeZone(req.getParameter("timezone")));
                final Date startTime;
                try {
                    startTime = df.parse(req.getParameter("start_time"));
                } catch (ParseException e) {
                    e.printStackTrace();
                    resp.sendError(400, "start_time invalid");
                    return;
                }
                final Date endTime;
                try {
                    endTime = df.parse(req.getParameter("end_time"));
                } catch (ParseException e) {
                    e.printStackTrace();
                    resp.sendError(400, "end_time invalid");
                    return;
                }
                String address1 = req.getParameter("address1");
                String address2 = req.getParameter("address2");
                String city = req.getParameter("city");
                String state = req.getParameter("state");
                String zipcode = req.getParameter("zipcode");
                String description = req.getParameter("description");

                PreparedStatement imageNameQuery = conn.prepareStatement("SELECT image_name FROM Events WHERE event_id=?");
                imageNameQuery.setInt(1,event_id);
                ResultSet imageNameResult = imageNameQuery.executeQuery();
                imageNameResult.next();
                String userImage = req.getPart("image").getSubmittedFileName();
                String imageName = null;
                if(!userImage.isBlank() && !userImage.isEmpty()) {
                    imageName = UUID.randomUUID().toString();
                    try(FileOutputStream image = new FileOutputStream(new File("images/"+imageName))) {
                        image.write(req.getPart("image").getInputStream().readAllBytes());
                        new File("images/" + imageNameResult.getString("image_name")).delete();
                    }
                }
                else if(userImage.isBlank() || userImage.isEmpty()) {
                    imageName = imageNameResult.getString("image_name");
                }


                PreparedStatement insertQuery = conn.prepareStatement("UPDATE Events SET eventname=?, createdate=current_timestamp(), " +
                        " address1=?, address2=?, city=?, state=?, zipcode=?, capacity=?, price=?, description=?, start_time=?, end_time=?, " +
                        " image_name=? WHERE event_id=? ");
                insertQuery.setString(1, eventname);
                insertQuery.setString(2, address1);
                insertQuery.setString(3, address2);
                insertQuery.setString(4, city);
                insertQuery.setString(5, state);
                insertQuery.setString(6, zipcode);
                insertQuery.setInt(7, capacity);
                insertQuery.setDouble(8, price);
                insertQuery.setString(9, description);
                insertQuery.setTimestamp(10, new Timestamp(startTime.getTime()));
                insertQuery.setTimestamp(11, new Timestamp(endTime.getTime()));
                insertQuery.setString(12, imageName);
                insertQuery.setInt(13, event_id);

                insertQuery.executeUpdate();

                resp.setStatus(302);
                resp.setHeader("location", "/myEvents");

            } else if (req.getParameter("formAction").equals("DELETE")) {
                int eventId = Integer.parseInt(req.getParameter("eventID"));
                PreparedStatement delete = conn.prepareStatement("delete from Events where event_id=?");
                delete.setInt(1, eventId);
                delete.execute();
                resp.setStatus(302);
                resp.setHeader("location", "/myEvents");

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


        private String getContent(String userName, int id, Connection conn) throws SQLException {
        PreparedStatement eventQuery = conn.prepareStatement("SELECT event_id, eventname, capacity, price, start_time," +
                " end_time, address1, address2, city, state, zipcode, description, image_name FROM Events WHERE event_id=?");
        eventQuery.setInt(1,id);
        ResultSet eventResultSet = eventQuery.executeQuery();
        eventResultSet.next();
        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String htmlItem = "<table><tr><td><table>";
        htmlItem += "<form action='/update' method='post' enctype='multipart/form-data' accept-charset='utf-8'><tr>" + "<td>" + "Name: " + "</td>" + "<td><input type='text' value='" + eventResultSet.getString("eventname") + "' name='eventname'/></td>" + "</tr>";
        htmlItem += "<tr>" + "<td>" + "Capacity: " + "</td>" + "<td><input type='text' value='" + eventResultSet.getString("capacity") + "' name='capacity'/></td>" + "</tr>";
        htmlItem += "<tr>" + "<td>" + "Price: " + "</td>" + "<td><input type='text' value='" + eventResultSet.getDouble("price") + "' name='price'/></td>" + "</tr>";
        htmlItem += "<tr>" + "<td>" + "Start_time: " + "</td>" + "<td><input type='datetime-local' value='" + df.format(eventResultSet.getTimestamp("start_time")) + "' name='start_time'/></td>" + "</tr>";
        htmlItem += "<tr>" + "<td>" + "End_time: " + "</td>" + "<td><input type='datetime-local' value='" + df.format(eventResultSet.getTimestamp("end_time")) + "' name='end_time'/></td>" + "</tr>";
        htmlItem += "<tr>" + "<td>" + "Address1: " + "</td>" + "<td><input type='text' value='" + eventResultSet.getString("address1") + "' name='address1'/></td>" + "</tr>";
        htmlItem += "<tr>" + "<td>" + "Address2: " + "</td>" + "<td><input type='text' value='" + eventResultSet.getString("address2") + "' name='address2'/></td>" + "</tr>";
        htmlItem += "<tr>" + "<td>" + "City: " + "</td>" + "<td><input type='text' value='" + eventResultSet.getString("city") + "' name='city'/></td>" + "</tr>";
        htmlItem += "<tr>" + "<td>" + "State: " + "</td>" + "<td><input type='text' value='" + eventResultSet.getString("state") + "' name='state'/></td>" + "</tr>";
        htmlItem += "<tr>" + "<td>" + "Zipcode: " + "</td>" + "<td><input type='text' value='" + eventResultSet.getString("zipcode") + "' name='zipcode'/></td>" + "</tr>";
        htmlItem += "<tr>" + "<td>" + "Description: " + "</td>" + "<td><textarea name='description' rows='4' cols='28'>" + eventResultSet.getString("description") + "</textarea></td>" + "</tr>";
        htmlItem += "<tr>" + "<td><label for='image'>Image:</label></td>" + "<td><input id='image' type='file' name='image' accept='image/*'/></td></tr>";
        htmlItem += "<tr></tr>";
        htmlItem += "<input type='hidden' name='eventID' value='" + eventResultSet.getInt("event_id") + "' />";
        htmlItem += "<input type='hidden' name='formAction' id='formAction' value='UPDATE'/>";
        htmlItem += "<input type='hidden' name='timezone' id='timezone' />";
        htmlItem += "<tr><td><button id='update' type='submit' onclick='form_update()'>Update</button></td>";
        htmlItem += "<td><button id='delete' type='submit' onclick='form_delete()'>Delete</button></td></tr></form>";
        htmlItem += "</table></td><td><image  width='450px' src='/images?image_name=" + eventResultSet.getString("image_name") + "'/></td>";
        htmlItem += "</table>";

        String content = HomeHtml.getHomeHtml(userName, htmlItem);
        return content;
    }
}
