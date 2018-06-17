package ua.vlpo.stock;

import org.apache.commons.lang.StringUtils;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ua.vlpo.StockBot;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StockCommand {
    private static final Pattern tradePattern = Pattern.compile("\\/add_\\d+ {3}(.+) x (\\d+)");

    public static void updateStock(StockBot bot, Message inMsg) {
        UserStock userStock;

        if (!StockStorage.userStocks.containsKey(inMsg.getFrom().getId())) {
            userStock = new UserStock(inMsg.getFrom());
            StockStorage.userStocks.put(inMsg.getFrom().getId(), userStock);
        } else {
            userStock = StockStorage.userStocks.get(inMsg.getFrom().getId());
            userStock.setUser(inMsg.getFrom());
        }

        HashMap<String, Integer> prevStock = userStock.getStock();
        HashMap<String, Integer> curStock = new HashMap<>();

        Matcher matcher = tradePattern.matcher(inMsg.getText());
        String stockName;
        int stockCount;
        int count = 0;
        while (matcher.find()) {
            count++;
            stockName = matcher.group(1);
            stockCount = Integer.parseInt(matcher.group(2));
            curStock.put(stockName, stockCount);
        }

        String out = "\uD83D\uDCE6Добавлено <b>" + count + "</b> наименований";

        if (prevStock != null) {
            out += "\n" + difference("Разница в складах", prevStock, curStock, userStock.getDate());
        }

        userStock.setStock(curStock);
        StockStorage.saveStocks();

        try {
            SendMessage outMsg = new SendMessage()
                    .setChatId(inMsg.getChatId())
                    .setParseMode(ParseMode.HTML)
                    .setText(out);
            bot.sendMessage(outMsg);
        } catch (TelegramApiException e) {

        }
    }

    public static void printStock(StockBot bot, Message inMsg) {
        String out = stockString(inMsg.getFrom().getId());

        try {
            SendMessage outMsg = new SendMessage()
                    .setChatId(inMsg.getChatId())
                    .setParseMode(ParseMode.HTML)
                    .setText(out);
            bot.sendMessage(outMsg);
        } catch (TelegramApiException e) {

        }
    }

    private static String stockString(int id) {
        String out;
        if (StockStorage.userStocks.containsKey(id)) {
            UserStock userStock = StockStorage.userStocks.get(id);
            out = "\uD83D\uDCE6Склад <b> @" + userStock.getUser().getUserName() + "</b>";
            if (userStock.getStock() == null || userStock.getStock().isEmpty()) {
                out = "\nСклад пуст";
            } else {
                out += "\n\uD83D\uDD51<code>" + userStock.getDate() + "</code>";
                out += stockString(userStock.getStock());
            }
        } else {
            out = "Склад не найден...";
        }
        return out;
    }

    private static String stockString(HashMap<String, Integer> stock) {
        String out = StringUtils.EMPTY;
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(stock.entrySet());
        Collections.sort(sortedEntries, (o1, o2) -> (o2.getValue() - o1.getValue()));
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            out += "\n" + entry.getKey() + " (" + entry.getValue() + ")";
        }
        return out;
    }

    private static String difference(String intro, HashMap<String, Integer> prevStock,
                                     HashMap<String, Integer> curStock, String date) {
        String out;
        ArrayList<String> keys = new ArrayList<>(prevStock.keySet());
        curStock.keySet().stream().filter(key -> !keys.contains(key)).forEach(keys::add);

        HashMap<String, Integer> diffStock = new HashMap<>();
        int sumDiff = 0;
        for (String key : keys) {
            int diff = (curStock.containsKey(key) ? curStock.get(key) : 0) -
                    (prevStock.containsKey(key) ? prevStock.get(key) : 0);
            if (diff != 0)
                diffStock.put(key, diff);
            sumDiff += diff;
        }
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(diffStock.entrySet());
        Collections.sort(sortedEntries, (o1, o2) -> (o2.getValue() - o1.getValue()));
        if (sumDiff > 0)
            out = "\ud83c\udf1d" + intro + ": <b>+" + sumDiff + "</b>";
        else
            out = "\ud83c\udf1a" + intro + ": <b>" + sumDiff + "</b>";
        if (sortedEntries.isEmpty()) {
            out += "\n\nНичего не изменилось ¯\\_(ツ)_/¯";
        } else {
            String outPlus = "";
            String outMinus = "";
            for (Map.Entry<String, Integer> entry : sortedEntries) {
                if (entry.getValue() > 0)
                    outPlus += "\n" + entry.getKey() + " +" + entry.getValue();
                else
                    outMinus += "\n" + entry.getKey() + " " + entry.getValue();
            }
            if (outPlus.length() != 0)
                out += "\n\n➕<b>Получено:</b>" + outPlus;
            if (outMinus.length() != 0)
                out += "\n\n➖<b>Потрачено:</b>" + outMinus;
        }
        if (date != null)
            out += "\n\n\uD83D\uDD51Относительно <code>" + date + "</code>";
        return out;
    }
}
