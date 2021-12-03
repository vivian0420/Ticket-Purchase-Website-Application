package cs601.project4;

import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ImageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!req.getParameterMap().containsKey("image_name")) {
            resp.sendError(400, "image_name not in request");
            return;
        }
        try(Connection conn = DriverManager.getConnection(getConnectionToken(req))) {
            File image = new File("images/" + req.getParameter("image_name"));
            if(!image.exists() || !image.isFile()) {
                resp.sendError(404, "Image " + req.getParameter("image_name") + " not found");
                return;
            }
            final byte[] bytes = new FileInputStream(image).readAllBytes();
            resp.setHeader("Content-Type: ", "image");
            resp.setHeader("Content-Lengrh", String.valueOf(bytes.length));
            resp.getOutputStream().write(bytes);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String getConnectionToken(HttpServletRequest req) {
        return ((JsonObject) req.getServletContext().getAttribute("config_key")).get("connection").getAsString();
    }
}
