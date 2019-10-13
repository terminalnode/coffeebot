package xyz.kazuthecat.coffeebot.settings;

import xyz.kazuthecat.coffeebot.DBHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsDB extends SettingsAbstract {
    private final DBHandler dbHandler;
    private boolean dbFunctional;

    /**
     * The main constructor for the class.
     * @param dbHandler A DBHandler object that can read and write to a database.
     */
    public SettingsDB(DBHandler dbHandler) {
        this.dbHandler = dbHandler;

        // Create settings table if absent in database.
        String settingsDB = "CREATE TABLE IF NOT EXISTS settingdb(" +
                "id       VARCHAR(30) NOT NULL, " +
                "name     VARCHAR(30) NOT NULL, " +
                "value    TEXT NOT NULL, " +
                "PRIMARY KEY (id, name) );";
        this.dbFunctional = dbHandler.execute(new String[]{settingsDB});

        // Load settings table from database.
        List<Map<String, String>> loadedSettings = null;
        if (this.dbFunctional) {
            loadedSettings = dbHandler.query("SELECT * FROM settingdb;");
            this.dbFunctional = loadedSettings != null;
        }

        // Map settings table to customSettings (Map<String, Map<String, String>>);
        if (this.dbFunctional) {
            for (Map<String, String> setting : loadedSettings) {
                String name = setting.get("name");
                String owner = setting.get("id");
                String value = setting.get("value");

                if (!customSettings.containsKey("name")) { customSettings.put(name, new HashMap<>()); }
                Map<String, String> customSetting = customSettings.get(name);
                customSetting.put(owner, value);
            }
        }
    }

    /**
     * For legacy reasons this method is called writeJSON, however it actually writes the entries to
     * the database using the DBHandler object provided during initialization.
     * @param settingName The name of the setting that's being updated.
     * @param id The ID of the guild/user for whom the setting is being updated (or "DEFAULT" for owner settings).
     * @param value The value being assigned to the setting.
     */
    @Override
    void writeJSON(String settingName, String id, String value) {
        // Value is null if a setting has been deleted.
        if (value != null) {
            String sql =
                    "INSERT INTO settingdb (id, name, value) VALUES (?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE value = ?;";
            dbHandler.execute(sql, new String[][]{{id, settingName, value, value}});
        } else {
            String sql = "DELETE FROM settingdb WHERE id = ? AND name = ?;";
            dbHandler.execute(sql, new String[][]{{id, settingName}});
        }

    }
}
