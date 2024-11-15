package seb.quacksalot.chansWhitelister;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

public class DiscordBot {
    private static DiscordBot instance = null;
    private JavaPlugin plugin;
    private FileConfiguration config;
    private JDA jda;

    public static DiscordBot getInstance(JavaPlugin plugin) {
        if(instance == null){
            instance = new DiscordBot(plugin);
        }
        return instance;
    }

    public DiscordBot(JavaPlugin plugin){
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public void run(){
        plugin.getLogger().info("Starting Discord Bot...");
        jda = JDABuilder.createLight(config.getString("bot_token"), Collections.emptyList()).
                addEventListeners(new SlashCommandListener(this.plugin))
                .build();

        CommandListUpdateAction commands = jda.updateCommands();

        commands.addCommands(
                Commands.slash("register", "registers minecraft user to server whitelist")
                        .addOption(OptionType.STRING, "username", "Minecraft Username", true)   // Accepting a user input
                        .setGuildOnly(true)                                                                             // this doesn't make sense in DMs
                        .setDefaultPermissions(DefaultMemberPermissions.ENABLED)
        );

        plugin.getLogger().info("Discord Bot started and is awaiting commands!");
        commands.queue();

    }

    public void shutdown(){
        jda.shutdown();
    }
}

