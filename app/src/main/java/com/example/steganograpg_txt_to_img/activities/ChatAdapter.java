package com.example.steganograpg_txt_to_img.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.steganograpg_txt_to_img.Models.ChatMessage;
import com.example.steganograpg_txt_to_img.Models.User;
import com.example.steganograpg_txt_to_img.Utils.Constants;
import com.example.steganograpg_txt_to_img.databinding.ReceivedMessageItemBinding;
import com.example.steganograpg_txt_to_img.databinding.SentMessageItemBinding;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ChatMessage> chatMessages;

    private ChatCallback chatCallback;

    public ChatAdapter() {
        this.chatMessages = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Constants.VIEW_TYPE_SENT) {
            return new SentMessageViewHolder(
                    SentMessageItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                            parent,
                            false)
            );
        } else {
            return new ReceivedMessageViewHolder(
                    ReceivedMessageItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                            parent,
                            false)
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == Constants.VIEW_TYPE_SENT)
            ((SentMessageViewHolder) holder).setData(chatMessages.get(position));
        else
            ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public int getItemViewType(int position) {
        if (chatMessages.get(position).getSenderId().equals(User.getInstance().getUid()))
            return Constants.VIEW_TYPE_SENT;
        else
            return Constants.VIEW_TYPE_RECEIVED;
    }

    public void updateMessages(ArrayList<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
        notifyDataSetChanged();
    }

    public ChatAdapter setChatCallback(ChatCallback chatCallback) {
        this.chatCallback = chatCallback;
        return this;
    }

    public ArrayList<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public ChatMessage getItem(int position) {
        return chatMessages.get(position);
    }

    public class SentMessageViewHolder extends RecyclerView.ViewHolder {

        private final SentMessageItemBinding binding;

        public SentMessageViewHolder(SentMessageItemBinding sentMessageItemBinding) {
            super(sentMessageItemBinding.getRoot());
            this.binding = sentMessageItemBinding;
            binding.sentFabDecode.setOnClickListener(view -> {
                String decoded = chatCallback.decodeClicked(getItem(getAdapterPosition()), getAdapterPosition());
                if (decoded == null){
                    Toast.makeText(this.itemView.getContext(), "Wrong key", Toast.LENGTH_LONG).show();
                }
                else if(decoded.equals(""))
                    Toast.makeText(this.itemView.getContext(), "key must be 16 characters", Toast.LENGTH_LONG).show();
                else{
                    String newMessage = "Fake Message: " +binding.sentMessage.getText() + "\nHidden Message: " + decoded;
                    binding.sentMessage.setText(newMessage);
                    binding.sentFabDecode.setVisibility(View.GONE);
                }
            });
        }

        void setData(ChatMessage chatMessage) {
            binding.sentMessage.setText(chatMessage.getMessage());
            binding.sentDateTime.setText(chatMessage.getDateTime());
            binding.sentName.setText(chatMessage.getSenderName());
            binding.sentFabDecode.setVisibility(View.VISIBLE);
            if(!chatMessage.isEncoded())
                binding.sentFabDecode.setVisibility(View.GONE);
        }
    }

    public class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {

        private final ReceivedMessageItemBinding binding;

        public ReceivedMessageViewHolder(ReceivedMessageItemBinding receivedMessageItemBinding) {
            super(receivedMessageItemBinding.getRoot());
            this.binding = receivedMessageItemBinding;
            binding.receivedFabDecode.setOnClickListener(view -> {
                String decoded = chatCallback.decodeClicked(getItem(getAdapterPosition()), getAdapterPosition());
                if(decoded.equals(""))
                    Toast.makeText(this.itemView.getContext(), "key must be 16 characters", Toast.LENGTH_LONG).show();
                else{
                    String newMessage = "Fake Message: " +binding.receivedMessage.getText() + "\nHidden Message: " + decoded;
                    binding.receivedMessage.setText(newMessage);
                    binding.receivedFabDecode.setVisibility(View.GONE);
                }
            });
        }

        private void setData(ChatMessage chatMessage) {
            binding.receivedMessage.setText(chatMessage.getMessage());
            binding.receivedDateTime.setText(chatMessage.getDateTime());
            binding.receivedName.setText(chatMessage.getSenderName());
            binding.receivedFabDecode.setVisibility(View.VISIBLE);
            if(!chatMessage.isEncoded())
                binding.receivedFabDecode.setVisibility(View.GONE);
        }
    }
}
