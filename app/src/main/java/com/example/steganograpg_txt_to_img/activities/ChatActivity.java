package com.example.steganograpg_txt_to_img.activities;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.steganograpg_txt_to_img.Models.ChatMessage;
import com.example.steganograpg_txt_to_img.Models.User;
import com.example.steganograpg_txt_to_img.Utils.Constants;
import com.example.steganograpg_txt_to_img.Utils.ZeroWidthInterface;
import com.example.steganograpg_txt_to_img.Utils.ZeroWidthUtil;
import com.example.steganograpg_txt_to_img.databinding.ActivityChatBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private String chatId;
    private ChatAdapter chatAdapter;
    private ChatViewModel chatViewModel;
    private ZeroWidthInterface zeroWidth;
    private Observer<ArrayList<ChatMessage>> observer = new Observer<ArrayList<ChatMessage>>() {

        @Override
        public void onChanged(ArrayList<ChatMessage> chatMessages) {
            chatAdapter.updateMessages(chatMessages);
            if (chatAdapter.getItemCount() > 0) {
                binding.chatLSTChatLst.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadReceivedDetails();
        initViews();
        setListeners();
        setCallbacks();
    }

    private void setCallbacks() {
        chatAdapter.setChatCallback(new ChatCallback() {
            @Override
            public String decodeClicked(ChatMessage chatMessage,int position) {
                String key =  binding.chatETKeyInput.getText().toString();
                if(key.length() != 16){
                    return "";
                }
                binding.chatETKeyInput.setText(null);
                return zeroWidth.decode(chatMessage.getMessage(),key);
            }
        });
    }

    private void initViews() {
        Log.d("initViews", "Entered initViews");
        chatViewModel = new ChatViewModel(chatId);
        chatViewModel.getChatMessages().observe(this, observer);
        chatAdapter = new ChatAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        binding.chatLSTChatLst.setLayoutManager(linearLayoutManager);
        binding.chatLSTChatLst.setAdapter(chatAdapter);
        zeroWidth = new ZeroWidthUtil();
    }

    /**
     * initializing all listeners of this activity
     */
    private void setListeners() {
        Log.d("setListeners", "Entered setListeners");
        binding.chatBTNBack.setOnClickListener(view -> changeToMainActivity());
        binding.chatFABSend.setOnClickListener(view -> sendMessage());
    }

    private void sendMessage() {
        Log.d("sendMessage", "Entered sendMessage");
        String msg = binding.chatETMsgInput.getText().toString();
        String fakeMsg = binding.chatETFakeMsg.getText().toString();
        String key = binding.chatETKeyInput.getText().toString();
        Log.d("sendMessage", "message is: " + msg);
        if (!msg.isEmpty()) {
            ChatMessage chatMessage;
            if(binding.chatCBEncrypt.isChecked()){
                if(fakeMsg.length() == 0){
                    Toast.makeText(this, "you must include fake message", Toast.LENGTH_LONG).show();
                    return;
                } else if (key.length() != 16) {
                    Toast.makeText(this, "key must be 16 characters", Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    String finalMessage = zeroWidth.encode(fakeMsg,msg,key);
                    if(finalMessage != null){
                        chatMessage = new ChatMessage(User.getInstance().getUid(),
                                User.getInstance().getName(),
                                finalMessage,
                                getReadableDateTime(new Date()),
                                true);
                    }
                    else{
                        Toast.makeText(this, "encryption failed", Toast.LENGTH_LONG).show();
                        chatMessage = null;
                    }
                }
            }
            else{
                chatMessage = new ChatMessage(User.getInstance().getUid(),
                        User.getInstance().getName(),
                        msg,
                        getReadableDateTime(new Date()),
                        false);
            }
            if(chatMessage != null)
                chatViewModel.updateChat(chatId, chatMessage, chatAdapter.getChatMessages().size() + "");
        }
        binding.chatETMsgInput.setText(null);
        binding.chatETKeyInput.setText(null);
        binding.chatETFakeMsg.setText(null);
        binding.chatCBEncrypt.setChecked(false);
    }



    private void changeToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    /**
     * Loading group chat's name for title and id for updating DB.
     **/
    private void loadReceivedDetails() {
        Log.d("setListeners", "Entered loadReceivedDetails");
        Intent previousIntent = getIntent();
        chatId = previousIntent.getStringExtra(Constants.KEY_CHAT_ID);
        String groupName = previousIntent.getStringExtra(Constants.KEY_CHAT_NAME);
        binding.chatTVUserName.setText(groupName);
    }


    /**
     * gets Date object and transform it to a readable format.
     *
     * @param date Date
     * @return date string
     */
    private String getReadableDateTime(Date date) {
        return new SimpleDateFormat("dd MMMM, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}