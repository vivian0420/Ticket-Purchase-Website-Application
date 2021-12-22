package cs601.project4.database;

import cs601.project4.Config;
import cs601.project4.ReadConfig;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBCPDataSource {
    private static BasicDataSource ds = new BasicDataSource();
    static{
        Config config = ReadConfig.readConfig();
        if(config ==  null) {
            System.exit(1);
        }
        ds.setUrl("jdbc:mysql://localhost:3306/" + config.getDatabase());
        ds.setUsername(config.getUsername());
        ds.setPassword(config.getPassword());
        ds.setMinIdle(5);
        ds.setMinIdle(10);
    }
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
