package HotelData;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class constructs the data structure containing information about the hotels
 */
public class HotelData {

    private List<Hotel> hotels;
    private TreeMap<String, Hotel> hotelMap;

    /**
     * Constructor method
     * @param hotels list of hotel objects
     */
    public HotelData(List<Hotel> hotels) {
        this.hotels = hotels;
        hotelMap = new TreeMap<>(String::compareTo);
    }

    /**
     * Constructs the hotelMap Map which contains the hotelId as the key and the hotel as the value
     */
    public void constructMap() {
        for (Hotel hotel : hotels) {
            addToMap(hotel);
        }
    }

    /**
     * Adds individual hotel info to map
     * @param hotel hotel object
     */
    public void addToMap(Hotel hotel) {
        hotelMap.putIfAbsent(hotel.getId(), hotel);
    }


    /**
     * Searches for a hotel given an id
     * @param id hotelId
     * @return hotel object if id is valid, null if not
     */
    public Hotel searchByID(String id) {
        if (!hotelMap.containsKey(id)) {
            return null;
        }
        return hotelMap.get(id);
    }

    public Map<String, Hotel> getHotelMap() {
        return Collections.unmodifiableMap(hotelMap);
    }
}
