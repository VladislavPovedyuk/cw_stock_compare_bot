package ua.vlpo.stock;

import org.telegram.telegrambots.api.objects.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class UserStock {
    private User user;
    private HashMap<String, Integer> stock;
    private String date;

    public UserStock(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public HashMap<String, Integer> getStock() {
        return stock;
    }

    public void setStock(HashMap<String, Integer> stock) {
        this.stock = stock;
        this.date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
    }

    public String getDate() {
        return date;
    }
}
