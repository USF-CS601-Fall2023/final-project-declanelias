package HotelData;

import java.util.*;

/**
 * This class constructs the data structure containing information about the hotels
 */
public class HotelData {

    private List<Hotel> hotels;
    private TreeMap<String, Hotel> hotelMap;
    private Map<String, Set<Hotel>> wordMap;

    /**
     * Constructor method
     * @param hotels list of hotel objects
     */
    public HotelData(List<Hotel> hotels) {
        this.hotels = hotels;
        hotelMap = new TreeMap<>(String::compareTo);
        wordMap = new HashMap<>();
        wordMap.put("", new HashSet<>());
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
     * Adds individual hotel info to map and add words of name to word map
     * @param hotel hotel object
     */
    public void addToMap(Hotel hotel) {
        hotelMap.putIfAbsent(hotel.getId(), hotel);

        String name = hotel.getHotelName();
        String[] wordsInName = name.split("\\W|-|,");

        for (String word : wordsInName) {
            word = word.toLowerCase();
            wordMap.putIfAbsent(word, new HashSet<>());
            Set<Hotel> idList = wordMap.get(word);
            idList.add(hotel);

            Set<Hotel> emptyWordSet = wordMap.get("");
            emptyWordSet.add(hotel);

        }
    }

    /**
     * Return list of hotelIds who's corresponding hotel name contains the word
     * @param word word to search
     * @return list of hotel ids or null
     */
    public Set<Hotel> searchByWord(String word) {
        return wordMap.getOrDefault(word, null);
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
}
