package server.Database;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;
import java.util.Properties;
import java.util.Random;

/**
 * Handles all registration in the database
 */
public class DatabaseRegistrationHandler {

    private String uri;
    private Properties config;
    private Random random;

    public DatabaseRegistrationHandler(String uri, Properties config, Random random) {
        this.uri = uri;
        this.config = config;
        this.random = random;
    }

    /**
     * Gets the salt for a specific user.
     *
     * @param connection - active database connection
     * @param user - which user to retrieve salt for
     * @return salt for the specified user or null if user does not exist
     * @throws SQLException if any issues with database connection
     */
    private String getSalt(Connection connection, String user) {
        String salt = null;
        try (PreparedStatement statement = connection.prepareStatement(PreparedStatements.SALT_SQL)) {
            statement.setString(1, user);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                salt = results.getString("usersalt");
                return salt;
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return salt;
    }

    /**
     * Check if username and password exist
     *
     * @param username
     * @param password
     * @return true if exists false if not
     */
    public boolean authenticateUser(String username, String password) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            //System.out.println("dbConnection successful");
            statement = connection.prepareStatement(PreparedStatements.AUTH_SQL);
            String usersalt = getSalt(connection, username);
            String passhash = getHash(password, usersalt);

            statement.setString(1, username);
            statement.setString(2, passhash);
            ResultSet results = statement.executeQuery();
            boolean flag = results.next();
            return flag;
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    /**
     * Registers a new user, placing the username, password hash, and
     * salt into the database.
     *
     * @param newuser - username of new user
     * @param newpass - password of new user
     */
    public void registerUser(String newuser, String newpass) {
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);

        String usersalt = encodeHex(saltBytes, 32); // salt
        String passhash = getHash(newpass, usersalt); // hashed password
        System.out.println(usersalt);

        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            try {
                statement = connection.prepareStatement(PreparedStatements.REGISTER_SQL);
                statement.setString(1, newuser);
                statement.setString(2, passhash);
                statement.setString(3, usersalt);
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

    /**
     * Returns the hex encoding of a byte array.
     *
     * @param bytes - byte array to encode
     * @param length - desired length of encoding
     * @return hex encoded byte array
     */
    private String encodeHex(byte[] bytes, int length) {
        BigInteger bigint = new BigInteger(1, bytes);
        String hex = String.format("%0" + length + "X", bigint);

        assert hex.length() == length;
        return hex;
    }

    /**
     * Calculates the hash of a password and salt using SHA-256.
     *
     * @param password - password to hash
     * @param salt - salt associated with user
     * @return hashed password
     */
    public String getHash(String password, String salt) {
        String salted = salt + password;
        String hashed = salted;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salted.getBytes());
            hashed = encodeHex(md.digest(), 64);
        }
        catch (Exception ex) {
            System.out.println(ex);
        }

        return hashed;
    }

    /**
     * Check if username exists in database
     *
     * @param username
     * @return true if it does, false if not
     */
    public boolean checkIfUsernameExists(String username) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(PreparedStatements.CHECK_USERNAME_SQL);
            statement.setString(1, username);

            ResultSet results = statement.executeQuery();
            boolean flag = results.next();
            return flag;
        } catch (SQLException e) {
            System.out.println(e);
        }

        return false;
    }

    /**
     * Get the last login of the user
     *
     * @param username
     * @return last date of the login
     */
    public String getLastLogin(String username) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(PreparedStatements.CHECK_LOGIN);
            statement.setString(1, username);

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return results.getString(1);
            }

            return null;
        } catch (SQLException e) {
            System.out.println(e);
        }

        return null;
    }

    private boolean checkLogin(String username) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(PreparedStatements.CHECK_LOGIN);
            statement.setString(1, username);
            ResultSet results = statement.executeQuery();
            return results.next();
        } catch (SQLException e) {
            System.out.println(e.getStackTrace());
        }

        return false;
    }

    /**
     * Adds the last time of the login to the database. If user had already logged in,
     * update the entry. If user hasnt logged in before, insert a new login
     * @param username
     * @param time
     */
    public void addLogin(String username, String time) {
        if (checkLogin(username)) {
            updateLogin(username, time);
        } else {
            insertLogin(username, time);
        }
    }

    private void updateLogin(String username, String time) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(PreparedStatements.UPDATE_LOGIN);
            statement.setString(1, time);
            statement.setString(2, username);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getStackTrace());
        }
    }

    private void insertLogin(String username, String time) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            statement = connection.prepareStatement(PreparedStatements.INSERT_LOGIN);
            statement.setString(1, username);
            statement.setString(2, time);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getStackTrace());
        }
    }
}
