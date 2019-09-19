package xyz.kazuthecat.coffeebot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Ping extends Command {
    public Ping() {
        this.name = "ping";
        this.help = "pings the bot";
        this.guildOnly = false;
    }

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

        event.getChannel().sendMessage("hehe").queue();

        /*
        * message = await channel.send("text")
        * await message.edit("hehe")
        * */
    }
}
