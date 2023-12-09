package HotelData;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * Stores information of hotel read in from json
 */
public final class Hotel implements JsonInterface, Comparable<Hotel>{

    @SerializedName("f")
    private String hotelName;
    @SerializedName("ad")
    private String address;
    private String id;
    private Coordinates ll;
    @SerializedName("ci")
    private String city;
    @SerializedName("pr")
    private String state;
    @SerializedName("c")
    private String country;

    private String expediaLink;

    public Hotel() {

    }

    public String getId() {
        return id;
    }

    public String getHotelName() {
        return hotelName;
    }

    @Override
    public String toString() {
        return  hotelName + ": " + id + '\n' +
                address + "\n" + city + ", " + state;
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", true);
        jsonObject.addProperty("hotelId", id);
        jsonObject.addProperty("name", hotelName);
        jsonObject.addProperty("addr", address);
        jsonObject.addProperty("city", city);
        jsonObject.addProperty("state", state);
        jsonObject.addProperty("lat", ll.getLat());
        jsonObject.addProperty("lng", ll.getLng());
        return jsonObject;
    }

    public String getExpediaLink() {
        return "http://expedia.com/" +
                city.replace(" ", "-") + "-" +
                hotelName.replace(" ", "-") + ".h" +
                id + ".Hotel-Information";

    }

    public String getAddress() {
        return address + ", " + city + ", " + state;
    }

    public double getLat() {
        return Double.parseDouble(ll.getLat());
    }

    public double getLng() {
        return Double.parseDouble(ll.getLng());
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCoords(String lat, String lng) {
        this.ll = new Coordinates(lat, lng);
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setExpediaLink(String expediaLink) {
        this.expediaLink = expediaLink;
    }

    @Override
    public int compareTo(Hotel o) {
        return -this.hotelName.compareTo(o.hotelName);
    }
}
