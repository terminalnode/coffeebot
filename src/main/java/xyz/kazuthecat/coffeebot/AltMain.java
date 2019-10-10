package xyz.kazuthecat.coffeebot;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xyz.kazuthecat.coffeebot.settings.SettingsFile;
import xyz.kazuthecat.coffeebot.listeners.*;
import xyz.kazuthecat.coffeebot.commands.*;
import xyz.kazuthecat.coffeebot.commands.setcommands.*;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.Scanner;

/* The project requirements say we need to have a menu system when the program starts.
*  This is not how I want to design my program, as such I have this alternative Main
*  where this functionality is implemented.
*
*  There aren't any real use-cases for this, it's just to demonstrate how the bot
*  could conceivably be started using a menu system.
*/
public class AltMain {
    public static void main(String[] args) throws LoginException {
        // CONFIG
        // Menu system does not have support for databases because that would be way too much of a hassle.
        // Token and ownerID is set to null to start with.

        String token = null;
        String ownerID = null;
        OnlineStatus startStatus = OnlineStatus.DO_NOT_DISTURB;
        String activityTxt = "with Java";
        boolean isReady = false;

        // This technically uses the menu system, but reads all input from file
        // that way we don't actually have to input all this stuff.
        Scanner scanner;
        try {
            scanner = new Scanner(new File("predefinedInputs"));
        } catch (Exception e) {
            scanner = new Scanner(System.in);
        }

        // Menu thing
        System.out.println("Configure your bot! \\o/");
        while (token == null || ownerID == null || !isReady) {
            System.out.println("What would you like to do?");
            System.out.println("1) Change the bots token (currently: " + token + ")");
            System.out.println("2) Change the owner ID (currently: " + ownerID + ")");
            System.out.println("3) Change the bot status (currently: " + startStatus + ")");
            System.out.println("4) Change the bot activity (currently: " + activityTxt + ")");
            System.out.println("5) I'm ready to start the bot!");
            String option = scanner.nextLine().strip();

            switch (option) {
                case "1":
                    System.out.println("Input your token then:");
                    token = getInput(scanner, token);
                    System.out.println();
                    break;
                case "2":
                    System.out.println("Input your owner ID then:");
                    ownerID = getInput(scanner, ownerID);
                    System.out.println();
                    break;
                case "3":
                    startStatus = setOnlineStatus(scanner, startStatus);
                    break;
                case "4":
                    System.out.println("Input an activity then:");
                    activityTxt = getInput(scanner, activityTxt);
                    System.out.println();
                    break;
                case "5":
                    if (token != null && ownerID != null) {
                        isReady = true;
                    } else {
                        System.out.println("Token and owner ID can not be null, set those values first.\n");
                    }
                    break;
                default:
                    System.out.println("That's not a valid option!\n");
            }

        }

        // BUILD CLIENT
        // Setting some client options.
        CommandClientBuilder client = new CommandClientBuilder();
        client.setOwnerId(ownerID);                         // loaded from configFile above.
        client.setPrefix("?");                              // ? because we never know if it will work
        client.setStatus(startStatus);                      // The initial status of the bot
        client.setActivity(Activity.playing(activityTxt));  // The initial activity of the bot
        SettingsFile settings = new SettingsFile();         // The object in which we'll hold our setting

        // EventWaiter is required for certain functions where we await some specific event.
        // For example when we want to reply to a user or something.
        EventWaiter eventWaiter = new EventWaiter();
        ListenerAdapter helloListener = new HelloListener(eventWaiter, settings);


        /* JDA Utilities uses emoji reacts for success/warning/error messages. These are specified here.
         * Success: Coffee/Hot beverage. Warning: Fire engine. Error: Fire
         * Visibility may vary because intellij is really finicky with emoji. Probably something with encoding. */
        client.setEmojis("☕", "\uD83D\uDE92", "\uD83D\uDD25");

        // ADD COMMANDS
        client.addCommands(
                // Demo functions
                // These have lots of comments to explain certain behaviours.
                new PingCommand(settings),

                // Settings functions
                new SetCommand(settings), new UnSetCommand(settings),
                new SetGuildCommand(settings), new UnSetGuildCommand(settings),
                new SetOwnerCommand(settings), new UnSetOwnerCommand(settings),
                new SetListCommand(settings)

                // Other commands
                // There's nothing here yet... :(
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

    private static String getInput(Scanner scanner, String currentValue) {
        String input = scanner.nextLine().strip();
        if (input.isBlank()) {
            return currentValue;
        } else {
            return input;
        }
    }

    private static OnlineStatus setOnlineStatus(Scanner scanner, OnlineStatus startStatus) {
        System.out.println("Here are the available online statuses! Pick one");
        System.out.println("   DO_NOT_DISTURB");
        System.out.println("   IDLE");
        System.out.println("   ONLINE");
        System.out.println("   INVISIBLE");
        String input = scanner.nextLine().strip().toLowerCase();

        switch (input) {
            case "do_not_disturb": return OnlineStatus.DO_NOT_DISTURB;
            case "idle": return OnlineStatus.IDLE;
            case "online": return OnlineStatus.ONLINE;
            case "invisible": return OnlineStatus.INVISIBLE;
            default:
                System.out.println("Invalid option, let's just keep it at " + startStatus + " then.");
                return startStatus;
        }

    }
}
