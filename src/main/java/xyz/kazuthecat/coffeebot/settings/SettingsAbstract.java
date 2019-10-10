package xyz.kazuthecat.coffeebot.settings;

import com.google.gson.Gson;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

abstract class SettingsAbstract {
    final Gson gson = new Gson();
    private final Map<String, String> defaultSettings = new HashMap<>();
    private Map<String, Boolean> userChangeable = new HashMap<>();
    private Map<String, Boolean> adminChangeable = new HashMap<>();
    Map<String, CustomSettings> customSettings = new HashMap<>();

    abstract void writeJSON();

    /**
     * This function is called by each command that wants to add it's settings. Each call creates
     * one setting if it was not among the settings loaded at start. If the setting already exists
     * it's attributes adminChangeable and userChangeable are overwritten with those provided by
     * the command adding it's settings.
     * @param settingName The name of the setting, e.g. something like hello.response.
     * @param defaultValue The default value for the setting (unless changed though putBotSetting).
     * @param userChangeable Boolean saying whether users are allowed to change this setting.
     * @param adminChangeable Boolean saying whether server admins are allowed to change this setting.
     */
    public void setDefaults(String settingName, String defaultValue, boolean userChangeable, boolean adminChangeable) {
        defaultSettings.put(settingName, defaultValue);
        this.userChangeable.put(settingName, userChangeable);
        this.adminChangeable.put(settingName, adminChangeable);

        CustomSettings setting = customSettings.get(settingName);
        if (setting == null) {
            customSettings.put(settingName, new CustomSettings(settingName, userChangeable, adminChangeable, null));
        } else {
            setting.setAdminChangeable(adminChangeable);
            setting.setUserChangeable(userChangeable);
        }
    }

    /**
     * Method for changing a setting for an individual user.
     * @param user The User object for the user.
     * @param settingName The name of the setting.
     * @param value The new value for the setting.
     * @return A SettingEnum indicating whether the operation was successful.
     */
    public SettingEnum putSetting(User user, String settingName, String value) {
        if (defaultSettings.containsKey(settingName)) {
            SettingEnum result = customSettings
                    .get(settingName)
                    .putSetting(user, value);
            writeJSON();
            return result;
        } else {
            return SettingEnum.DOESNOTEXIST;
        }
    }

    /**
     * Method for changing a setting for a specific server.
     * @param guild The Guild object for the server.
     * @param settingName The name of the setting.
     * @param value The new value for the setting.
     * @return A SettingEnum indicating whether the operation was successful.
     */
    public SettingEnum putSetting(Guild guild, String settingName, String value) {
        if (defaultSettings.containsKey(settingName)) {
            SettingEnum result = customSettings
                    .get(settingName)
                    .putSetting(guild, value);
            writeJSON();
            return result;
        } else {
            return SettingEnum.DOESNOTEXIST;
        }
    }

    /**
     * Method for setting a new default value for a setting. This value will be used in all servers as long
     * as users don't have their own settings or admins have set a server setting.
     * @param settingName The name of the setting.
     * @param value The new value for the setting.
     * @return A SettingEnum indicating whether the operation was successful.
     */
    public SettingEnum putSetting(String settingName, String value) {
        if (defaultSettings.containsKey(settingName)) {
            SettingEnum result = customSettings
                    .get(settingName)
                    .putSetting(value);
            writeJSON();
            return result;
        } else {
            return SettingEnum.DOESNOTEXIST;
        }
    }

    /**
     * Method for resetting a setting for an individual user.
     * @param user The User object for the user.
     * @param identifier The name of the setting.
     * @return A SettingEnum indicating whether the operation was successful.
     */
    public SettingEnum removeSetting(User user, String identifier) {
        if (defaultSettings.containsKey(identifier)) {
            SettingEnum result = customSettings
                    .get(identifier)
                    .removeSetting(user);
            writeJSON();
            return result;
        } else {
            return SettingEnum.DOESNOTEXIST;
        }
    }

    /**
     * Method for resetting a setting for a specific server.
     * @param guild The Guild object for the server.
     * @param identifier The name of the setting.
     * @return A SettingEnum indicating whether the operation was successful.
     */
    public SettingEnum removeSetting(Guild guild, String identifier) {
        if (defaultSettings.containsKey(identifier)) {
            SettingEnum result = customSettings
                    .get(identifier)
                    .removeSetting(guild);
            writeJSON();
            return result;
        } else {
            return SettingEnum.DOESNOTEXIST;
        }
    }

    /**
     * Method for resetting a changed default value to the default provided by the command itself.
     * @param identifier The name of the setting.
     * @return A SettingEnum indicating whether the operation was successful.
     */
    public SettingEnum removeSetting(String identifier) {
        if (defaultSettings.containsKey(identifier)) {
            SettingEnum result = customSettings
                    .get(identifier)
                    .removeSetting();
            writeJSON();
            return result;
        }
        return SettingEnum.DOESNOTEXIST;
    }

    /**
     * Method for retrieving the most relevant setting for responding to a message.
     * If a user setting is set (and the setting is currently userChangeable), that setting is returned.
     * If a guild setting is set (and the setting is currently adminChangeable), that setting is returned.
     * If the default setting has been altered, that setting is returned.
     * If the setting has not been altered, the default setting is returned.
     * @param settingName The name of the setting.
     * @param message The message requesting the setting.
     * @return A string containing the setting.
     */
    public String getSetting(String settingName, Message message) {
        String botSetting = null, userSetting = null, guildSetting = null, result;
        CustomSettings setting = customSettings.get(settingName);

        if (setting != null) {
            if (userChangeable.get(settingName))
                userSetting = setting.getSetting(message.getAuthor().getId());
            if (adminChangeable.get(settingName) && message.isFromGuild())
                guildSetting = setting.getSetting(message.getGuild().getId());
            botSetting = setting.getSetting("DEFAULT");
        }

        if (userSetting != null) {
            result = standardReplacements(userSetting, message);
        } else if (guildSetting != null) {
            result = standardReplacements(guildSetting, message);
        } else if (botSetting != null) {
            result = standardReplacements(botSetting, message);
        } else {
            result = standardReplacements(defaultSettings.get(settingName), message);
        }

        return result;
    }

    /**
     * Method for carrying out a few standard replacements on a retrieved setting.
     * Placeholders are indicated with {brackets}.
     * @param input The string we're doing replacements on.
     * @param message The message requesting the setting.
     * @return A modified version of input with all replacements carried out.
     */
    private String standardReplacements(String input, Message message) {
        return input
                .replaceAll("\\{user\\}", message.getAuthor().getAsMention())
                .replaceAll("\\{botname\\}", message.getJDA().getSelfUser().getAsMention())
                .replaceAll("\\{content\\}", message.getContentRaw().strip());
    }

    /**
     * Method for searching through the settings by looking for setting names containing the provided substring.
     * @param substring The substring we're filtering settings based on.
     * @return A set of CustomSettings whose names contain the provided substring.
     */
    public Set<CustomSettings> allSettingsContaining(String substring) {
        return customSettings.entrySet().stream()
                .filter(x -> x.getKey().contains(substring))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }
}
