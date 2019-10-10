package xyz.kazuthecat.coffeebot.commands.setcommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import xyz.kazuthecat.coffeebot.settings.CustomSettings;
import xyz.kazuthecat.coffeebot.settings.SettingsFile;

import java.util.stream.Collectors;

public class SetListCommand extends Command {
    private final SettingsFile settings;

    public SetListCommand(SettingsFile settings) {
        this.name = "setlist";
        this.aliases = new String[]{"listset,settings"};
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
        String allSettings = settings
                .allSettingsContaining(searchTerm)
                .stream()
                .map(CustomSettings::getName)
                .collect(Collectors.joining(", "));

        if (!allSettings.isBlank()) {
            event.reply(event.getAuthor().getAsMention() + " These are the settings I could find:\n" + allSettings);
        } else {
            event.reply(event.getAuthor().getAsMention() + " I couldn't find any settings matching your query.");
        }

    }
}
