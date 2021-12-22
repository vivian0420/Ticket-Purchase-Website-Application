package cs601.project4;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ReadConfig {
    public static final String configFileName = "config.json";

    public static Config readConfig() {
        Config config = null;
        Gson gson = new Gson();
        try{
            config = gson.fromJson(new FileReader(configFileName), Config.class);
        } catch (FileNotFoundException e) {
            System.err.println("Config file config.json not found: " + e.getMessage());
        }
        return config;
    }
}

