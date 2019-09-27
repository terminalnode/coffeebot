package xyz.kazuthecat.coffeebot.commands.setcommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import xyz.kazuthecat.coffeebot.settings.Settings;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SetListCommand extends Command {
    private final Settings settings;

    public SetListCommand(Settings settings) {
        this.name = "setlist";
        this.help = "lets you search for settings";
        this.arguments = "searchQuery";
        this.guildOnly = false;
        this.category = new Category("Settings");

        // Set settings
        this.settings = settings;
    }

    @Override
    protected void execute(CommandEvent event) {
        String searchTerm = String.join(
                ".",
                event.getArgs().split(" ")
        );
        event.reply("If this was implemented I would've searched for: " + searchTerm);

    }
}
