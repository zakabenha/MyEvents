package com.emsi.myevents.Model;
import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Organisateur{
    public String id;
    public String name;
    public String description;
    public String logo;
    public Date startSubscription;
    public Date endSubscription;
    public String president;
    //public Boolean followed = false;

    public Organisateur(String name, String university, String description, String president, String logo) {
        this.name = name;
        this.description = description;
        this.president = president;
        this.logo = logo;
    }

    @Exclude
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("clear_name", name.trim().toLowerCase().replaceAll(" ", ""));
        result.put("description", description);
        result.put("logo", logo);
        result.put("start", startSubscription);
        result.put("end", endSubscription);
        result.put("president", president);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name= ").append(name).append("\n");
        sb.append("description= ").append(description).append("\n");
        sb.append("logo= ").append(logo).append("\n");
        sb.append("president= ").append(president).append("\n");
        return sb.toString();
    }

}
