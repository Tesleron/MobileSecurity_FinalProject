package com.example.steganograpg_txt_to_img.activities;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.steganograpg_txt_to_img.Models.User;
import com.example.steganograpg_txt_to_img.Models.UsersChat;
import com.example.steganograpg_txt_to_img.Utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<User>> mUsers;

    public MainViewModel() {
        mUsers = new MutableLiveData<>();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference usersdRef = rootRef.child(Constants.DB_USERS);
        ArrayList<User> array = new ArrayList<>();

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    if(!User.getInstance().getUid().equals(user.getUid())){
                        array.add(user);
                        mUsers.setValue(array);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        usersdRef.addListenerForSingleValueEvent(eventListener);
    }

    public void updateUser(User user){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = db.getReference(Constants.DB_USERS);
        databaseReference.child(user.getUid()).setValue(user);
    }

    public void updateChat(UsersChat usersChat){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = db.getReference(Constants.DB_CHATS);
        databaseReference.child(usersChat.getId()).setValue(usersChat);
    }

    public MutableLiveData<ArrayList<User>> getMUsers() {
        return mUsers;
    }
}