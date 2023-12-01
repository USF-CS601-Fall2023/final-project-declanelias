package HotelData;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

/**
 * This class contains information of hotel reviews, read in from json
 */
public final class HotelReview implements Comparable<HotelReview>, JsonInterface{
    private int hotelId;
    private String reviewId;
    @SerializedName("ratingOverall")
    private double averageRating;
    private String title;
    private String reviewText;
    private String userNickname;
    @SerializedName("reviewSubmissionTime")
    private String submissionDate;

    public String getReviewText() {
        return reviewText;
    }

    private static int count = 0;


    public HotelReview(int hotelId, String title, String reviewText, String userNickname, String submissionDate, double averageRating) {
        this.hotelId = hotelId;
        this.title = title;
        this.reviewText = reviewText;
        this.userNickname = userNickname;
        this.submissionDate = submissionDate;
        this.averageRating = averageRating;

        reviewId = Integer.toString(count);
        count--;
    }

    @Override
    public String toString() {
        return "hotelId = " + hotelId + '\n' +
                "reviewId = " + reviewId + '\n' +
                "averageRating = " + averageRating + '\n' +
                "title = " + title + '\n' +
                "reviewText = " + reviewText + '\n' +
                "userNickname = " + userNickname + '\n' +
                "submissionDate = " + submissionDate;
    }

    /**
     * Returns submission date as a LocalDate object
     * @return submission date as a LocalDate object
     */
    public LocalDate getSubmissionDate() {
        String date = submissionDate.substring(0, 10);
        return LocalDate.parse(date);
    }

    public int getHotelId() {
        return hotelId;
    }

    /**
     * Returns the hotel with most recent date, if dates are equal, returns the hotel by reviewId
     * @param o the object to be compared.
     */
    @Override
    public int compareTo(HotelReview o) {

        int compare = this.getSubmissionDate().compareTo(o.getSubmissionDate());
        if (compare == 0) {
            return reviewId.compareTo(o.reviewId);
        } else {
            return -compare;
        }
    }

    public String getUserNickname() {
        if (userNickname.isEmpty()) {
            return "Anonymous";
        }
        return userNickname;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getTitle() {
        return title;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("reviewId", reviewId);
        jsonObject.addProperty("title", title);
        jsonObject.addProperty("user", getUserNickname());
        jsonObject.addProperty("reviewText", reviewText);
        jsonObject.addProperty("date", submissionDate);

        return jsonObject;
    }
}
