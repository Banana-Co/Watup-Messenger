package com.buaa.whatupmessengermessaging.service.impl;

import com.buaa.whatupmessengermessaging.exception.ForbiddenException;
import com.buaa.whatupmessengermessaging.model.*;
import com.buaa.whatupmessengermessaging.service.FriendService;
import com.buaa.whatupmessengermessaging.service.GroupService;
import com.buaa.whatupmessengermessaging.service.MessagingService;
import com.buaa.whatupmessengermessaging.websocket.MessagingSession;
import com.mongodb.client.result.UpdateResult;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Primary
public class MessagingServiceImpl implements MessagingService {
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    FriendService friendService;
    @Autowired
    GroupService groupService;

    @Override
    public List<SavedMessage> getMessagesAsc(String receiverId) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(receiverId)),
                Aggregation.unwind("messages"),
                Aggregation.project()
                        .and("messages.type").as("type")
                        .and("messages.senderId").as("senderId")
                        .and("messages.receiverId").as("receiverId")
                        .and("messages.content").as("content")
                        .and("messages.timestamp").as("timestamp"),
                Aggregation.sort(Sort.Direction.ASC, "timestamp"),
                Aggregation.group("senderId")
                        .last("senderId").as("senderId")
                        .push("$$ROOT").as("messages"));
        AggregationResults<SavedMessage> results = mongoTemplate.aggregate(aggregation, "messages", SavedMessage.class);
        return results.getMappedResults();
    }

    @Override
    public List<SavedMessage> getMessagesDesc(String receiverId) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(receiverId)),
                Aggregation.unwind("messages"),
                Aggregation.project()
                        .and("messages.type").as("type")
                        .and("messages.senderId").as("senderId")
                        .and("messages.receiverId").as("receiverId")
                        .and("messages.content").as("content")
                        .and("messages.timestamp").as("timestamp"),
                Aggregation.sort(Sort.Direction.DESC, "timestamp"),
                Aggregation.group("senderId")
                        .last("senderId").as("senderId")
                        .push("$$ROOT").as("messages"));
        AggregationResults<SavedMessage> results = mongoTemplate.aggregate(aggregation, "messages", SavedMessage.class);
        return results.getMappedResults();
    }

    @Override
    public List<SavedGroupMessage> getGroupMessagesAsc(String receiverId) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(receiverId)),
                Aggregation.unwind("messages"),
                Aggregation.project()
                        .and("messages.type").as("type")
                        .and("messages.senderId").as("senderId")
                        .and("messages.groupId").as("groupId")
                        .and("messages.content").as("content")
                        .and("messages.timestamp").as("timestamp"),
                Aggregation.sort(Sort.Direction.ASC, "timestamp"),
                Aggregation.group("groupId")
                        .last("groupId").as("groupId")
                        .push("$$ROOT").as("messages"));
        AggregationResults<SavedGroupMessage> results = mongoTemplate.aggregate(aggregation, "groupmessages", SavedGroupMessage.class);
        return results.getMappedResults();
    }

    @Override
    public List<SavedGroupMessage> getGroupMessagesDesc(String receiverId) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(receiverId)),
                Aggregation.unwind("messages"),
                Aggregation.project()
                        .and("messages.type").as("type")
                        .and("messages.senderId").as("senderId")
                        .and("messages.groupId").as("groupId")
                        .and("messages.content").as("content")
                        .and("messages.timestamp").as("timestamp"),
                Aggregation.sort(Sort.Direction.DESC, "timestamp"),
                Aggregation.group("groupId")
                        .last("groupId").as("groupId")
                        .push("$$ROOT").as("messages"));
        AggregationResults<SavedGroupMessage> results = mongoTemplate.aggregate(aggregation, "groupmessages", SavedGroupMessage.class);
        return results.getMappedResults();
    }

    @Override
    public void dropMessages(String receiverId) {
        Criteria criteria = Criteria.where("_id").is(receiverId);
        mongoTemplate.remove(Query.query(criteria), "messages");
    }

    @Override
    public void dropGroupMessages(String receiverId) {
        Criteria criteria = Criteria.where("_id").is(receiverId);
        mongoTemplate.remove(Query.query(criteria), "groupmessages");
    }

    @Override
    public void saveMessage(String receiverId, Message message) {
        Criteria criteria = Criteria.where("_id").is(receiverId);
        Update update = new Update().push("messages", message);
        UpdateResult updateResult = mongoTemplate.updateFirst(Query.query(criteria), update, SavedMessage.class, "messages");

        if (updateResult.getModifiedCount() == 0) {
            List<Message> messages = new ArrayList<>();
            messages.add(message);
            mongoTemplate.save(new SavedMessage(receiverId, messages), "messages");
        }
    }

    @Override
    public void saveGroupMessage(String receiverId, GroupMessage message) {
        Criteria criteria = Criteria.where("_id").is(receiverId);
        Update update = new Update().push("messages", message);
        UpdateResult updateResult = mongoTemplate.updateFirst(Query.query(criteria), update, SavedGroupMessage.class, "groupmessages");

        if (updateResult.getModifiedCount() == 0) {
            List<GroupMessage> messages = new ArrayList<>();
            messages.add(message);
            mongoTemplate.save(new SavedGroupMessage(receiverId, messages), "groupmessages");
        }
    }

    @Override
    public void sendNotification(String token, String receiverId, Object msg) {
        sendOneNotification(token, receiverId, msg);
    }

    @Override
    public void sendGroupNotification(String token, String groupId, Object msg) {
        Optional<Group> group = groupService.getGroup(token, groupId);
        if (group.isPresent()) {
            for (String userId : group.get().getUsersId()) {
                sendOneNotification(token, userId, msg);
            }
        } else {
            throw new ForbiddenException("No such group, or you don't have access to the group.");
        }
    }

    @Override
    public void sendAllNotification(String token, Object msg) {
        List<String> friends = friendService.getFriendsSimple(token);
        for (String userId : friends) {
            sendOneNotification(token, userId, msg);
        }
    }

    private void sendOneNotification(String token, String receiverId, Object msg) {
        Optional<ChannelHandlerContext> ctx = MessagingSession.getChannelHandlerContext(receiverId);
        ctx.ifPresent(channelHandlerContext -> channelHandlerContext.writeAndFlush(msg));
    }
}
