package ua.vlpo.stock;

import org.telegram.telegrambots.api.objects.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class UserMain {
    private User user;
    private HashMap<String, Integer> sum;
    private String date;

    public UserMain(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public HashMap<String, Integer> getSum() {
        return sum;
    }

    public void setSum(HashMap<String, Integer> sum) {
        this.sum = sum;
        this.date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
    }

    public String getDate() {
        return date;
    }
}
