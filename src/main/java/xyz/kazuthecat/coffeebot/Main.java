package xyz.kazuthecat.coffeebot;
import xyz.kazuthecat.coffeebot.commands.*;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

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

        EventWaiter waiter = new EventWaiter(); // heaven knows what this is, but I need it.

        /* JDA Utilities uses emoji reacts for success/warning/error messages. These are specified here.
         * Success: Coffee/Hot beverage. Warning: Fire engine. Error: Fire
         * Visibility may vary because intellij is really finicky with emoji. Probably something with encoding. */
        client.setEmojis("☕", "\uD83D\uDE92", "\uD83D\uDD25");

        // ADD COMMANDS
        client.addCommands(
                // Demo functions
                // These have lots of comments to explain certain behaviours.
                new Ping()

                // Other functions
                // ...there's nothing here yet.
        );

        // Go online!
        JDA bot = new JDABuilder(AccountType.BOT)
                .setToken(token) // loaded from config file
                .addEventListeners(waiter, client.build())
                .build(); // in we go!
    }
}
