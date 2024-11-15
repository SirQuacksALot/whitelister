package seb.quacksalot.chansWhitelister;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChansWhitelister extends JavaPlugin {
    private FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        prepConfig();
        if(config.getBoolean("bot_enabled")) DiscordBot.getInstance(this).run();
    }

    @Override
    public void onDisable() {
        DiscordBot.getInstance(this).shutdown();
    }

    private void prepConfig(){
        this.getLogger().info("Initializing Default Config...");
        config.addDefault("bot_enabled", false);
        config.addDefault("bot_token", "TOKEN");
        config.addDefault("username", "user");
        config.addDefault("password", "password");
        config.addDefault("database", "database");
        config.addDefault("host", "server.example");
        config.addDefault("port", 1234);
        config.addDefault("dbms", "mysql");
        config.options().copyDefaults(true);
        saveConfig();
    }

}