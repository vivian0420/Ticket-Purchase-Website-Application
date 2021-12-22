package cs601.project4.backend;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import cs601.project4.database.DBCPDataSource;
import cs601.project4.frontend.LoginPageHtml;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * LoginServlet. Display login page and validate the OAuth token that is returned from google. If the token is valid, allow
 * user to login and store user's information to the database.
 */
public class LoginServlet extends HttpServlet {

    /**
     * Display login page.
     *
     * @param req  Http request
     * @param resp Http response
     * @throws ServletException exception that a servlet can throw when it encounters difficulty
     * @throws IOException  exceptions produced by failed or interrupted I/O operations.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpStatus.OK_200);
        String content = LoginPageHtml.getLoginHtml();
        resp.getWriter().write(content);
    }

    /**
     * Use http client to send the request to google and validate the OAuth token.If the token is valid then redirect
     * the user to the home page and store the user information to the database.
     *
     * @param req  Http request
     * @param resp Http response
     * @throws ServletException exception that a servlet can throw when it encounters difficulty
     * @throws IOException  exceptions produced by failed or interrupted I/O operations.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!req.getParameterMap().containsKey("google_id_token")) {
            resp.sendError(404, "google_id_token not found.");
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest tokenRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://oauth2.googleapis.com/tokeninfo?id_token=" + req.getParameter("google_id_token")))
                .GET()
                .build();
        final HttpResponse<String> googleResponse;
        try {
            googleResponse = client.send(tokenRequest,HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException e) {
            resp.sendError(500, "Error sending https://oauth2.googleapis.com/tokeninfo?id_token=");
            return;
        }

        if(googleResponse.statusCode()!= 200) {
            resp.sendError(googleResponse.statusCode(), "Google returns: " + googleResponse.statusCode());
            return;
        }

        try(Connection conn = DBCPDataSource.getConnection()) {
            PreparedStatement providerQuery = conn.prepareStatement("select idp_id from identity_providers where name = 'Google'");
            final ResultSet providerRS = providerQuery.executeQuery();
            providerRS.next();
            int idp_id = providerRS.getInt("idp_id");

            final JsonObject json = new Gson().fromJson(googleResponse.body(), JsonObject.class);
            final PreparedStatement insertUsers = conn.prepareStatement("INSERT ignore into User(idp_id, sub, email, name, picture, given_name, family_name) VALUES(?,?,?,?,?,?,?)");
            insertUsers.setInt(1,idp_id);
            insertUsers.setString(2,json.get("sub").getAsString());
            insertUsers.setString(3,json.get("email").getAsString());
            insertUsers.setString(4,json.get("name").getAsString());
            insertUsers.setString(5, json.get("picture").getAsString());
            insertUsers.setString(6,json.get("given_name").getAsString());
            insertUsers.setString(7,json.get("family_name").getAsString());
            insertUsers.executeUpdate();

            final PreparedStatement userIdQuery = conn.prepareStatement("SELECT user_id FROM User WHERE sub=?");
            userIdQuery.setString(1, json.get("sub").getAsString());
            ResultSet userIdSet = userIdQuery.executeQuery();
            userIdSet.next();
            int userId = userIdSet.getInt("user_id");

            final String session = String.valueOf(UUID.randomUUID());
            final PreparedStatement insertSession = conn.prepareStatement("INSERT into User_session(user_id, session, expiration) VALUES(?,?,current_timestamp()+INTERVAL 15 DAY)");
            insertSession.setInt(1,userId);
            insertSession.setString(2,session);
            insertSession.executeUpdate();

            resp.setStatus(302);
            resp.setHeader("location", "/home");
            resp.setHeader("set-cookie", "session=" + session);
            resp.getWriter().write("<html>Redirect to home</htlm>");
        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }
    }
}
