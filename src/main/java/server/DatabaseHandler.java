package server;

import HotelData.*;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

/**
 *
 * Modified from the example of Prof. Engle
 */
public class DatabaseHandler {

    private static DatabaseHandler dbHandler = new DatabaseHandler("database.properties"); // singleton pattern
    private Properties config; // a "map" of properties
    private String uri = null; // uri to connect to mysql using jdbc
    private Random random = new Random(); // used in password  generation

    /**
     * DataBaseHandler is a singleton, we want to prevent other classes
     * from creating objects of this class using the constructor
     */
    private DatabaseHandler(String propertiesFile){
        this.config = loadConfigFile(propertiesFile);
        this.uri = "jdbc:mysql://"+ config.getProperty("hostname") + "/" + config.getProperty("username") + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        //System.out.println("uri = " + uri);
    }

    /**
     * Returns the instance of the database handler.
     * @return instance of the database handler
     */
    public static DatabaseHandler getInstance() {
        return dbHandler;
    }


    // Load info from config file database.properties
    public Properties loadConfigFile(String propertyFile) {
        Properties config = new Properties();
        try (FileReader fr = new FileReader(propertyFile)) {
            config.load(fr);
        }
        catch (IOException e) {
            System.out.println(e);
        }

        return config;
    }

    private void createTable(String preparedStatement) {
        Statement statement;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            statement = dbConnection.createStatement();
            statement.executeUpdate(preparedStatement);
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public void createUserTable() {
        createTable(PreparedStatements.CREATE_USER_TABLE);
    }

    public void createHotelTable() {
        createTable(PreparedStatements.CREATE_HOTEL_TABLE);
    }

    public void createReviewTable() {
        createTable(PreparedStatements.CREATE_REVIEW_TABLE);
    }



    /**
     * Returns the hex encoding of a byte array.
     *
     * @param bytes - byte array to encode
     * @param length - desired length of encoding
     * @return hex encoded byte array
     */
    public static String encodeHex(byte[] bytes, int length) {
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
    public static String getHash(String password, String salt) {
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
     * Registers a new user, placing the username, password hash, and
     * salt into the database.
     *
     * @param newuser - username of new user
     * @param newpass - password of new user
     */
    public void registerUser(String newuser, String newpass) {
        // Generate salt
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



    public void addHotel(Hotel hotel) {

        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            try {
                System.out.println("Added hotel " + hotel.getHotelName());
                statement = connection.prepareStatement(PreparedStatements.INSERT_HOTEL);
                statement.setString(1, hotel.getId());
                statement.setString(2, hotel.getHotelName());
                statement.setString(3, hotel.getAddress());
                statement.setDouble(4, hotel.getLat());
                statement.setDouble(5, hotel.getLng());
                statement.setString(6, hotel.getCity());
                statement.setString(7, hotel.getState());
                statement.setString(8, hotel.getCountry());
                statement.setString(9, hotel.getExpediaLink());

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

    public void addReview(HotelReview review) {

    }

    public static void main(String[] args) {
        DatabaseHandler dbhandler = DatabaseHandler.getInstance();
//        dbHandler.createUserTable();
        dbhandler.createHotelTable();
//        dbhandler.createReviewTable();
        loadHotelInfo(args);
    }

    private static void loadHotelInfo(String[] args) {
        ThreadSafeHotels hotels = new ThreadSafeHotels();
        ThreadSafeReviews reviews = new ThreadSafeReviews(Set.of("a", "the", "is", "are", "were", "and"));

        CommandLineParser cp = new CommandLineParser();
        cp.parse(args, Set.of("hotels", "reviews", "threads"));

        String hotelPath = cp.getArg("hotels");
        String reviewPath = cp.getArg("reviews");
        String numThreads = cp.getArg("threads");

        FileParser fp = new FileParser(Integer.parseInt(numThreads), true);
        fp.addHotels(hotelPath, hotels);
        fp.addReviews(reviewPath, reviews);

        hotels.addToDb();
//        reviews.addToDb();
    }

}

