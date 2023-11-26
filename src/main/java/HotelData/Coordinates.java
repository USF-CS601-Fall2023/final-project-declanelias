package HotelData;

/**
 * This class contains the coordinates for a Hotel read in from the json
 */
public class Coordinates {

    private String lat;
    private String lng;

    @Override
    public String toString() {
        return "Coordinates{" +
                "lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                '}';
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }
}
