package xyz.kazuthecat.coffeebot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
/* This function is a sort of demo function where all the various parts of the command
 * class and important syntax stuff (mostly lambda functions) are explained for future reference. */

// extends Command is, of course, what makes us able to instantiate this as a command
public class Ping extends Command {
    public Ping() {
        /* this.name/help/guildOnly are values used for various functions and for the
           most part self explanatory. guildOnly makes it not work in DMs, help is
           printed in the help menu, name is the name used in help menus and when
           invoking the command (we can also set aliases) */
        this.name = "ping";
        this.help = "pings the bot";
        this.guildOnly = false;
    }

    /* The execute function is part of the Command class and entails the events
     * that occur when the command is triggered. */
    @Override
    protected void execute(CommandEvent event) {
        long time = System.currentTimeMillis();
        // event.getChannel().sendMessage() returns a MessageAction object.
        // MessageAction.queue() is what actually sends the message to Discord.
        // response.editMessageFormat() also returns a MessageAction object.

        event.getChannel().sendMessage("Pong!")
                .queue(
                        response -> response.editMessageFormat(
                                "Pong! %d ms", (System.currentTimeMillis() - time)).queue()
                );
    }
}
