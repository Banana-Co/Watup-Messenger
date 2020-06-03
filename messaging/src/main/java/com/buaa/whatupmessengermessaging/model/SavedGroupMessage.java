package com.buaa.whatupmessengermessaging.model;

import org.springframework.data.annotation.Id;

import java.util.List;

public class SavedGroupMessage {
    @Id
    String id;
    List<GroupMessage> messages;

    public SavedGroupMessage(String id, List<GroupMessage> messages) {
        this.id = id;
        this.messages = messages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<GroupMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<GroupMessage> messages) {
        this.messages = messages;
    }
}
