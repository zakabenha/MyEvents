package com.emsi.myevents.Model;

import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {
    public String id;
    public String email;
    public String firstName;
    public String lastName;
    public String nickName;
   public Map<String, Organisateur> following;

    /**
     * Constructor with no arguments for firebase query
     */
    public User() {
    }

    /**
     * Minimum default values for user
     * @param email
     * @param firstName
     * @param lastName
     */
    public User(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.following = new HashMap<>();
    }

    /**
     * Define how data will be stored in database
     */
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("firstname", firstName);
        result.put("lastname", lastName);
        result.put("email", email);
        result.put("nickname", nickName);
        result.put("followed", following);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("User : ");
        sb.append("firstname= ").append(firstName).append("\n");
        sb.append("lastname= ").append(lastName).append("\n");
        sb.append("email= ").append(email).append("\n");
        return sb.toString();
    }
}
