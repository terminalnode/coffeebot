package xyz.kazuthecat.coffeebot.settings;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for holding a custom setting. Keeps a record of all individual and server settings as well as if
 * the default behaviour has been altered (by the bot owner). Also checks if the setting is allowed to be
 * changed by users/admins before doing so.
 */
public class CustomSettings {
    private final String name; // Used only  for error messages.
    private final Map<Long, String> guildValues;
    private final Map<Long, String> userValues;
    private boolean userChangeable;
    private boolean adminChangeable;
    private String alteredDefault;

    /**
     * Default constructor.
     * @param name The name of the setting, mostly used for error messages.
     * @param userChangeable Boolean value indicating whether users are allowed to change the setting.
     * @param adminChangeable Boolean value indicating whether server admins are allowed to change the setting.
     * @param alteredDefault String value with the altered default value, or null.
     */
    CustomSettings(String name, boolean userChangeable, boolean adminChangeable, String alteredDefault) {
        this.name = name;
        this.userChangeable = userChangeable;
        this.adminChangeable = adminChangeable;

        this.guildValues = new HashMap<>();
        this.userValues = new HashMap<>();
        this.alteredDefault = alteredDefault;
    }

    void setUserChangeable(boolean setting) { userChangeable = setting; }
    void setAdminChangeable(boolean setting) { adminChangeable = setting; }
    public boolean isUserChangeable() { return userChangeable; }
    public boolean isAdminChangeable() { return adminChangeable; }
    public String getName() { return name; }

    /**
     * Retrieves the most relevant setting for a given user/guild combination, if the setting is set for user/admin
     * this setting is returned. If it is not set the altered default (which may be null) is returned.
     * @param user The User object which requested the setting.
     * @param guild the Guild object which requested the setting.
     * @return The string value for the setting (may be null).
     */
    String getSetting(User user, Guild guild) {
        String userSetting = userValues.get(user.getIdLong());
        if (userSetting != null && userChangeable) {
            return userSetting;
        }

        String guildSetting = guildValues.get(guild.getIdLong());
        if (guildSetting != null && adminChangeable) {
            return guildSetting;
        }

        return alteredDefault; // May be null!
    }

    /**
     * Method for setting a new default value.
     * @param value The value for the new default.
     */
    void setDefault(String value) {
        alteredDefault = value;
    }

    /**
     * Method for unsetting an altered default value.
     * @return A SettingEnum indicating whether the operation was successful.
     */
    SettingEnum unsetDefault() {
        if (alteredDefault != null) {
            alteredDefault = null;
            return SettingEnum.SUCCESSFUL;
        } else {
            return SettingEnum.NOTSET;
        }
    }

    /**
     * Method for personalizing the setting for a user.
     * @param user The user who wishes to customise the setting.
     * @param value The value which the setting will have for this user.
     * @return A SettingEnum indicating whether the operation was successful.
     */
    SettingEnum putUserSetting(User user, String value) {
        if (userChangeable) {
            try {
                userValues.put(user.getIdLong(), value);
                return SettingEnum.SUCCESSFUL;
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
     * Method for removing the setting made for a given user.
     * @param user The user who wishes to reset the setting.
     * @return A SettingEnum indicating whether the operation was successful.
     */
    SettingEnum removeUserSetting(User user) {
        try {
            if (userValues.containsKey(user.getIdLong())) {
                userValues.remove(user.getIdLong());
                return SettingEnum.SUCCESSFUL;
            } else {
                return SettingEnum.NOTSET;
            }
        } catch (Exception e) {
            System.out.println("Error: Could not remove setting " + name + " for user " + user.getName());
            System.out.println(e.toString());
            return SettingEnum.ERROR;
        }
    }

    /**
     * Method for customizing the setting for a given guild.
     * @param guild The guild thas wishes to customise the setting.
     * @param value The value which the setting will have for this server/guild.
     * @return A SettingEnum indicating whether the operation was successful.
     */
    SettingEnum putGuildSetting(Guild guild, String value) {
        if (adminChangeable) {
            try {
                guildValues.put(guild.getIdLong(), value);
                return SettingEnum.SUCCESSFUL;
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
     * Method for removing the setting made for a given guild.
     * @param guild The guild that wishes to reset their setting.
     * @return A SettingEnum indicating whether the operation was successful.
     */
    SettingEnum removeGuildSetting(Guild guild) {
        try {
            if (guildValues.containsKey((guild.getIdLong()))) {
                guildValues.remove(guild.getIdLong());
                return SettingEnum.SUCCESSFUL;
            } else {
                return SettingEnum.NOTSET;
            }
        } catch (Exception e) {
            System.out.println("Error: Could not remove setting " + name + " for user " + guild.getName());
            System.out.println(e.toString());
            return SettingEnum.ERROR;
        }
    }
}


