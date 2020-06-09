package com.buaa.whatupmessengermessaging.service;

import com.buaa.whatupmessengermessaging.model.GroupMessage;
import com.buaa.whatupmessengermessaging.model.Message;
import com.buaa.whatupmessengermessaging.model.SavedGroupMessage;
import com.buaa.whatupmessengermessaging.model.SavedMessage;

import java.util.List;

public
interface MessagingService {

    List<SavedMessage> getMessagesAsc(String receiverId);
    List<SavedMessage> getMessagesDesc(String receiverId);
    List<SavedGroupMessage> getGroupMessagesAsc(String receiverId);
    List<SavedGroupMessage> getGroupMessagesDesc(String receiverId);

    void dropMessages(String receiverId);
    void dropGroupMessages(String receiverId);

    void saveMessage(String receiverId, Message message);
    void saveGroupMessage(String receiverId, GroupMessage message);

    void sendNotification(String receiverId, Object msg);
    void sendGroupNotification(String groupId, Object msg);

    void sendAllNotification(String userId, Object msg);
}
