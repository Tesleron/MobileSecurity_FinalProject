package com.example.steganograpg_txt_to_img.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.steganograpg_txt_to_img.Models.User;
import com.example.steganograpg_txt_to_img.Models.UsersChat;
import com.example.steganograpg_txt_to_img.Utils.Constants;
import com.example.steganograpg_txt_to_img.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private UsersAdapter usersAdapter;
    private MainViewModel mainViewModel;

    private Observer<ArrayList<User>> observer = new Observer<ArrayList<User>>() {

        @Override
        public void onChanged(ArrayList<User> users) {
            usersAdapter.updateGroups(users);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
        setListeners();
        setCallbacks();

    }

    private void setListeners() {
        binding.mainETSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                usersAdapter.filter(binding.mainETSearch.getText().toString());
            }
        });
        binding.mainLogout.setOnClickListener(view -> logout());
    }


    private void initViews() {
        binding.mainTVHello.setText("Hello, " + User.getInstance().getName());
        mainViewModel = new MainViewModel();
        mainViewModel.getMUsers().observe(this, observer);
        usersAdapter = new UsersAdapter(this);
        binding.mainLSTUsers.setLayoutManager(new LinearLayoutManager(this));
        binding.mainLSTUsers.setAdapter(usersAdapter);
    }

    private void setCallbacks() {
        usersAdapter.setUserCallback(new UserCallback() {
            @Override
            public void itemClicked(User user, int position) {
                if(User.getInstance().getChats() == null)
                    User.getInstance().setChats(new HashMap<>());
                if(!User.getInstance().getChats().containsKey(user.getUid())){
                    createNewChat(user);
                }
                changeToChatIntent(user);
            }
        });

    }

    private void changeToChatIntent(User user) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(Constants.KEY_CHAT_ID,User.getInstance().getChats().get(user.getUid()));
        intent.putExtra(Constants.KEY_CHAT_NAME,user.getName());
        startActivity(intent);
        finish();
    }

    private void createNewChat(User user) {
        UsersChat newUserChat = new UsersChat(User.getInstance(), user);
        User.getInstance().getChats().put(user.getUid(),newUserChat.getId());
        user.setChats(new HashMap<>());
        user.getChats().put(User.getInstance().getUid(),newUserChat.getId());
        mainViewModel.updateUser(User.getInstance());
        mainViewModel.updateUser(user);
        mainViewModel.updateChat(newUserChat);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}