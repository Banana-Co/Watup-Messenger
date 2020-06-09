package com.buaa.whatupmessengermessaging.model;

import org.springframework.data.annotation.Id;

import java.util.List;

public class SavedMessage {
    @Id
    String id;
    List<Message> messages;

    public SavedMessage(String id, List<Message> messages) {
        this.id = id;
        this.messages = messages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
