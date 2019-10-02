package xyz.kazuthecat.coffeebot.settings;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class keeps track of all the individual settings, any changes to
 * the settings goes through this class which will also check if they exist
 * and carry out a few standard replacements for placeholders such as
 * {placeholder_name} and the like.
 */
public class Settings {
    private final Gson gson;
    private final String settingsFile;
    private final Map<String, String> defaultSettings;
    private Map<String, CustomSettings> customSettings;

    /**
     * The constructor for the settings class. When called it will look for
     * any existing settings and load them. If the settings can't load for any
     * reason (such as if they don't exist) it will output a message saying
     * something went wrong and then create a new set of settings.
     */
    public Settings() {
        gson = new Gson();
        settingsFile = "botsettings.json";
        defaultSettings = new HashMap<>();

        customSettings = null;
        try {
            Path settingsPath = Paths.get(settingsFile);
            String json = Files.readString(settingsPath);
            Type settingsMapType = new TypeToken<HashMap<String, CustomSettings>>() {}.getType();
            customSettings = gson.fromJson(json, settingsMapType);
        } catch (Exception e) {
            System.out.println("Something went wrong when trying to load json.");
        }

        if (customSettings == null) {
            customSettings = new HashMap<>();
        }
    }

    /**
     * This function is called by each command that wants to add it's settings. Each call creates
     * one setting if it was not among the settings loaded at start. If the setting already exists
     * it's attributes adminChangeable and userChangeable are overwritten with those provided by
     * the command adding it's settings.
     * @param identifier The name of the setting, e.g. something like hello.response.
     * @param defaultValue The default value for the setting (unless changed though putBotSetting).
     * @param userChangeable Boolean saying whether users are allowed to change this setting.
     * @param adminChangeable Boolean saying whether server admins are allowed to change this setting.
     */
    public void setDefaults(String identifier, String defaultValue, boolean userChangeable, boolean adminChangeable) {
        defaultSettings.put(identifier, defaultValue);

        CustomSettings setting = customSettings.get(identifier);
        if (setting == null) {
            customSettings.put(identifier, new CustomSettings(identifier, userChangeable, adminChangeable, null));
        } else {
            setting.setAdminChangeable(adminChangeable);
            setting.setUserChangeable(userChangeable);
        }
    }

    /**
     * Method for writing the contents of customSettings to disk in JSON format.
     */
    private void writeJSON() {
        // Pretty print json, for debugging
        // System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(customSettings));

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

    /**
     * Method for setting a new default value for a setting. This value will be used in all servers as long
     * as users don't have their own settings or admins have set a server setting.
     * @param identifier The name of the setting.
     * @param value The new value for the setting.
     * @return A SettingEnum indicating whether the operation was successful.
     */
    public SettingEnum putBotSetting(String identifier, String value) {
        if (defaultSettings.containsKey(identifier)) {
            try {
                customSettings
                        .get(identifier)
                        .setDefault(value);
                writeJSON();
                return SettingEnum.SUCCESSFUL;
            } catch (Exception e) {
                System.out.println("Failed to change bot setting +" + identifier + ":");
                System.out.println(e.toString());
                return SettingEnum.ERROR;
            }
        }
        return SettingEnum.DOESNOTEXIST;
    }

    /**
     * Method for resetting a changed default value to the default provided by the command itself.
     * @param identifier The name of the setting.
     * @return A SettingEnum indicating whether the operation was successful.
     */
    public SettingEnum removeBotSetting(String identifier) {
        if (defaultSettings.containsKey(identifier)) {
            return customSettings
                    .get(identifier)
                    .unsetDefault();
        }
        return SettingEnum.DOESNOTEXIST;
    }

    /**
     * Method for changing a setting for an individual user.
     * @param user The User object for the user.
     * @param identifier The name of the setting.
     * @param value The new value for the setting.
     * @return A SettingEnum indicating whether the operation was successful.
     */
    public SettingEnum putUserSetting(User user, String identifier, String value) {
        if (defaultSettings.containsKey(identifier)) {
            SettingEnum result = customSettings
                    .get(identifier)
                    .putUserSetting(user, value);
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
    public SettingEnum removeUserSetting(User user, String identifier) {
        if (defaultSettings.containsKey(identifier)) {
            SettingEnum result = customSettings
                    .get(identifier)
                    .removeUserSetting(user);
            writeJSON();
            return result;
        } else {
            return SettingEnum.DOESNOTEXIST;
        }
    }

    /**
     * Method for changing a setting for a specific server.
     * @param guild The Guild object for the server.
     * @param identifier The name of the setting.
     * @param value The new value for the setting.
     * @return A SettingEnum indicating whether the operation was successful.
     */
    public SettingEnum putGuildSetting(Guild guild, String identifier, String value) {
        if (defaultSettings.containsKey(identifier)) {
            SettingEnum result = customSettings
                    .get(identifier)
                    .putGuildSetting(guild, value);
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
    public SettingEnum removeGuildSetting(Guild guild, String identifier) {
        if (defaultSettings.containsKey(identifier)) {
            SettingEnum result = customSettings
                    .get(identifier)
                    .removeGuildSetting(guild);
            writeJSON();
            return result;
        } else {
            return SettingEnum.DOESNOTEXIST;
        }
    }

    /**
     * Method for retrieving the most relevant setting for responding to a message.
     * If a user setting is set (and the setting is currently userChangeable), that setting is returned.
     * If a guild setting is set (and the setting is currently adminChangeable), that setting is returned.
     * If the default setting has been altered, that setting is returned.
     * If the setting has not been altered, the default setting is returned.
     * @param identifier The name of the setting.
     * @param message The message requesting the setting.
     * @return A string containing the setting.
     */
    public String getSetting(String identifier, Message message) {
        if (!defaultSettings.containsKey(identifier)) {
            return null;
        }

        CustomSettings setting = customSettings.get(identifier);
        String result;

        if (setting == null) {
            result = null;
        } else {
            result = setting.getSetting(message);
        }

        if (result == null) {
            return standardReplacements(defaultSettings.get(identifier), message);
        } else if (result.contains("{")) {
            return standardReplacements(result, message);
        } else {
            return result;
        }
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
                .replaceAll("\\{botname\\}", message.getJDA().getSelfUser().getName())
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
