package xyz.kazuthecat.coffeebot.settings;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
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
    private final Map<String, String> idValues;
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

        this.idValues = new HashMap<>();
        this.alteredDefault = alteredDefault;
    }

    void setUserChangeable(boolean setting) { userChangeable = setting; }
    void setAdminChangeable(boolean setting) { adminChangeable = setting; }
    public String getName() { return name; }

    /**
     * Retrieves the most relevant setting for a given user/guild combination, if the setting is set for user/admin
     * this setting is returned. If it is not set the altered default (which may be null) is returned.
     * @param message The message context for which the setting will be retrieved.
     * @return The string value for the setting (may be null).
     */
    String getSetting(Message message) {
        String setting = alteredDefault;

        if (adminChangeable && message.isFromGuild()) {
            String guildSetting = idValues.get(message.getGuild().getId());
            if (guildSetting != null) { setting = guildSetting; }
        }

        if (userChangeable) {
            String userSetting = idValues.get(message.getAuthor().getId());
            if (userSetting != null) { setting = userSetting; }
        }

        return setting; // May be null!
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
     * Method for customizing the setting for a user.
     * @param user The user who wishes to customise the setting.
     * @param value The value which the setting will have for this user.
     * @return A SettingEnum indicating whether the operation was successful.
     */
    SettingEnum putSetting(User user, String value) {
        if (adminChangeable) {
            return putSetting(user.getId(), user.getName(), "user", value);
        } else {
            return SettingEnum.FORBIDDEN;
        }
    }

    /**
     * Method for customizing the setting for a given guild.
     * @param guild The guild that wishes to customise the setting.
     * @param value The value which the setting will have for this server/guild.
     * @return A SettingEnum indicating whether the operation was successful.
     */
    SettingEnum putSetting(Guild guild, String value) {
        if (adminChangeable) {
            return putSetting(guild.getId(), guild.getName(), "guild", value);
        } else {
            return SettingEnum.FORBIDDEN;
        }
    }

    private SettingEnum putSetting(String id, String receiver, String requestType, String value) {
        try {
            idValues.put(id, value);
            return SettingEnum.SUCCESSFUL;
        } catch (Exception e) {
            System.out.print("Error: Could not remove setting " + name);
            System.out.println(" for " + requestType + " " + receiver);
            System.out.println(e.toString());
            return SettingEnum.ERROR;
        }
    }

    /**
     * Method for removing the setting made for a given user.
     * @param user The user who wishes to reset the setting.
     * @return A SettingEnum indicating whether the operation was successful.
     */
    SettingEnum removeSetting(User user) {
        if (userChangeable) {
            return removeSetting(user.getId(), user.getName(), "user");
        } else {
            return SettingEnum.FORBIDDEN;
        }
    }

    /**
     * Method for removing the setting made for a given guild.
     * @param guild The guild that wishes to reset their setting.
     * @return A SettingEnum indicating whether the operation was successful.
     */
    SettingEnum removeSetting(Guild guild) {
        if (adminChangeable) {
            return removeSetting(guild.getId(), guild.getName(), "guild");
        } else {
            return SettingEnum.FORBIDDEN;
        }
    }

    private SettingEnum removeSetting(String id, String receiver, String requestType) {
        try {
            if (idValues.containsKey(id)) {
                idValues.remove(id);
                return SettingEnum.SUCCESSFUL;
            } else {
                return SettingEnum.NOTSET;
            }
        } catch (Exception e) {
            System.out.print("Error: Could not remove setting " + name);
            System.out.println(" for " + requestType + " " + receiver);
            System.out.println(e.toString());
            return SettingEnum.ERROR;
        }
    }
}


