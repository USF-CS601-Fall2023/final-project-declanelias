package server.Database;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

public class DatabaseLinkHandler {

    private String uri;
    private Properties config;

    public DatabaseLinkHandler(String uri, Properties config) {
        this.uri = uri;
        this.config = config;
    }

    /**
     * Adds a link to link history table
     *
     * @param username
     * @param link
     */
    public void addLinkToHistory(String username, String link) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            try {
                statement = connection.prepareStatement(PreparedStatements.INSERT_LINK);
                statement.setString(1, username);
                statement.setString(2, link);
                statement.setString(3, LocalDateTime.now().toString());

                statement.executeUpdate();
                statement.close();
            }
            catch(SQLException e) {
                System.out.println(e.getStackTrace());
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Get the history given the username
     *
     * @param username
     * @return Set of links
     */
    public Set<Link> getLinkHistory(String username) {
        PreparedStatement sql;
        Set<Link> links = new TreeSet<>();
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {


            sql = dbConnection.prepareStatement(PreparedStatements.GET_LINK_HISTORY);
            sql.setString(1, username);

            ResultSet result = sql.executeQuery();
            while(result.next()) {
                String link = result.getString(2);
                String date = result.getString(3);

                links.add(new Link(link, LocalDateTime.parse(date)));
            }

        } catch (SQLException e) {
            System.err.println(e.getStackTrace());
        }

        return links;
    }

    /**
     * Removes history given a username
     *
     * @param username
     */
    public void clearLinkHistory(String username) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            try {
                statement = connection.prepareStatement(PreparedStatements.CLEAR_HISTORY);
                statement.setString(1, username);

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
