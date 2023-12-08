package server;

import HotelData.*;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;
import java.util.*;

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
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            try {
                statement = connection.prepareStatement(PreparedStatements.INSERT_REVIEW);
                statement.setString(1, Integer.toString(review.getHotelId()));
                statement.setString(2, review.getReviewId());
                statement.setDouble(3, review.getAverageRating());
                statement.setString(4, review.getTitle());
                statement.setString(5, review.getReviewText());
                statement.setString(6, review.getUserNickname());
                statement.setString(7, review.getSubmissionDate().toString());

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

    public Set<HotelReview> getReviews(String hotelId) {
        PreparedStatement sql;
        Set<HotelReview> reviews = new TreeSet<>();
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {


            sql = dbConnection.prepareStatement(PreparedStatements.GET_REVIEWS_FROM_ID);
            sql.setString(1, hotelId);

            ResultSet result = sql.executeQuery();
            while(result.next()) {
                String reviewId = result.getString(2);
                double averageRating = result.getDouble(3);
                String title = result.getString(4);
                String text = result.getString(5);
                String username = result.getString(6);
                String date = result.getString(7);

                HotelReview review = new HotelReview(Integer.parseInt(hotelId), title, text, username, date, averageRating, reviewId);
                reviews.add(review);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return reviews;
    }


    public Hotel getHotel(String hotelId){
        PreparedStatement sql;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            sql = dbConnection.prepareStatement(PreparedStatements.GET_HOTEL_FROM_ID);
            sql.setString(1, hotelId);

            ResultSet result = sql.executeQuery();
            result.next();

            return createHotel(result);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return null;

    }

    public Set<Hotel> getHotelsByKeyword(String word) {
        PreparedStatement sql;
        Set<Hotel> hotels = new TreeSet<>();

        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {


            sql = dbConnection.prepareStatement(PreparedStatements.GET_HOTELS_WITH_KEYWORD);
            sql.setString(1, word);

            ResultSet result = sql.executeQuery();
            while(result.next()) {
               hotels.add(createHotel(result));
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return hotels;
    }

    private Hotel createHotel(ResultSet result) throws SQLException {
        String hotelId = result.getString(1);
        String hotelName = result.getString(2);
        String address = result.getString(3);
        double lat = result.getDouble(4);
        double lng = result.getDouble(5);
        String city = result.getString(6);
        String state = result.getString(7);
        String country = result.getString(8);
        String link = result.getString(9);

        Hotel hotel = new Hotel();
        hotel.setId(hotelId);
        hotel.setHotelName(hotelName);
        hotel.setAddress(address);
        hotel.setCoords(Double.toString(lat), Double.toString(lng));
        hotel.setCity(city);
        hotel.setState(state);
        hotel.setCountry(country);
        hotel.setExpediaLink(link);

        return hotel;
    }


    public void updateReview(String hotelId, String reviewId, String title, String text) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            try {
                statement = connection.prepareStatement(PreparedStatements.UPDATE_REVIEW);
                statement.setString(1, title);
                statement.setString(2, text);
                statement.setString(3, hotelId);
                statement.setString(4, reviewId);


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

    public void deleteReview(String hotelId, String reviewId, String username) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            try {
                statement = connection.prepareStatement(PreparedStatements.DELETE_REVIEW);
                statement.setString(1, hotelId);
                statement.setString(2, reviewId);
                statement.setString(3, username);

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

    public void createLinkHistoryTable() {
        createTable(PreparedStatements.CREATE_LINK_HISTORY_TABLE);
    }


    public static void main(String[] args) throws SQLException {
//        DatabaseHandler dbhandler = DatabaseHandler.getInstance();
//        dbHandler.createUserTable();
//        dbhandler.createHotelTable();
//        dbhandler.createReviewTable();
        dbHandler.createLinkHistoryTable();

//        loadHotelInfo(args);

//        Set<Hotel> review = dbHandler.getHotelsByKeyword("");
//        System.out.println(review);
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
        reviews.addToDb();
    }

}

