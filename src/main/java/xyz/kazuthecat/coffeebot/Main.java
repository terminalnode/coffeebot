package xyz.kazuthecat.coffeebot;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xyz.kazuthecat.coffeebot.commands.*;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import xyz.kazuthecat.coffeebot.commands.setcommands.SetCommand;
import xyz.kazuthecat.coffeebot.commands.setcommands.SetGuildCommand;
import xyz.kazuthecat.coffeebot.commands.setcommands.SetOwnerCommand;
import xyz.kazuthecat.coffeebot.settings.Settings;
import xyz.kazuthecat.coffeebot.listeners.HelloListener;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws LoginException, IOException {
        // LOAD CONFIG
        // Config file should be placed in project root.
        // First line should be bot token, second line owner ID.
        List<String> configFile = Files.readAllLines(Paths.get("config"));
        String token = configFile.get(0).strip();
        String ownerID = configFile.get(1);

        // BUILD CLIENT
        // Setting some client options.
        CommandClientBuilder client = new CommandClientBuilder();
        client.setOwnerId(ownerID);                         // loaded from configFile above.
        client.setPrefix("?");                              // ? because we never know if it will work
        client.setStatus(OnlineStatus.DO_NOT_DISTURB);      // Always busy...
        client.setActivity(Activity.playing("with Java"));  // ...doin' sum Java.
        Settings settings = new Settings();                 // Holds all of our settings, obviously.

        // EventWaiter is required for certain functions where we await some specific event.
        // For example when we want to reply to a user or something.
        EventWaiter eventWaiter = new EventWaiter();
        ListenerAdapter helloListener = new HelloListener(eventWaiter);


        /* JDA Utilities uses emoji reacts for success/warning/error messages. These are specified here.
         * Success: Coffee/Hot beverage. Warning: Fire engine. Error: Fire
         * Visibility may vary because intellij is really finicky with emoji. Probably something with encoding. */
        client.setEmojis("☕", "\uD83D\uDE92", "\uD83D\uDD25");

        // ADD COMMANDS
        client.addCommands(
                // Demo functions
                // These have lots of comments to explain certain behaviours.
                new PingCommand(settings),

                // Other functions
                new SetCommand(settings),
                new SetGuildCommand(settings),
                new SetOwnerCommand(settings)
        );

        // Go online!
        JDA bot = new JDABuilder(AccountType.BOT)
                .setToken(token) // loaded from config file
                .addEventListeners(
                        eventWaiter,
                        helloListener,
                        client.build()
                ).build(); // in we go!
    }
}
