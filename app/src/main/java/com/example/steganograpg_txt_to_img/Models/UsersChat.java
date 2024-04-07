package com.example.steganograpg_txt_to_img.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsersChat {

    private String id;
    private List<ChatMessage> chatMessages;
    private List<User> users;

    public UsersChat(User user1, User user2) {
        this.id = UUID.randomUUID().toString();
        this.chatMessages = new ArrayList<>();
        this.users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
