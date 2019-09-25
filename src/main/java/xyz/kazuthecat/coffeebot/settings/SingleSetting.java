package xyz.kazuthecat.coffeebot.settings;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;

/**
 * A class which holds a specific setting, handles changing of that
 * setting and so on. A single function can have many settings and
 * thus many of these objects.
 */
public class SingleSetting {
    private final String name;
    private final String defaultValue;
    private final boolean userChangeable;
    private final boolean adminChangeable;
    private final Map<Long, String> guildValues;
    private final Map<Long, String> userValues;

    /**
     * Constructor for the class.
     * @param name Name of the setting (e.q. "ping.response")
     * @param defaultValue Default value for the setting.
     * @param userChangeable Boolean indicating whether users are allowed to change this setting for themselves.
     * @param adminChangeable Boolean indicating whether admins are allowed to change this setting for their guild.
     */
    public SingleSetting(String name, String defaultValue, boolean userChangeable, boolean adminChangeable) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.userChangeable = userChangeable;
        this.adminChangeable = adminChangeable;

        this.guildValues = new HashMap<>();
        this.userValues = new HashMap<>();
    }

    /**
     * Retrieves the most relevant setting value. First checks for user settings,
     * then for guild settings and finally returns the default value if none is found.
     * @param user The user who triggered the command.
     * @param guild The guild in which it was triggered.
     * @return The setting value.
     */
    public String getSetting(User user, Guild guild) {
        String userSetting = userValues.get(user.getIdLong());
        if (userSetting != null) {
            return userSetting;
        }

        String guildSetting = guildValues.get(guild.getIdLong());
        if (guildSetting != null) {
            return guildSetting;
        }

        return defaultValue;
    }

    /**
     * Change the setting for a guild.
     * @param guild The guild whose setting we're changing.
     * @param value The value we're assigning to the setting.
     * @return SettingEnum indicating whether the operation was successful.
     */
    public SettingEnum putGuildSetting(Guild guild, String value) {
        if (adminChangeable) {
            try {
                guildValues.put(guild.getIdLong(), value);
                return SettingEnum.SUCCCESSFUL;
            } catch (Exception e) {
                System.out.println("Error: Could not change setting " + name + " for guild " + guild.getName());
                System.out.println(e.toString());
                return SettingEnum.ERROR;
            }
        } else {
            return SettingEnum.FORBIDDEN;
        }
    }

    /**
     * Change the setting for a single user.
     * @param user The user whose setting we're changing.
     * @param value The value we're assigning to the setting.
     * @return SettingEnum indicating whether the operation was successful.
     */
    public SettingEnum putUserSetting(User user, String value) {
        if (userChangeable) {
            try {
                userValues.put(user.getIdLong(), value);
                return SettingEnum.SUCCCESSFUL;
            } catch (Exception e) {
                System.out.println("Error: Could not change setting " + name + " for user " + user.getName());
                System.out.println(e.toString());
                return SettingEnum.ERROR;
            }
        } else {
            return SettingEnum.FORBIDDEN;
        }
    }

    /**
     * Retrieve the default value for the setting.
     * @return String value containing the default value for the setting.
     */
    public String getDefaultValue() { return defaultValue; }

    /**
     * Check whether the setting can be changed on a per-user basis.
     * @return Boolean value indicating whether the setting can be changed on a per-user basis.
     */
    public boolean isUserChangeable() { return userChangeable; }

    /**
     * Check whether the setting can be changed on a per-guild basis.
     * @return Boolean value indicating whether the setting can be changed on a per-guild basis.
     */
    public boolean isAdminChangeable() { return adminChangeable; }
}
