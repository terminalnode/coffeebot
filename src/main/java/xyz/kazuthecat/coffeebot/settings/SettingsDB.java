package xyz.kazuthecat.coffeebot.settings;

import xyz.kazuthecat.coffeebot.DBHandler;

public class SettingsDB extends SettingsAbstract {
    private final DBHandler dbHandler;

    public SettingsDB(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    @Override
    void writeJSON() {

    }
}
