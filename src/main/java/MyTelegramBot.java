import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class MyTelegramBot extends TelegramLongPollingBot {
    private final Map<Long, List<String>> userArtists = new HashMap<>();

    @Override
    public String getBotToken() {
        return System.getenv("BOT_TOKEN");
    }

    @Override
    public String getBotUsername() {
        return "SpotifySuggestionMakerbot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();

            // Handle /start command
            if (text.equals("/start")) {
                sendMessage(chatId, "Welcome! Please provide 3 artists, one at a time.");
                userArtists.putIfAbsent(chatId, new ArrayList<>());
                return;
            }

            List<String> artists = userArtists.get(chatId);
            // Ensure the list only collects 3 artists
            if (artists != null && artists.size() < 3) {
                artists.add(text);
                if (artists.size() == 3) {
                    String recommendation = ArtistRecommender.getRecommendation(artists);
                    sendMessage(chatId, "You might like: " + recommendation);
                    artists.clear();  // Clear the list after sending recommendation
                } else {
                    sendMessage(chatId, "Got it! Send me " + (3 - artists.size()) + " more.");
                }
            }
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
