package xyz.kazuthecat.coffeebot.internals;

/**
 * This class contains a single (default) setting of a command.
 * Some commands may have more than one of these, one for each
 * setting they allow. It contains a String with the default value
 * for the setting as well as booleans indicating if users/admins
 * are allowed to change the setting (if both of these are false,
 * only the owner will be allowed to change it.)
 */
public class SingleSetting {
    private final String defaultValue;
    private final boolean userChangeable;
    private final boolean adminChangeable;

    /**
     * The default constructor.
     * @param defaultValue The value that this setting will have by default.
     * @param userChangable Whether users can change this setting for themselves.
     * @param adminChangeable Whether admins can change this setting server-wide.
     */
    public SingleSetting(String defaultValue, boolean userChangable, boolean adminChangeable) {
        this.defaultValue = defaultValue;
        this.userChangeable = userChangable;
        this.adminChangeable = adminChangeable;
    }

    /**
     * Get the default value of the setting.
     * @return The default value for the setting.
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Check whether users are allowed to change the setting for themselves.
     * @return Boolean value indicating if users are allowed to change this setting for themselves.
     */
    public boolean userCanChange() {
        return userChangeable;
    }

    /**
     * Check whether admins are allowed to change the setting for the server.
     * @return Boolean value indicating is admins are allowed to change this setting for the server.
     */
    public boolean adminCanChange() {
        return adminChangeable;
    }
}
