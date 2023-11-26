package HotelData;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * This class reads in data from the json files
 */
public class FileParser {
    private final ExecutorService poolManager;
//    private final Logger logger = LogManager.getLogger();
    private final Phaser phaser = new Phaser();
    private final boolean noOutput;


    public FileParser(int numThreads, boolean noOutput) {
        poolManager = Executors.newFixedThreadPool(numThreads);
        this.noOutput = noOutput;
    }

    /**
     * Adds reviews concurrently to a Reviews object (ideally a ThreadSafeReviews object)
     * @param reviewDir path to the direcory containing reviews
     * @param reviews Reviews object containing the maps
     */
    public void addReviews(String reviewDir, Reviews reviews) {
//        logger.debug("adding reviews");
        if (reviewDir == null) {
            return;
        }

        parseDirectory(Paths.get(reviewDir), reviews);

        phaser.awaitAdvance(phaser.getPhase());
        poolManager.shutdown();
        try {
            poolManager.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
//        logger.debug("done");
    }

    /**
     * Adds hotels to the Hotels object sequentially
     * @param hotelFile file containing hotel data
     * @param hotels Hotels object containing hotels map
     */
    public void addHotels(String hotelFile, HotelData hotels) {
        List<Hotel> hotelList = parseHotelFiles(hotelFile);
        for (Hotel hotel : hotelList) {
            hotels.addToMap(hotel);
        }
    }

    /**
     * parses the json files containing hotel info
     * @param filePath filePath of hotel jsons
     * @return list of hotel objects
     */
    private List<Hotel> parseHotelFiles(String filePath) {
        JsonArray jsonArr = getJsonArray(filePath, new String[]{"sr"});
        Type hotelReviewsType = new TypeToken<ArrayList<Hotel>>() {}.getType();
        Gson gson = new Gson();
        return gson.fromJson(jsonArr, hotelReviewsType);
    }

    /**
     * does the work to add reviews to the maps. If path is not a directory,
     * pass the path to parseReviewFile, if it is a directory create a new worker
     * for the directory
     */
    private void parseDirectory(Path p, Reviews reviews) {
        try (DirectoryStream<Path> pathsInDir = Files.newDirectoryStream(p)) {
            for (Path path : pathsInDir) {
                if (!Files.isDirectory(path) && (path.toString().endsWith(".json"))) {
                    parseReviewFile(path, reviews);
                } else if (Files.isDirectory(path)) {
//                    logger.debug("working on path " + path);
                    parseDirectory(path, reviews);
                }
            }
        } catch (IOException e) {
            System.out.println("Can not open directory: " + p);
        }
    }

    /**
     * Parses a file containing multiple reviews by creating an anonymous class and
     * adds the runnable to the poolManager
     * @param path path to file containing reviews
     */
    private void parseReviewFile(Path path, Reviews reviews) {
        Runnable task = () -> {
            String filePath = path.toString();
            JsonArray jsonArr = getJsonArray(filePath, new String[]{"reviewDetails", "reviewCollection", "review"});
            Type hotelReviewsType = new TypeToken<ArrayList<HotelReview>>() {
            }.getType();
            Gson gson = new Gson();
            List<HotelReview> reviewList = gson.fromJson(jsonArr, hotelReviewsType);
//            logger.debug("Working on review path " + path);
            for (HotelReview hr : reviewList) {
                reviews.addToIdMap(hr);
                if (noOutput) {
                    reviews.addToWordMap(hr);
                }
            }
            phaser.arriveAndDeregister();
        };

        poolManager.submit(task);
        phaser.register();
    }



    /**
     * General method to read in from json
     * @param filePath file to be read
     * @param jsonObjectNames list of objects contained within the file.
     *                        Each step of the array represents an object nested within the step before
     * @return JsonArray containing the json objects read in from filePath
     */
    private JsonArray getJsonArray(String filePath, String[] jsonObjectNames) {
        JsonArray jsonArr = null;

        try (FileReader fr = new FileReader(filePath)) {
            JsonParser parser = new JsonParser();
            JsonObject jo = (JsonObject) parser.parse(fr);

            jsonArr = navigateJsonObject(jo, jsonObjectNames);

            return jsonArr;
        } catch (IOException e) {
            System.err.println("Could not find the file: " + e);
            System.exit(1);
        }

        return jsonArr;
    }

    /**
     * Helper for getJsonArray. Given a JsonObject and a string of jsonObjectNames, it returns a JsonArray
     * @param jo jsonObject
     * @param jsonObjectNames see getJsonArray
     * @return JsonArray
     */
    private JsonArray navigateJsonObject(JsonObject jo, String[] jsonObjectNames) {
        int n = jsonObjectNames.length;
        if (n > 1) {
            String[] subArray = Arrays.copyOfRange(jsonObjectNames, 0, n - 1);
            for (String objectName : subArray) {
                jo = jo.getAsJsonObject(objectName);
            }
        }
        return jo.getAsJsonArray(jsonObjectNames[n - 1]);
    }
}
