package cs601.project4;

import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import java.util.UUID;

/**
 * CreateEventServlet.Allows users to create new events and insert the information of the new events into the database.
 */
public class CreateEventServlet extends HttpServlet {

    /**
     * Allow users to create new events. When users click "Confirm" button, read the information of the event from http request and update it to the Events table(DB).
     * After finishing insertion, redirect the user to the myEvents page.
     *
     * @param req Http request
     * @param resp Http response
     * @throws ServletException exception that a servlet can throw when it encounters difficulty
     * @throws IOException  exceptions produced by failed or interrupted I/O operations.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String eventName = req.getParameter("eventName");
        String address1 = req.getParameter("add1");
        String address2 = req.getParameter("add2");
        String city = req.getParameter("city");
        String state = req.getParameter("state");
        String zipcode = req.getParameter("zipcode");
        int capacity = Integer.parseInt(req.getParameter("capacity"));
        double price = Double.parseDouble(req.getParameter("price"));
        String description = req.getParameter("description");

        try(Connection conn = DBCPDataSource.getConnection()) {
            ResultSet userSet = LoginUtilities.getUserQuerySet(req, conn);
            if (!userSet.next()) {
                resp.setHeader("location", "/login");
                resp.setStatus(302);
                resp.getWriter().write("<html>302 Found</html>");
                return;
            }
            int userId = userSet.getInt("user_id");

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            final Date startTime;
            try {
                startTime = df.parse(req.getParameter("starttime"));
            } catch (ParseException e) {
                e.printStackTrace();
                resp.sendError(400, "start_time invalid");
                return;
            }
            final Date endTime;
            try {
                endTime = df.parse(req.getParameter("endtime"));
            } catch (ParseException e) {
                e.printStackTrace();
                resp.sendError(400, "end_time invalid");
                return;
            }


            PreparedStatement insertQuery = conn.prepareStatement("INSERT into Events(eventname, createbyuserid, createdate, " +
                    " address1, address2, city, state, zipcode, capacity, price, description, start_time, end_time, image_name) VALUES " +
                    " (?,?,current_timestamp(),?,?,?,?,?,?,?,?,?,?,?) ");
            insertQuery.setString(1, eventName);
            insertQuery.setInt(2, userId);
            insertQuery.setString(3, address1);
            insertQuery.setString(4, address2);
            insertQuery.setString(5, city);
            insertQuery.setString(6, state);
            insertQuery.setString(7, zipcode);
            insertQuery.setInt(8, capacity);
            insertQuery.setDouble(9, price);
            insertQuery.setString(10, description);
            insertQuery.setTimestamp(11, new Timestamp(startTime.getTime()));
            insertQuery.setTimestamp(12, new Timestamp(endTime.getTime()));

            final String userImage = req.getPart("image").getSubmittedFileName();
            if(!userImage.isBlank() && !userImage.isEmpty()) {
                final String imageName = UUID.randomUUID().toString();
                insertQuery.setString(13,imageName);
                try(FileOutputStream image = new FileOutputStream("images/"+imageName)){
                    image.write(req.getPart("image").getInputStream().readAllBytes());
                }
            }
            else {
                insertQuery.setString(13,null);
            }
            insertQuery.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        resp.setStatus(302);
        resp.setHeader("location", "/myEvents");
    }
}
