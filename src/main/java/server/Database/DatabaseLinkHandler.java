package server.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseLinkHandler {

    private String uri;
    private Properties config;

    public DatabaseLinkHandler(String uri, Properties config) {
        this.uri = uri;
        this.config = config;
    }

    public void addLinkToHistory(String username, String link) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            try {
                statement = connection.prepareStatement(PreparedStatements.INSERT_LINK);
                statement.setString(1, username);
                statement.setString(2, link);

                statement.executeUpdate();
                statement.close();
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
    }
}
