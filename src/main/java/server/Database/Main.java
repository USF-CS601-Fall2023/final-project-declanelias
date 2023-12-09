package server.Database;

import HotelData.FileParser;
import HotelData.ThreadSafeHotels;
import HotelData.ThreadSafeReviews;
import server.CommandLineParser;

import java.sql.SQLException;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws SQLException {

        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        dbHandler.createTable(PreparedStatements.CREATE_LINK_HISTORY_TABLE);
//        dbHandler.createTable(PreparedStatements.CREATE_HOTEL_TABLE);
//        dbHandler.createTable(PreparedStatements.CREATE_FAVORITE_TABLE);
//        dbHandler.createTable(PreparedStatements.CREATE_REVIEW_TABLE);
//        dbHandler.createTable(PreparedStatements.CREATE_USER_TABLE);


//        dbHandler.createUserTable();
//        dbhandler.createHotelTable();
//        dbhandler.createReviewTable();

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
