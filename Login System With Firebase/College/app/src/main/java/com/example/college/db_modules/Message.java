package com.example.college.db_modules;

import java.util.Date;

public class Message {
    private String messageText;
    private String messageUser;
    private long messageTime;

    public Message(){}

    public Message(String messageText, String messageUser, long messageTime) {
        this.messageText = messageText;
        this.messageUser = messageUser;

        // Initialize to current time
        this.messageTime = new Date().getTime();
    }



    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public static String getSimpleName(){
        return Message.class.getSimpleName();
    }

}
