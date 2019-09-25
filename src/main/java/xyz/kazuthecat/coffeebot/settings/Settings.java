package xyz.kazuthecat.coffeebot.settings;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    public SettingEnum putUserSetting(User user, String identifier, String value) {
        SingleSetting setting = settings.get(identifier);
        if (setting == null) {
            return SettingEnum.DOESNOTEXIST;
        }
        return setting.putUserSetting(user, value);
    }

    /**
     * Retrieve the most relevant setting for a certain user in a certain guild.
     * @param user The user in question.
     * @param guild The guild in question.
     * @param identifier The name of the setting.
     * @return User setting if available, guild setting if not, default setting if we have neither.
     */
    public String getSetting(User user, Guild guild, String identifier) {
        SingleSetting setting = settings.get(identifier);
        if (setting == null) {
            return null;
        }
        return setting.getSetting(user, guild);
    }

    /**
     * Get a set of all registered settings.
     * @return The set of all currently listed settings.
     */
    public Set<String> listAllSettings() {
        return settings.keySet();
    }

    /**
     * Get a list of all registered settings of which substring is part of the identifier.
     * @param substring The substring which all returned settings must contain.
     * @return The set of all currently listed settings of which substring is part of the identifier.
     */
    public Set<String> listAllSettingsContaining(String substring) {
        return settings.keySet().stream()
                .filter(x -> x.contains(substring))
                .collect(Collectors.toSet());
    }
}
