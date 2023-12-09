package server.Database;

public class PreparedStatements {
    /** Prepared Statements  */
    /** For creating the users table */
    public static final String CREATE_USER_TABLE =
            "CREATE TABLE users (" +
                    "userid INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(32) NOT NULL UNIQUE, " +
                    "password CHAR(64) NOT NULL, " +
                    "usersalt CHAR(32) NOT NULL);";

    public static final String CREATE_LINK_HISTORY_TABLE =
            "CREATE TABLE linkhistory (" +
                    "username VARCHAR(32) NOT NULL, " +
                    "expediaLink VARCHAR(255) NOT NULL, " +
                    "time VARCHAR(255) NOT NULL);";

    public static final String CREATE_REVIEW_TABLE =
            "CREATE TABLE reviews (" +
                    "hotelId VARCHAR(10) NOT NULL, " +
                    "reviewId VARCHAR(25) NOT NULL, " +
                    "averageRating DOUBLE NOT NULL, " +
                    "title VARCHAR(255) NOT NULL, " +
                    "reviewText TEXT NOT NULL, " +
                    "userNickname VARCHAR(255) NOT NULL, " +
                    "submissionDate VARCHAR(255) NOT NULL);";

    public static final String CREATE_FAVORITE_TABLE =
            "CREATE TABLE favorites (" +
                    "username VARCHAR(255) NOT NULL, " +
                    "hotelId VARCHAR(10) NOT NULL," +
                    "CONSTRAINT unique_username_hotelId " +
                    "UNIQUE (username, hotelId));";

    public static final String CREATE_HOTEL_TABLE =
            "CREATE TABLE hotels (" +
                    "hotelId VARCHAR(10) PRIMARY KEY, " +
                    "address VARCHAR(255) NOT NULL, " +
                    "hotelName VARCHAR(255) NOT NULL, " +
                    "lat DOUBLE NOT NULL, " +
                    "lng DOUBLE NOT NULL, " +
                    "city VARCHAR(255) NOT NULL, " +
                    "state VARCHAR(255) NOT NULL, " +
                    "country VARCHAR(255) NOT NULL, " +
                    "expediaLink VARCHAR(255) NOT NULL);";

    public static final String INSERT_FAVORITE_HOTEL =
            "INSERT INTO favorites " +
                    "(username, hotelId) " +
                    "VALUES (?, ?)";

    public static final String INSERT_LINK =
            "INSERT INTO linkhistory " +
                    "(username, expediaLink, time) " +
                    "VALUES (?, ?, ?)";

    public static final String INSERT_HOTEL =
            "INSERT INTO hotels " +
                    "(hotelId, address, hotelName, lat, lng, city, state, country, expediaLink) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

    public static final String INSERT_REVIEW =
            "INSERT INTO reviews " +
                    "(hotelId, reviewId, averageRating, title, reviewText, userNickname, submissionDate)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?);";


    /** Used to insert a new user into the database. */
    public static final String REGISTER_SQL =
            "INSERT INTO users (username, password, usersalt) " +
                    "VALUES (?, ?, ?);";

    /** Used to retrieve the salt associated with a specific user. */
    public static final String SALT_SQL =
            "SELECT usersalt FROM users WHERE username = ?";

    /** Used to authenticate a user. */
    public static final String AUTH_SQL =
            "SELECT username FROM users " +
                    "WHERE username = ? AND password = ?";

    public static final String CHECK_USERNAME_SQL =
            "SELECT username FROM users " +
                    "WHERE username = ?";

    public static final String GET_HOTEL_FROM_ID =
            "SELECT * FROM hotels WHERE hotelId=(?)";

    public static final String GET_LINK_HISTORY =
            "SELECT * FROM linkhistory WHERE username=(?)";

    public static final String GET_REVIEWS_FROM_ID =
            "SELECT * FROM reviews WHERE hotelId=(?)";

    public static final String GET_HOTELS_WITH_KEYWORD =
            "SELECT * FROM hotels " +
                    "WHERE hotelName LIKE CONCAT('%', ?, '%')";

    public static final String GET_FAVORITE_HOTELS =
            "SELECT " +
                    "hotelId, " +
                    "address," +
                    "hotelName, " +
                    "lat, " +
                    "lng, " +
                    "city, " +
                    "state, " +
                    "country, " +
                    "expediaLink " +
                    "FROM favorites " +
                    "NATURAL JOIN hotels " +
                    "WHERE username=?";

    public static final String UPDATE_REVIEW =
            "UPDATE reviews " +
                    "SET title = (?), reviewText = (?) " +
                    "WHERE hotelId = (?) AND reviewId = (?);";

    public static final String DELETE_REVIEW =
            "DELETE FROM reviews " +
                    "WHERE hotelId = (?) AND reviewId = (?) AND userNickname = (?);";

    public static final String REMOVE_FAVORITE_HOTEL =
            "DELETE FROM favorites " +
                    "WHERE hotelId = (?) " +
                    "AND username = (?);";

    public static final String CLEAR_HISTORY =
            "DELETE FROM linkhistory " +
                    "WHERE username = (?);";
}
