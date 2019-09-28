package xyz.kazuthecat.coffeebot.commands.setcommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import xyz.kazuthecat.coffeebot.settings.Settings;

public class UnSetOwnerCommand extends Command {
    private final Settings settings;

    public UnSetOwnerCommand(Settings settings) {
        this.name = "unsetowner";
        this.help = "changes the default value for a setting";
        this.aliases = new String[]{"ownerunset", "unsetbot", "botunset"};
        this.arguments = "settingName value";
        this.guildOnly = false;
        this.ownerCommand = true;
        this.category = new Category("Settings");

        // Set settings
        this.settings = settings;
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply(this.name + " is WIP");
    }
}
