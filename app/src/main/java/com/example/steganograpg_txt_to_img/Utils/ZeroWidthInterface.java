package com.example.steganograpg_txt_to_img.Utils;

public interface ZeroWidthInterface {
    public String encode(String message, String secret, String key);

    public String decode(String encodedMessage, String key);

}
