package xyz.kazuthecat.coffeebot.settings;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * This class keeps track of all the individual settings, any changes to
 * the settings goes through this class which will also check if they exist
 * and carry out a few standard replacements for placeholders such as
 * {placeholder_name} and the like.
 */
public class SettingsFile extends SettingsAbstract {
    private final String settingsFile;

    /**
     * The constructor for the settings class. When called it will look for
     * any existing settings and load them. If the settings can't load for any
     * reason (such as if they don't exist) it will output a message saying
     * something went wrong and then create a new set of settings.
     */
    public SettingsFile() {
        settingsFile = "botsettings.json";
        customSettings = null;
        try {
            Path settingsPath = Paths.get(settingsFile);
            String json = Files.readString(settingsPath);
            Type settingsMapType = new TypeToken<HashMap<String, Map<String, String>>>() {}.getType();
            customSettings = gson.fromJson(json, settingsMapType);
        } catch (Exception e) {
            System.out.println("Something went wrong when trying to load json.");
        }

        if (customSettings == null) {
            customSettings = new HashMap<>();
        }
    }

    @Override
    void writeJSON(String settingName, String id, String value) {
        // Pretty print json, for debugging
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(customSettings));

        String json = gson.toJson(customSettings);
        try {
            PrintStream out = new PrintStream(new FileOutputStream(settingsFile));
            out.print(json);
            out.flush();
        } catch (Exception e) {
            System.out.println("Error: Failed to write " + settingsFile + " to disk:");
            System.out.println(e.toString());
        }
    }

}
