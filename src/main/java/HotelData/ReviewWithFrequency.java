package HotelData;


import com.google.gson.JsonObject;

/**
 * This class contains a HotelReview object along with the associated frequency
 * for a given word in the HotelReviewSearcher class
 */
public class ReviewWithFrequency implements Comparable<ReviewWithFrequency>, JsonInterface{

    private final HotelReview hotelReview;
    private int frequency;

    /**
     * Constructor
     * @param hotelReview hotelReview object
     * @param frequency amount of times the review contained a word
     */
    public ReviewWithFrequency(HotelReview hotelReview, int frequency) {
        this.hotelReview = hotelReview;
        this.frequency = frequency;
    }

    /**
     * Compares two ReviewWithFrequency objects
     *
     * @param o the object to be compared.
     * @return < 0 if this object has a smaller frequency, > 0 with bigger frequency.
     *         if frequencies are equal, returns the object with the most recent date
     */
    @Override
    public int compareTo(ReviewWithFrequency o) {
        int compare = Integer.compare(frequency, o.frequency);
        if (compare == 0) {
            return hotelReview.compareTo(o.hotelReview);
        }
        return -compare;
    }

    @Override
    public String toString() {
        return hotelReview.toString() + "\n" +
                frequency;
    }

    @Override
    public JsonObject toJson() {
        return hotelReview.toJson();
    }
}
