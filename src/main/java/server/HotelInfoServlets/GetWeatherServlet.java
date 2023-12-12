package server.HotelInfoServlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import server.HttpFetcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Gets weather information
 */
public class GetWeatherServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String lat = req.getParameter("lat");
        String lng = req.getParameter("lng");

        JsonObject weather = getCurrentWeather(Double.parseDouble(lat), Double.parseDouble(lng));
        resp.getWriter().println(weather);
    }

    private static JsonObject getCurrentWeather(double lat, double lng) {
        String host = "api.open-meteo.com";
        String path = "/v1/forecast?latitude=" + lat +
                "&longitude=" + lng + "&current_weather=true";

        String res;
        try {
            res = HttpFetcher.fetch(host, path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Gson gson = new Gson();
        JsonObject json = gson.fromJson(res, JsonObject.class);

        return json.get("current_weather").getAsJsonObject();
    }
}
