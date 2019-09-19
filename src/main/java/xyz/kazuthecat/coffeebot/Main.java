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
    // Since it's the main class there's little harm in having it throw a login exception.
    // Program will crash and then the user can fix their token or whatever.
    public static void main(String[] args) throws LoginException, IOException {
        // LOAD CONFIG
        // Config file should be placed in project root.
        // First line should be bot token, second line owner ID.
        List<String> configFile = Files.readAllLines(Paths.get("config"));
        String token = configFile.get(0).strip();
        String ownerID = configFile.get(1);

        // BUILD CLIENT
        CommandClientBuilder client = new CommandClientBuilder();
        client.setOwnerId(ownerID);        // loaded from config file
        client.setPrefix("?");             // prefix used for all of our dank commands
        client.setEmojis(                  // apparently JDA Utilities uses emojis for a bunch of stuff
                "☕", // Coffee emoji
                "\uD83D\uDE92", // Fire engine emoji
                "\uD83D\uDD25" // Fire emoji
        );
        EventWaiter waiter = new EventWaiter(); // heaven knows what this is, but I need it.

        // ADD COMMANDS
        client.addCommands(
                // Adding commands be like:
                // new HeyThereCommand();
                // new HelloThereCommand(waiter);
                new Ping()
        );

        // LOGIN
        JDA bot = new JDABuilder(AccountType.BOT)
                .setToken(token) // loaded from config file
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .addEventListeners(waiter, client.build())
                .setActivity(Activity.playing("with Java")) // playing with Java
                .build(); // in we go!
    }
}
