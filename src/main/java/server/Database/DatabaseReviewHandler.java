package server.Database;

import HotelData.HotelReview;

import java.sql.*;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

public class DatabaseReviewHandler {

    private String uri;
    private Properties config;

    public DatabaseReviewHandler(String uri, Properties config) {
        this.uri = uri;
        this.config = config;
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
}
