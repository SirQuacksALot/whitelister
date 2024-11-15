package seb.quacksalot.chansWhitelister;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.sisu.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;

public class Whitelist {
    private JavaPlugin plugin;
    private JSONArray whitelist;
    private File whitelistFile;
    private JSONParser parser = new JSONParser();

    private static Whitelist instance = null;

    public static Whitelist getInstance(@Nullable JavaPlugin plugin) {
        if (instance == null) {
            instance = new Whitelist(plugin);
        }
        return instance;
    }

    Whitelist(JavaPlugin plugin) {
        this.plugin = plugin;
        if(prepWhitelist() < 0){
            plugin.getLogger().log(Level.SEVERE, "Could not initialize Whitelist. Shutting off plugin!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    public int whitelistUserWithReturnValues(String username){
        WhitelistUser user = null;
        if((user = getUserData(username)) == null){
            plugin.getLogger().log(Level.INFO, "User " + username + " not found!");
            return -1;
        }
        if(userIsWhitelisted(user)){
            plugin.getLogger().log(Level.INFO, "User " + username + " is already whitelisted!");
            return -2;
        }

        whitelist.add((user.toJson()));

        if(saveWhitelist() < 0){
            plugin.getLogger().log(Level.SEVERE, "Could not save Whitelist. Shutting off plugin!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return -3;
        };

        plugin.getServer().reloadWhitelist();
        plugin.getLogger().log(Level.INFO, "Whitelist reloaded!");

        plugin.getLogger().log(Level.INFO, "User " + username + " successfully whitelisted!");
        return 0;
    }

    public void whitelistUser(String username){
        WhitelistUser user = null;
        if((user = getUserData(username)) == null){
            plugin.getLogger().log(Level.INFO, "User " + username + " not found!");
            return;
        }
        if(userIsWhitelisted(user)){
            plugin.getLogger().log(Level.INFO, "User " + username + " is already whitelisted!");
            return;
        }

        whitelist.add((user.toJson()));

        if(saveWhitelist() < 0){
            plugin.getLogger().log(Level.SEVERE, "Could not save Whitelist. Shutting off plugin!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        };

        plugin.getServer().reloadWhitelist();
        plugin.getLogger().log(Level.INFO, "Whitelist reloaded!");

        plugin.getLogger().log(Level.INFO, "User " + username + " successfully whitelisted!");
    }

    private int prepWhitelist() {
        plugin.getLogger().info("Initializing Whitelist...");
        whitelistFile = new File((plugin.getDataFolder().getAbsolutePath() + "/../../whitelist.json").replace("\\","/"));
        try { whitelist = (JSONArray)(new JSONParser().parse(new FileReader(whitelistFile))); }
        catch (IOException | ParseException e) { return -1; }
        return 0;
    }

    private int saveWhitelist(){
        plugin.getLogger().info("Saving Whitelist...");
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(whitelistFile, whitelist);
        }
        catch (IOException e) { return -1; }
        return 0;
    }

    private WhitelistUser getUserData(String username){
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet("https://playerdb.co/api/player/minecraft/" + username);
        HttpResponse response;
        String result = null;
        WhitelistUser user = null;

        try{
            response = client.execute(request);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);

            JSONObject obj = (JSONObject) parser.parse(result);
            JSONObject data = (JSONObject) obj.get("data");
            JSONObject player = (JSONObject) data.get("player");

            user = new WhitelistUser(player.get("username").toString(), player.get("id").toString());
        }catch (Exception e) { return null; }

        return user;
    }

    private boolean userIsWhitelisted(WhitelistUser user){
        for(Object whitelisteduser : whitelist ){
            WhitelistUser wlu = new WhitelistUser(((JSONObject)whitelisteduser).get("name").toString(), ((JSONObject)whitelisteduser).get("uuid").toString());
            if(wlu.uuid.equals(user.uuid) && wlu.username.equals(user.username)) return true;
        }
        return false;
    }

    public void printWhitelisted(){
        whitelist.forEach((i) -> {
            JSONObject obj = (JSONObject) i;
            plugin.getLogger().info("[DEBUG] " + obj.toJSONString());
        });
    }

}
