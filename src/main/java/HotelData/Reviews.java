package HotelData;

import java.util.*;

/**
 * This class constructs the data structure to hold the reviews and performs the search
 */
public class Reviews {
    private final Map<Integer, TreeSet<HotelReview>> idMap = new HashMap<>();
    private final Map<String, TreeSet<ReviewWithFrequency>> wordMap = new HashMap<>();

    private final Set<String> stopWords;
    private final List<HotelReview> hotelReviews;

    /**
     * Constructor
     * @param hotelReviews list of hotelReview objects
     */
    public Reviews(List<HotelReview> hotelReviews, Set<String> stopWords) {
        this.stopWords = stopWords;
        this.hotelReviews = hotelReviews;
    }

    /**
     * Adds hotel review to IdMap
     */
    public void addToIdMap(HotelReview hr) {
        int hotelId = hr.getHotelId();
        idMap.putIfAbsent(hotelId, new TreeSet<>());
        Set<HotelReview> set = idMap.get(hotelId);
        set.add(hr);
    }

    /**
     * Removes review from id map given hotelId and string id
     * @param hotelId hotel id
     * @param reviewId review id
     */
    public void removeFromIdMap(String hotelId, String reviewId) {

        Set<HotelReview> set = idMap.get(Integer.parseInt(hotelId));
        Iterator<HotelReview> iter = set.iterator();
        while (iter.hasNext()) {
            HotelReview hr = iter.next();
            if (hr.getReviewId().equals(reviewId)) {
                set.remove(hr);
                System.out.println("removed: " + hr);
                break;
            }
        }
    }

    /**
     * Constructs the idMap map with hotelId as key and set of reviews as value and
     * constructs wordMap Map that holds a word as the value and a set of
     * hotelReviews as the key
     */
    public void constructMaps() {
        for (HotelReview hr : hotelReviews) {
            addToIdMap(hr);
            addToWordMap(hr);
        }
    }

    /**
     * Adds hotel review to wordMap
     */
    public void addToWordMap(HotelReview hr) {
        // get text of reviews and split into an array based on whitespace
        String reviewText = hr.getReviewText();
        String[] words = reviewText.split(" ");
        Map<String, Integer> wordFreq = getWordFrequencies(words, stopWords);

        for (String word : wordFreq.keySet()) {
            // loop through each word in a give review, and add it to the map if not present
            wordMap.putIfAbsent(word, new TreeSet<>());
            Set<ReviewWithFrequency> freqSet = wordMap.get(word);
            ReviewWithFrequency reviewWithFrequency = new ReviewWithFrequency(hr,
                    wordFreq.get(word));
            freqSet.add(reviewWithFrequency);
        }
    }

    /**
     * This string takes an array of words and counts the frequency of each word
     * @param words array containing single words
     * @return wordFreq, map containing word as key and frequency as value
     */
    private Map<String, Integer> getWordFrequencies(String[] words, Set<String> stopWords) {
        Map<String, Integer> wordFreq = new HashMap<>();

        for (String word : words) {
            word = word.toLowerCase();
            if (!stopWords.contains(word) && !(containsNumbers(word))) {
                wordFreq.putIfAbsent(word, 0);
                int freq = wordFreq.get(word);
                freq++;
                wordFreq.put(word, freq);
            }
        }

        return wordFreq;
    }

    /**
     * Checks if there is a number in a word
     * @param word word to be checked
     * @return boolean which tells if the word contains a number or not
     */
    private boolean containsNumbers(String word) {
        for (char c : word.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;

    }

    /**
     * Searches the idMap map for a given hoteId
     * @param id the hotelId
     * @return a set of hotel reviews corresponding to the given id
     */
    public Set<HotelReview> searchByID(int id) {
        if (!idMap.containsKey(id)) {
            return null;
        }
        return Collections.unmodifiableSet(idMap.get(id));
    }

    /**
     * Searches wordMap map for reviews containing the word
     * @param word word to look for
     * @return a set of hotel reviews containing the given word
     */
    public Set<ReviewWithFrequency> searchByWord(String word) {
        if (!wordMap.containsKey(word)) {
            return null;
        }
        return Collections.unmodifiableSet(wordMap.get(word));
    }
}
