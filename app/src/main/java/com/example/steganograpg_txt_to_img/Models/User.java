package com.example.steganograpg_txt_to_img.Models;

import com.example.steganograpg_txt_to_img.Utils.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class User {

    private static User instance = null;
    private String name;
    private String uid;
    private HashMap<String,String> chats;


    public User() {
    }

    public User(String name, String uid) {
        this.name = name;
        this.uid = uid;
        this.chats = new HashMap<>();
    }

    public static void init(String name, String uid) {
        if (instance == null)
            instance = new User(name, uid);
    }


    public void setUser(User currentUser) {
        this.name = currentUser.getName();
        this.uid = currentUser.getUid();
        this.chats = currentUser.getChats();
    }

    public static User getInstance() {
        if(instance == null) {
            throw new AssertionError("You have to call init first");
        }
        return instance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public HashMap<String, String> getChats() {
        return chats;
    }

    public void setChats(HashMap<String, String> chats) {
        this.chats = chats;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", uid='" + uid + '\'' +
                ", chats = " + chats +
                '}';
    }
}
