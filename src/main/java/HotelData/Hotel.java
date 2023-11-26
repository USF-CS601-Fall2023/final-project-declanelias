package HotelData;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * Stores information of hotel read in from json
 */
public final class Hotel implements JsonInterface{

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

    public Hotel() {
    }

    public String getId() {
        return id;
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
}
