package xyz.kazuthecat.coffeebot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.User;
import xyz.kazuthecat.coffeebot.databases.DBEnum;
import xyz.kazuthecat.coffeebot.databases.DBHandler;

import java.util.ArrayList;
import java.util.List;

public class CoffeeLog extends Command {
    private final DBHandler dbHandler;
    private final List<User> initalizedUsers;

    public CoffeeLog(DBHandler dbHandler) {
        this.name = "coffeelog";
        this.help = "logs a cup of coffee";
        this.guildOnly = false;
        this.dbHandler = dbHandler;
        this.initalizedUsers = new ArrayList<>();

        String coffeelog_tbl = "CREATE TABLE IF NOT EXISTS coffeelog(" +
                "id      BIGINT NOT NULL, " +
                "cups    INT NOT NULL, " +
                "PRIMARY KEY (id) );";

        String coffeetypes_tbl = "CREATE TABLE IF NOT EXISTS coffeetypes(" +
                "id      BIGINT NOT NULL, " +
                "name    VARCHAR(30) NOT NULL, " +
                "cups    INT NOT NULL, " +
                "PRIMARY KEY (id, name) )";

        try {
            dbHandler.execute(new String[]{coffeelog_tbl, coffeetypes_tbl});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] arglist = event.getArgs().split(" ");

        User user = event.getAuthor();
        String uid = user.getId();
        if (!initalizedUsers.contains(user)) {
            dbHandler.execute("INSERT IGNORE INTO coffeelog (id,cups) VALUES (?, 0); ", new String[][]{{uid}});
            initalizedUsers.add(user);
        }

        String reply;
        switch (arglist[0]) {
            case "add":   reply = addCups(arglist, uid); break;
            case "check": reply = checkCups(arglist, uid); break;
            default:
                reply = "That's not a valid subcommand.";
                break;
        }

        event.reply(reply);
    }

    private String addCups(String[] arglist, String uid) {
        String reply;
        String numCups = "1";
        boolean cupOverflow = false;
        if (arglist.length > 1 && arglist[1].matches("\\d+")) {
            numCups = arglist[1];
            if (Integer.parseInt(numCups) > 10) {
                // Max number to avoid the cup number overflowing.
                numCups = "10";
                cupOverflow = true;
            }
        }
        DBEnum result = dbHandler.execute("UPDATE coffeelog SET cups = cups + ? WHERE id = ?;", new String[][]{{numCups, uid}});

        switch (result) {
            case SUCCESS:
                if (cupOverflow){
                    reply = String.format("Success! I have added %s to your cup count! This is the maximum number of cups you can add at one time. :coffee:", numCups);
                } else {
                    reply = String.format("Success! I have added %s to your cup count! :coffee:", numCups);
                }
                break;

            case ERROR:
                reply = "An error occurred, your cup count probably hasn't been updated. But who knows? :shrug:";
                break;

            default:
                reply = "Congratulations, you have caused some kind of undefined behaviour!";
                break;
        }
        return reply;
    }

    private String checkCups(String[] arglist, String uid) {
        // TODO
        return "";
    }
}
