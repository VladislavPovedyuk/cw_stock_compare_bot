package ua.vlpo.stock;

import com.google.gson.reflect.TypeToken;
import org.telegram.telegrambots.api.objects.User;
import ua.vlpo.files.FileManager;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class StockStorage {

    public static final String mainsFileName = "stockBotMains";
    private static final Type mainsFileType = new TypeToken<HashMap<Integer, UserMain>>(){}.getType();

    public static final String stocksFileName = "stockBotStocks";
    private static final Type stocksFileType = new TypeToken<HashMap<Integer, UserStock>>(){}.getType();

    public static HashMap<Integer, UserMain> userMains;
    public static HashMap<Integer, UserStock> userStocks;

    public static void init() {
        userMains = FileManager.openMap(mainsFileType, mainsFileName);
        if (userMains == null) {
            userMains = new HashMap<Integer, UserMain>();
        }
        userStocks = FileManager.openMap(stocksFileType, stocksFileName);
        if (userStocks == null) {
            userStocks = new HashMap<Integer, UserStock>();
        }
    }

    public static void saveMains() {
        FileManager.saveMap(userMains, mainsFileName);
    }

    public static void saveStocks() {
        FileManager.saveMap(userStocks, stocksFileName);
    }

    public static int findIdByUsername(String username) {
        for (Map.Entry<Integer, UserStock> stockEntry : userStocks.entrySet()) {
            if (stockEntry.getValue().getUser() != null)
                if (stockEntry.getValue().getUser().getUserName() != null)
                    if (stockEntry.getValue().getUser().getUserName().toLowerCase().contentEquals(username))
                        return stockEntry.getKey();
        }
        return 0;
    }
}
