package xyz.kazuthecat.coffeebot.commands.setcommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import xyz.kazuthecat.coffeebot.settings.SettingEnum;
import xyz.kazuthecat.coffeebot.settings.Settings;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SetCommand extends Command {
    private final Settings settings;

    public SetCommand(Settings settings) {
        this.name = "set";
        this.help = "sets a setting for the current user";
        this.arguments = "settingName value";
        this.guildOnly = false;
        this.category = new Category("Settings");

        // Set settings
        this.settings = settings;
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] arglist = event.getArgs().split(" ");
        String settingName = arglist[0];
        String value = Arrays.stream(arglist).skip(1).collect(Collectors.joining(" "));
        String reply;

        if (value.isBlank()) {
            reply = " You need to specify a setting *and* a value for that setting, DOLT!";
        } else {
            SettingEnum settingStatus = settings.putUserSetting(event.getAuthor(), settingName, value);
            switch (settingStatus) {
                case SUCCCESSFUL:
                    reply = " Your settings have been updated!"; break;
                case DOESNOTEXIST:
                    reply = " There is no setting by the name of **" + settingName + "**, check your spelling or something idk."; break;
                case FORBIDDEN:
                    reply = " That setting can not be changed at a user level."; break;
                default:
                    // Includes SettingEnum.ERROR
                    reply = " Something went wrong. Not sure what. Not sure I care."; break;
            }
        }

        event.getChannel().sendMessage(event.getAuthor().getAsMention() + reply).queue();
    }
}
