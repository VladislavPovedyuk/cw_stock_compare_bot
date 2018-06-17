package ua.vlpo;

import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ua.vlpo.stock.StockCommand;
import ua.vlpo.util.Commands;
import ua.vlpo.util.CommonConstants;

import java.util.Arrays;

public class StockBot extends TelegramLongPollingBot {

    public String getBotUsername() {
        return CommonConstants.CW_STOCK_COMPARE_BOT_USERNAME;
    }

    public String getBotToken() {
        return CommonConstants.CW_STOCK_COMPARE_BOT_TOKEN;
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message inMsg = update.getMessage();
            if ((inMsg.getChatId() == (long) inMsg.getFrom().getId()) && inMsg.hasText()) {
                if (inMsg.getText().startsWith(Commands.HELP.toString()) || inMsg.getText().startsWith(Commands.START.toString())) {
                    helpCommand(inMsg);
                } else if (inMsg.getText().matches("(?s).*\\/add_\\d+ {3}(.+) x (\\d+).*")) {
                    StockCommand.updateStock(this, inMsg);
                } else if (inMsg.getText().startsWith(Commands.STOCK.toString())) {
                    StockCommand.printStock(this, inMsg);
                } else {
                    commandNotFound(inMsg);
                }
            }
        }
    }

    private void commandNotFound(Message inMsg) {
        String out = "<b>Команда не найдена</b>\n" +
                "Доступные команды: " + Arrays.toString(Commands.values());

        SendMessage outMsg = new SendMessage()
                .setChatId(inMsg.getChatId())
                .setParseMode(ParseMode.HTML)
                .setText(out);
        try {
            sendMessage(outMsg);
        } catch (TelegramApiException e) {

        }
    }

    private void helpCommand(Message inMsg) {
        String out = "<b>Базовый функционал</b>\n" +
                "Напиши /start в @ChatWarsTradeBot и пришли мне его ответ (форвардом, либо просто скопируй)! " +
                "Если ты шлешь мне склад уже не первый раз, я тебе скажу разницу между текущим и предыдущим.\n";

        SendMessage outMsg = new SendMessage()
                .setChatId(inMsg.getChatId())
                .setParseMode(ParseMode.HTML)
                .setText(out);
        try {
            sendMessage(outMsg);
        } catch (TelegramApiException e) {

        }
    }
}