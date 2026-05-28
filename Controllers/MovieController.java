package Controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class MovieController {

    private static final String API_KEY = "API_KEY"; // Ganti dengan API Key milikmu
    private static final String BASE_URL = "http://www.omdbapi.com/?apikey=" + API_KEY;

    public String searchMovie(String title) {
        StringBuilder result = new StringBuilder();
        try {
            String query = title.replace(" ", "+");
            URL url = new URI(BASE_URL + "&t=" + query).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            return result.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
