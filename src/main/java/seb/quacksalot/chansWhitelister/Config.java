package seb.quacksalot.chansWhitelister;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Config {
    private boolean bot_enabled;
    private String bot_token;

    private String username;
    private String password;
    private String database;
    private String host;
    private int port;
    private String dbms;

    Config() {};

    public void setBot_enabled(boolean bot_enabled) {this.bot_enabled = bot_enabled;}
    public void setBot_token(String bot_token) { this.bot_token = bot_token; }
    public boolean getBot_enabled() { return bot_enabled; }
    public String getBot_token() { return bot_token; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setDatabase(String database) { this.database = database; }
    public void setHost(String host) { this.host = host; }
    public void setPort(int port) { this.port = port; }
    public void setDbms(String dbms) { this.dbms = dbms; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getDatabase() { return database; }
    public String getHost() { return host; }
    public int getPort() { return port; }
    public String getDbms() { return dbms; }

}

