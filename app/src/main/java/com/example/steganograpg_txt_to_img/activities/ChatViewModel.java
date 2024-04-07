package com.example.steganograpg_txt_to_img.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.steganograpg_txt_to_img.Models.ChatMessage;
import com.example.steganograpg_txt_to_img.Utils.Constants;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatViewModel {

    private final MutableLiveData<ArrayList<ChatMessage>> mChatMessages;

    public ChatViewModel() {
        mChatMessages = new MutableLiveData<>();
    }

    public ChatViewModel(String chatId){
        this();
        ArrayList<ChatMessage> chatMessages = new ArrayList<>();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = db.getReference().child(Constants.DB_CHATS).child(chatId).child(Constants.DB_CHAT_MESSAGES);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChatMessage newMessage = snapshot.getValue(ChatMessage.class);
                if(newMessage != null){
                    chatMessages.add(newMessage);
                    mChatMessages.setValue(chatMessages);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateChat(String chatId,ChatMessage chatMessage,String index){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = db.getReference(Constants.DB_CHATS);
        databaseReference.child(chatId).child(Constants.DB_CHAT_MESSAGES).child(index).setValue(chatMessage);
    }

    public ArrayList<ChatMessage> getMessagesFromDB(String groupID){
        ArrayList<ChatMessage> chatMessages = new ArrayList<>();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = db.getReference().child(Constants.DB_CHATS).child(groupID).child(Constants.DB_CHAT_MESSAGES);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChatMessage newMessage = snapshot.getValue(ChatMessage.class);
                chatMessages.add(newMessage);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return chatMessages;
    }
    public MutableLiveData<ArrayList<ChatMessage>> getChatMessages(){
        return mChatMessages;
    }

}
