package com.example.steganograpg_txt_to_img.Utils;

public class ZeroWidthUtil implements ZeroWidthInterface{

    public ZeroWidthUtil() {
    }

    public String encode(String fakeMsg, String hidden, String key) {
        String encryptedMessaged, encryptedInputBinary, hiddenMessage, encodedText;
        encryptedMessaged = AESUtil.encrypt(hidden, key);
        if (encryptedMessaged == null)
            return null;
        encryptedInputBinary = stringToBinaryString(encryptedMessaged);
        hiddenMessage = binaryToZeroWidthString(encryptedInputBinary);
        encodedText = zeroWidthStringInMessage(fakeMsg, hiddenMessage);
        return encodedText;
    }

    private String stringToBinaryString(String string) {
        byte[] bytes = string.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            binary.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        return binary.toString();
    }

    private String binaryToZeroWidthString(String binaryString) {
        int secretBinaryLength = binaryString.length();
        StringBuilder zeroWidthBinaryMessage = new StringBuilder();
        for (int i = 0; i < secretBinaryLength; i++) {
            if (binaryString.charAt(i) == Constants.ZERO)
                zeroWidthBinaryMessage.append(Constants.ZWNJ);//0 char
            else if (binaryString.charAt(i) == Constants.ONE)
                zeroWidthBinaryMessage.append(Constants.ZWJ);//1 char
            else if (binaryString.charAt(i) == Constants.SPACE)
                zeroWidthBinaryMessage.append(Constants.ZERO_WIDTH_SPACE);//space char
        }
        return zeroWidthBinaryMessage.toString();
    }

    private String zeroWidthStringInMessage(String fakeMsg, String hiddenMessage) {
        String encodedMessage = String.valueOf(fakeMsg.charAt(0));
        encodedMessage += hiddenMessage;
        encodedMessage += (fakeMsg.substring(1));
        return encodedMessage;
    }

    public String decode(String encodedMessage, String key) {
        String hiddenMessage, encryptedBinaryMessage, encryptedMessage, decodedText;
        hiddenMessage = getZeroWidthStringFromMessage(encodedMessage);
        encryptedBinaryMessage = zeroWidthToBinaryString(hiddenMessage);
        encryptedMessage = binaryToTextString(encryptedBinaryMessage);
        decodedText = AESUtil.decrypt(encryptedMessage.getBytes(), key);
        return decodedText;
    }

    private String getZeroWidthStringFromMessage(String message) {
        StringBuilder zwString = new StringBuilder();
        int messageLen = message.length();
        for (int i = 0; i < messageLen; i++) {
            char currentChar = message.charAt(i);
            if (currentChar == Constants.ZWNJ)
                zwString.append(Constants.ZWNJ);
            if (currentChar == Constants.ZWJ)
                zwString.append(Constants.ZWJ);
            if (currentChar == Constants.ZERO_WIDTH_SPACE)
                zwString.append(Constants.ZERO_WIDTH_SPACE);
        }
        return zwString.toString();
    }

    private String zeroWidthToBinaryString(String binaryString) {
        int secretBinaryLength = binaryString.length();
        StringBuilder binaryMessage = new StringBuilder();
        for (int i = 0; i < secretBinaryLength; i++) {
            char currentChar = binaryString.charAt(i);
            if (currentChar == Constants.ZWNJ)
                binaryMessage.append(Constants.ZERO);//0 char
            else if (currentChar == Constants.ZWJ)
                binaryMessage.append(Constants.ONE);//1 char
            else if (currentChar == Constants.ZERO_WIDTH_SPACE)
                binaryMessage.append(Constants.SPACE);//space char
        }
        return binaryMessage.toString();
    }

    private String binaryToTextString(String binaryMessage) {
        StringBuilder textString  = new StringBuilder();
        int messageLen = binaryMessage.length();
        for (int i = 0; i < messageLen; i += 8) {
            if (binaryMessage.charAt(i) == Constants.SPACE)
                i++;
            if (i + 8 > messageLen)
                break;
            String binaryChar = binaryMessage.substring(i, i + 8);
            int asciiValue = Integer.parseInt(binaryChar, 2);
            textString.append((char) asciiValue);
        }
        return textString.toString();
    }
}