package com.example.steganograpg_txt_to_img.activities;

import com.example.steganograpg_txt_to_img.Models.ChatMessage;

public interface ChatCallback {
    String decodeClicked(ChatMessage chatMessage, int adapterPosition);
}
