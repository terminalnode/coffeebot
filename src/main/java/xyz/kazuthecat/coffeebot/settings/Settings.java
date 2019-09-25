package xyz.kazuthecat.coffeebot.settings;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Settings {
    private final Map<String, SingleSetting> settings;

    public Settings() {
        settings = new HashMap<>();
    }

    /**
     * Adds a new setting with it's default values.
     * @param identifier The name of the setting. Should be of the form commandname.settingname to avoid collisions.
     * @param defaultValue The default value of the setting.
     * @param userChangeable Boolean indicating whether users may change this setting for themselves.
     * @param adminChangeable Boolean indicating whether admins may change this setting for the server.
     */
    public void setDefaults(String identifier, String defaultValue, boolean userChangeable, boolean adminChangeable) {
        settings.put(
                identifier,
                new SingleSetting(
                        identifier,
                        defaultValue,
                        userChangeable,
                        adminChangeable));
    }

    public String getSetting(User user, Guild guild, String identifier) {
        SingleSetting setting = settings.get(identifier);
        if (setting == null) {
            return null;
        }
        return setting.getSetting(user, guild);
    }

    public Set<String> listAllSettings() {
        return settings.keySet();
    }
}
