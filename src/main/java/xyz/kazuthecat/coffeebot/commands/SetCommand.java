package xyz.kazuthecat.coffeebot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import xyz.kazuthecat.coffeebot.internals.Settings;

public class SetCommand extends Command {
    private final Settings settings;

    public SetCommand(Settings settings) {
        this.name = "set";
        this.help = "sets a setting";
        this.guildOnly = false;

        // Set settings
        this.settings = settings;
    }

    @Override
    protected void execute(CommandEvent event) {

    }
}
