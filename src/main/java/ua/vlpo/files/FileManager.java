package ua.vlpo.files;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;

public class FileManager {
    private static String homepath = System.getProperty("user.home");

    private static final String FILE_PATH = FileManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();

    private static Gson gson = new Gson();

    public static int saveMap(HashMap data, String filename) {
        try {
            FileWriter writer = new FileWriter(new File(FILE_PATH + File.separator + filename));
            gson.toJson(data, writer);
            writer.close();
        } catch (IOException e) {
            return -1;
        }
        return 0;
    }

    public static HashMap openMap(Type typeOfT, String filename) {
        try {
            JsonReader reader = new JsonReader(new FileReader(new File(FILE_PATH + File.separator + filename)));
            HashMap data = gson.fromJson(reader, typeOfT);
            reader.close();
            return data;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }
}
