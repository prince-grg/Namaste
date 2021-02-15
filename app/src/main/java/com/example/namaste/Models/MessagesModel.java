package com.example.namaste.Models;

public class MessagesModel {

    String iId,message,messageId;
    Long timestamp;

    public MessagesModel(String iId, String message, Long timestamp) {
        this.iId = iId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public MessagesModel(String iId, String message) {
        this.iId = iId;
        this.message = message;
    }
    public MessagesModel(){}

    public String getiId() {
        return iId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setiId(String iId) {
        this.iId = iId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
