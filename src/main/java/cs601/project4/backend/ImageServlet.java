package cs601.project4.backend;

import cs601.project4.database.DBCPDataSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ImageServlet. Read image from file system and display.
 */
public class ImageServlet extends HttpServlet {
    /**
     * Read image from file system and display.
     *
     * @param req  Http request
     * @param resp Http response
     * @throws ServletException exception that a servlet can throw when it encounters difficulty
     * @throws IOException  exceptions produced by failed or interrupted I/O operations.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if(!req.getParameterMap().containsKey("image_name")) {
            resp.sendError(400, "image_name not in request");
            return;
        }

        try(Connection conn = DBCPDataSource.getConnection()) {
            ResultSet userSet = LoginUtilities.getUserQuerySet(req, conn);
            if (!userSet.next()) {
                resp.setHeader("location", "/login");
                resp.setStatus(302);
                resp.getWriter().write("<html>302 Found</html>");
                return;
            }
            File image = new File("images/" + req.getParameter("image_name"));
            if(!image.exists() || !image.isFile()) {
                resp.sendError(404, "Image " + req.getParameter("image_name") + " not found");
                return;
            }
            final byte[] bytes = new FileInputStream(image).readAllBytes();
            resp.setHeader("Content-Type: ", "image");
            resp.setHeader("Content-Length", String.valueOf(bytes.length));
            resp.getOutputStream().write(bytes);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
