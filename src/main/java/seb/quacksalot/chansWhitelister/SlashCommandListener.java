package seb.quacksalot.chansWhitelister;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class SlashCommandListener extends ListenerAdapter {
    private final JavaPlugin plugin;
    public SlashCommandListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "register" -> {
                String message = "Unknown error occurred! Please contact an administrator.";
                String username = Objects.requireNonNull(event.getOption("username")).getAsString();
                switch(Whitelist.getInstance(plugin).whitelistUserWithReturnValues(username)){
                    case 0 -> { message = "User " + username + " successfully whitelisted!"; }
                    case -1 -> { message = "User" + username + " not be found!"; }
                    case -2 -> { message = "User " + username + " is already whitelisted!"; }
                    case -3 -> { message = "Error -3 occurred! Please contact an administrator."; }
                }
                event.reply(message).setEphemeral(true).queue();
            }
            default -> { return; }
        }
    }
}
