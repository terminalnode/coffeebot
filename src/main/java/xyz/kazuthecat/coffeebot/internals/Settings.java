package xyz.kazuthecat.coffeebot.internals;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;

public class Settings {
    private final Map<String, SingleSetting> defaults;

    public Settings() {
        defaults = new HashMap<>();
    }

    /**
     * Adds a new setting to the defaults, indicating which settings can and can not be made.
     * @param identifier The name of the setting. Should be of the form commandname.settingname to avoid collisions.
     * @param defaultValue The default value of the setting.
     * @param userChangeable Boolean indicating whether users may change this setting for themselves.
     * @param adminChangeable Boolean indicating whether admins may change this setting for the server.
     */
    public void setDefaults(String identifier, String defaultValue, boolean userChangeable, boolean adminChangeable) {
        SingleSetting newSetting = new SingleSetting(defaultValue, userChangeable, adminChangeable);
        defaults.put(identifier, newSetting);
    }

    /**
     * This function retrieves a given setting, results may vary depending
     * on if there are guild or user preferences overriding the defaults.
     * @param guild The guild in which the setting was asked for.
     * @param user The user who asked  to retrieve the setting.
     * @param settingName The setting that will be retrieved.
     * @return String The setting that was requested.
     */
    public String getSetting(Guild guild, User user, String settingName) {
        SingleSetting defaultValue = defaults.get(settingName);

        return "";
    }

    /**
     * This function alters the bot's default settings.
     * If a server/user has not changed their settings manually,
     * these are the settings that will be used.
     * @param settingName The setting that will be altered.
     * @param settingValue The new value for the setting.
     */
    public void setSetting(String settingName, String settingValue) {

    }

    /**
     * This function alters a guild's preferences.
     * @param guild The guild whose settings are being altered.
     * @param settingName The setting that will be altered.
     * @param settingValue The new value for the setting.
     */
    public void setSetting(Guild guild, String settingName, String settingValue) {

    }

    /**
     * This function alters a user's personal preferences.
     * @param user The user whose settings are being altered.
     * @param settingName The setting that will be altered.
     * @param settingValue The new value for the setting.
     */
    public void setSetting(User user, String settingName, String settingValue) {

    }
}
