package server.Database;

import HotelData.Hotel;

import java.sql.*;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

public class DatabaseHotelHandler {

    private String uri;
    private Properties config;

    public DatabaseHotelHandler(String uri, Properties config) {
        this.uri = uri;
        this.config = config;
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

    public Set<Hotel> getHotelSet(String param, String preparedStatement) {
        PreparedStatement sql;
        Set<Hotel> hotels = new TreeSet<>();

        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {


            sql = dbConnection.prepareStatement(preparedStatement);
            sql.setString(1, param);

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

    public void addFavorite(String username, String id) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            try {
                statement = connection.prepareStatement(PreparedStatements.INSERT_FAVORITE_HOTEL);
                statement.setString(1, username);
                statement.setString(2, id);

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