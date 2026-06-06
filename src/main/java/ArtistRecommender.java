import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArtistRecommender {
    public static String getRecommendation(List<String> artists) {
        Map<String, Integer> counts = new HashMap<>();

        for (String artist : artists) {
            List<String> similar = LastFmService.getSimilarArtists(artist);
            similar.removeAll(artists);
            for (String name : similar) {
                counts.put(name, counts.getOrDefault(name, 0) + 1);
            }
        }

        return counts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("No match found");
    }
}
