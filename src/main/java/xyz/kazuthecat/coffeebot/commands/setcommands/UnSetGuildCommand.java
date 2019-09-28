package xyz.kazuthecat.coffeebot.commands.setcommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;
import xyz.kazuthecat.coffeebot.settings.Settings;

public class UnSetGuildCommand extends Command {
    private final Settings settings;

    public UnSetGuildCommand(Settings settings) {
        this.name = "unsetserver";
        this.help = "unsets a setting for the current guild/server";
        this.aliases = new String[]{"guildunset","unsetguild","serverunset"};
        this.userPermissions = new Permission[]{Permission.ADMINISTRATOR};
        this.arguments = "settingName";
        this.guildOnly = true;
        this.category = new Category("Settings");

        // Set settings
        this.settings = settings;
    }

    @Override
    protected void execute(CommandEvent event) {
        event.reply(this.name + " is WIP");
    }
}
