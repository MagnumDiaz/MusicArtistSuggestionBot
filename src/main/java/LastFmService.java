
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;




public class LastFmService {
    private static final String API_KEY = "89168224debf3d7f08544a514034e584";

    public static List<String> getSimilarArtists(String artistName) {
        String url = "https://ws.audioscrobbler.com/2.0/?method=artist.getsimilar&artist="
                + URLEncoder.encode(artistName, StandardCharsets.UTF_8)
                + "&api_key=" + API_KEY + "&format=json";

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());
            JsonNode artistArray = root.path("similarartists").path("artist");

            List<String> similarArtists = new ArrayList<>();
            for (int i = 0; i < Math.min(10, artistArray.size()); i++) {
                similarArtists.add(artistArray.get(i).path("name").asText());
            }

            return similarArtists;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
