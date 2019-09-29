package xyz.kazuthecat.coffeebot.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

public class Settings {
    private final Gson gson;
    private final String settingsFile;
    private final Map<String, String> defaultSettings;
    private Map<String, CustomSettings> customSettings;

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
        if (customSettings == null) customSettings = new HashMap<>();
    }

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

    public SettingEnum putBotSetting(String identifier, String value) {
        if (defaultSettings.containsKey(identifier)) {
            try {
                customSettings
                        .get(identifier)
                        .setDefault(value);
                writeJSON();
                return SettingEnum.SUCCCESSFUL;
            } catch (Exception e) {
                System.out.println("Failed to change bot setting +" + identifier + ":");
                System.out.println(e.toString());
                return SettingEnum.ERROR;
            }
        }
        return SettingEnum.DOESNOTEXIST;
    }

    public SettingEnum removeBotSetting(String identifier) {
        if (defaultSettings.containsKey(identifier)) {
            return customSettings
                    .get(identifier)
                    .unsetDefault();
        }
        return SettingEnum.DOESNOTEXIST;
    }

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

    public String getSetting(String identifier, Message message) {
        if (!defaultSettings.containsKey(identifier)) {
            return null;
        }

        CustomSettings setting = customSettings.get(identifier);
        Guild guild = message.getGuild();
        User user = message.getAuthor();
        String result;

        if (setting == null) {
            result = null;
        } else {
            result = setting.getSetting(user, guild);
        }

        if (result == null) {
            return standardReplacements(defaultSettings.get(identifier), message);
        } else if (result.contains("{")) {
            return standardReplacements(result, message);
        } else {
            return result;
        }
    }

    private String standardReplacements(String input, Message message) {
        return input
                .replaceAll("\\{user\\}", message.getAuthor().getAsMention())
                .replaceAll("\\{botname\\}", message.getJDA().getSelfUser().getName())
                .replaceAll("\\{content\\}", message.getContentRaw().strip());
    }

    public Set<CustomSettings> allSettingsContaining(String substring) {
        return customSettings.entrySet().stream()
                .filter(x -> x.getKey().contains(substring))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }
}
