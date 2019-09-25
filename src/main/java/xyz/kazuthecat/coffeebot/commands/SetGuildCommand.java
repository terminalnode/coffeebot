package xyz.kazuthecat.coffeebot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;
import xyz.kazuthecat.coffeebot.settings.SettingEnum;
import xyz.kazuthecat.coffeebot.settings.Settings;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SetGuildCommand extends Command {
    private final Settings settings;

    public SetGuildCommand(Settings settings) {
        this.name = "setserver";
        this.help = "sets a setting for the current guild/server";
        this.aliases = new String[]{"guildset","setguild","serverset"};
        this.userPermissions = new Permission[]{Permission.ADMINISTRATOR};
        this.arguments = "settingName value";
        this.guildOnly = true;

        // Set settings
        this.settings = settings;
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] arglist = event.getArgs().split(" ");
        String settingName = arglist[0];
        String value = Arrays.stream(arglist).skip(1).collect(Collectors.joining(" "));
        SettingEnum settingStatus = settings.putGuildSetting(event.getGuild(), settingName, value);

        String reply;
        switch (settingStatus) {
            case SUCCCESSFUL:
                reply = " The server settings have been updated!"; break;
            case DOESNOTEXIST:
                reply = " There is no setting by the name of **" + settingName + "**, check your spelling or something idk."; break;
            case FORBIDDEN:
                reply = " That setting can not be changed at a server level."; break;
            default:
                // Includes SettingEnum.ERROR
                reply = " Something went wrong. Not sure what. Not sure I care."; break;
        }
        event.getChannel().sendMessage(event.getAuthor().getAsMention() + reply).queue();
    }
}
