package seb.quacksalot.chansWhitelister;

import org.json.simple.JSONObject;

public class WhitelistUser {
    public String username;
    public String uuid;

    WhitelistUser(String username, String uuid) {
        this.username = username;
        this.uuid = uuid;
    }

    public JSONObject toJson(){
        JSONObject obj = new JSONObject();
        obj.put("name", username);
        obj.put("uuid", uuid);
        return obj;
    }
}
