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
    private final String name; // Used only for error messages.
    private final Map<String, String> values;
    private boolean userChangeable;
    private boolean adminChangeable;

    /**
     * Default constructor.
     * @param name The name of the setting, mostly used for error messages.
     * @param userChangeable Boolean value indicating whether users are allowed to change the setting.
     * @param adminChangeable Boolean value indicating whether server admins are allowed to change the setting.
     * @param alteredDefault String value with the altered default value, or null.
     */
    CustomSettings(String name, boolean userChangeable, boolean adminChangeable, String alteredDefault) {
        this.name = name;
        this.values = new HashMap<>();
        this.values.put("DEFAULT", alteredDefault);
    }

    void setUserChangeable(boolean setting) { userChangeable = setting; }
    void setAdminChangeable(boolean setting) { adminChangeable = setting; }
    public String getName() { return name; }


    /**
     * Retrieves a setting for a certain identifier.
     * @param identifier A guild ID, a user ID or "DEFAULT".
     * @return A string with the given setting, or null if it does not exist.
     */
    String getSetting(String identifier) {
        return values.get(identifier);
    }

    /**
     * Method for customizing the setting for a user.
     * @param user The user who wishes to customise the setting.
     * @param value The value which the setting will have for this user.
     * @return A SettingEnum indicating whether the operation was successful.
     */
    SettingEnum putSetting(User user, String value) {
        if (adminChangeable) {
            String receiver = "user " + user.getName();
            return putSetting(user.getId(), receiver, value);
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
            String receiver = "guild " + guild.getName();
            return putSetting(guild.getId(), receiver, value);
        } else {
            return SettingEnum.FORBIDDEN;
        }
    }

    /**
     * Method for setting a new default value.
     * @param value The value for the new default.
     */
    SettingEnum putSetting(String value) {
        return putSetting("DEFAULT", "bot default", value);
    }

    private SettingEnum putSetting(String id, String receiver, String value) {
        try {
            values.put(id, value);
            return SettingEnum.SUCCESSFUL;
        } catch (Exception e) {
            System.out.print("Error: Could not remove setting " + name);
            System.out.println(" for " + receiver);
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
            String receiver = "user " + user.getName();
            return removeSetting(user.getId(), receiver);
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
            String receiver = "guild " + guild.getName();
            return removeSetting(guild.getId(), receiver);
        } else {
            return SettingEnum.FORBIDDEN;
        }
    }

    /**
     * Method for unsetting an altered default value.
     * @return A SettingEnum indicating whether the operation was successful.
     */
    SettingEnum removeSetting() {
        return removeSetting("DEFAULT", "bot default");
    }

    private SettingEnum removeSetting(String id, String receiver) {
        try {
            if (values.containsKey(id)) {
                values.remove(id);
                return SettingEnum.SUCCESSFUL;
            } else {
                return SettingEnum.NOTSET;
            }
        } catch (Exception e) {
            System.out.print("Error: Could not remove setting " + name);
            System.out.println(" for " + receiver);
            System.out.println(e.toString());
            return SettingEnum.ERROR;
        }
    }
}


