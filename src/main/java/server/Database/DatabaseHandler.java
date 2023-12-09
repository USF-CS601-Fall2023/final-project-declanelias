package server.Database;

import HotelData.*;

import java.io.FileReader;
import java.io.IOException;
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
    private DatabaseRegistrationHandler registrationHandler;
    private DatabaseHotelHandler hotelHandler;
    private DatabaseReviewHandler reviewHandler;
    private DatabaseLinkHandler linkHandler;

    /**
     * DataBaseHandler is a singleton, we want to prevent other classes
     * from creating objects of this class using the constructor
     */
    private DatabaseHandler(String propertiesFile){
        this.config = loadConfigFile(propertiesFile);
        this.uri = "jdbc:mysql://"+ config.getProperty("hostname") + "/" + config.getProperty("username") + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        //System.out.println("uri = " + uri);
        registrationHandler = new DatabaseRegistrationHandler(uri, config, random);
        hotelHandler = new DatabaseHotelHandler(uri, config);
        reviewHandler = new DatabaseReviewHandler(uri, config);
        linkHandler = new DatabaseLinkHandler(uri, config);
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

    public void createTable(String preparedStatement) {
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


    // Registration methods, check DatabaseRegistrationHandler for javadocs
    public void registerUser(String newuser, String newpass) {
        registrationHandler.registerUser(newuser, newpass);
    }

    public boolean checkIfUsernameExists(String username) {
        return registrationHandler.checkIfUsernameExists(username);
    }

    public boolean authenticateUser(String username, String password) {
        return registrationHandler.authenticateUser(username, password);
    }


    // Hotel methods, check DatabaseHotelHandler for javadocs
    public Hotel getHotel(String hotelId){
        return hotelHandler.getHotel(hotelId);
    }

    public Set<Hotel> getHotelsByKeyword(String word) {
        return hotelHandler.getHotelSet(word, PreparedStatements.GET_HOTELS_WITH_KEYWORD);
    }

    public void addHotel(Hotel hotel) {
        hotelHandler.addHotel(hotel);
    }

    public void addFavoriteHotel(String username, String id) {
        hotelHandler.addFavorite(username, id);
    }

    public Set<Hotel> getFavoriteHotels(String username) {
        return hotelHandler.getHotelSet(username, PreparedStatements.GET_FAVORITE_HOTELS);
    }

    // Review methods, check DatabaseReviewHandler for javadocs
    public void addReview(HotelReview review) {
        reviewHandler.addReview(review);
    }

    public Set<HotelReview> getReviews(String hotelId) {
        return reviewHandler.getReviews(hotelId);
    }

    public void updateReview(String hotelId, String reviewId, String title, String text) {
        reviewHandler.updateReview(hotelId, reviewId, title, text);
    }

    public void deleteReview(String hotelId, String reviewId, String username) {
        reviewHandler.deleteReview(hotelId, reviewId, username);
    }

    // Expedia link methods, check DatabaseLinkHandler for javadocs
    public void addLinkToHistory(String username, String link) {
        linkHandler.addLinkToHistory(username, link);
    }

    public Set<Link> getLinkHistory(String username) {
        return linkHandler.getLinkHistory(username);
    }

    public void clearLinkHistory(String username) {
        linkHandler.clearLinkHistory(username);
    }
}

