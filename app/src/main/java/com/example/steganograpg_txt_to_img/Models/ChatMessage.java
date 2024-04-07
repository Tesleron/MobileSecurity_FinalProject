package com.example.steganograpg_txt_to_img.Models;

import android.graphics.Bitmap;

public class ChatMessage {
    private String senderId;
    private String senderName;
    private String message;
    private String dateTime;

    private boolean isEncoded;

    public ChatMessage() {
    }

    public ChatMessage(String senderId, String senderName, String message, String dateTime, boolean isEncoded) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.message = message;
        this.dateTime = dateTime;
        this.isEncoded = isEncoded;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isEncoded() {
        return isEncoded;
    }

    public void setEncoded(boolean encoded) {
        isEncoded = encoded;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "senderId='" + senderId + '\'' +
                ", senderName='" + senderName + '\'' +
                ", message='" + message + '\'' +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
