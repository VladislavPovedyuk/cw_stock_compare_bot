package ua.vlpo;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ua.vlpo.stock.StockStorage;

public class Main {

    public static void main(String[] args) {
        StockStorage.init();

        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new StockBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
