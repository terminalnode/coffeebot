package xyz.kazuthecat.coffeebot.settings;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;

public class CustomSettings {
    private final String name; // Used only  for error messages.
    private final Map<Long, String> guildValues;
    private final Map<Long, String> userValues;
    private boolean userChangeable;
    private boolean adminChangeable;
    private String alteredDefault;

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

    void changeDefault(String value) {
        alteredDefault = value;
    }

    SettingEnum putUserSetting(User user, String value) {
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

    SettingEnum putGuildSetting(Guild guild, String value) {
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
}
