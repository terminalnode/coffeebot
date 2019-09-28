package xyz.kazuthecat.coffeebot.commands.setcommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import xyz.kazuthecat.coffeebot.settings.Settings;

public class UnSetCommand extends Command {
    private final Settings settings;

    public UnSetCommand(Settings settings) {
        this.name = "unset";
        this.help = "unsets a setting for the current user";
        this.arguments = "settingName";
        this.guildOnly = false;
        this.category = new Category("Settings");

        // Set settings
        this.settings = settings;
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply(this.name + " is WIP");
    }
}
