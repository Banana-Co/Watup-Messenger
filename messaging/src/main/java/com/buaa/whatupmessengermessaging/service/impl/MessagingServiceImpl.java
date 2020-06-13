package com.buaa.whatupmessengermessaging.service.impl;

import com.buaa.whatupmessengermessaging.exception.ForbiddenException;
import com.buaa.whatupmessengermessaging.model.Group;
import com.buaa.whatupmessengermessaging.model.GroupMessage;
import com.buaa.whatupmessengermessaging.model.Message;
import com.buaa.whatupmessengermessaging.model.Tuple;
import com.buaa.whatupmessengermessaging.service.FriendService;
import com.buaa.whatupmessengermessaging.service.GroupService;
import com.buaa.whatupmessengermessaging.service.MessagingService;
import com.buaa.whatupmessengermessaging.websocket.MessagingSession;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Map<Tuple, List<Message>> getMessagesAsc(String receiverId) {
//        Aggregation aggregation = Aggregation.newAggregation(
//                Aggregation.match(Criteria.where("_id").is(receiverId)),
//                Aggregation.unwind("messages"),
//                Aggregation.project()
//                        .and("messages.type").as("type")
//                        .and("messages.senderId").as("senderId")
//                        .and("messages.receiverId").as("receiverId")
//                        .and("messages.content").as("content")
//                        .and("messages.timestamp").as("timestamp"),
//                Aggregation.sort(Sort.Direction.ASC, "timestamp"),
//                Aggregation.group("senderId")
//                        .last("senderId").as("senderId")
//                        .push("$$ROOT").as("messages"));
//        AggregationResults<SavedMessage> results = mongoTemplate.aggregate(aggregation, "messages", SavedMessage.class);
        Criteria criteria = Criteria.where("receiverId").is(receiverId);
        List<Message> messages = mongoTemplate.find(Query.query(criteria), Message.class, "messages");
        return messages.parallelStream()
                .sorted(Comparator.comparing(Message::getTimestamp))
                .collect(Collectors.groupingBy(message -> new Tuple(message.getSenderId(), message.getType())));
    }

    @Override
    public Map<Tuple, List<Message>> getMessagesDesc(String receiverId) {
//        Aggregation aggregation = Aggregation.newAggregation(
//                Aggregation.match(Criteria.where("_id").is(receiverId)),
//                Aggregation.unwind("messages"),
//                Aggregation.project()
//                        .and("messages.type").as("type")
//                        .and("messages.senderId").as("senderId")
//                        .and("messages.receiverId").as("receiverId")
//                        .and("messages.content").as("content")
//                        .and("messages.timestamp").as("timestamp"),
//                Aggregation.sort(Sort.Direction.DESC, "timestamp"),
//                Aggregation.group("senderId")
//                        .last("senderId").as("senderId")
//                        .push("$$ROOT").as("messages"));
//        AggregationResults<SavedMessage> results = mongoTemplate.aggregate(aggregation, "messages", SavedMessage.class);
//        return results.getMappedResults();
        Criteria criteria = Criteria.where("receiverId").is(receiverId);
        List<Message> messages = mongoTemplate.find(Query.query(criteria), Message.class, "messages");
        return messages.parallelStream()
                .sorted(Comparator.comparing(Message::getTimestamp).reversed())
                .collect(Collectors.groupingBy(message -> new Tuple(message.getSenderId(), message.getType())));
    }

    @Override
    public Map<Tuple, List<GroupMessage>> getGroupMessagesAsc(String receiverId) {
//        Aggregation aggregation = Aggregation.newAggregation(
//                Aggregation.match(Criteria.where("_id").is(receiverId)),
//                Aggregation.unwind("messages"),
//                Aggregation.project()
//                        .and("messages.type").as("type")
//                        .and("messages.senderId").as("senderId")
//                        .and("messages.groupId").as("groupId")
//                        .and("messages.content").as("content")
//                        .and("messages.timestamp").as("timestamp"),
//                Aggregation.sort(Sort.Direction.ASC, "timestamp"),
//                Aggregation.group("groupId")
//                        .last("groupId").as("groupId")
//                        .push("$$ROOT").as("messages"));
//        AggregationResults<SavedGroupMessage> results = mongoTemplate.aggregate(aggregation, "groupmessages", SavedGroupMessage.class);
//        return results.getMappedResults();
        Criteria criteria = Criteria.where("receiverId").is(receiverId);
        List<GroupMessage> messages = mongoTemplate.find(Query.query(criteria), GroupMessage.class, "groupmessages");
        return messages.parallelStream()
                .sorted(Comparator.comparing(GroupMessage::getTimestamp))
                .collect(Collectors.groupingBy(message -> new Tuple(message.getGroupId(), message.getType())));
    }

    @Override
    public Map<Tuple, List<GroupMessage>> getGroupMessagesDesc(String receiverId) {
//        Aggregation aggregation = Aggregation.newAggregation(
//                Aggregation.match(Criteria.where("_id").is(receiverId)),
//                Aggregation.unwind("messages"),
//                Aggregation.project()
//                        .and("messages.type").as("type")
//                        .and("messages.senderId").as("senderId")
//                        .and("messages.groupId").as("groupId")
//                        .and("messages.content").as("content")
//                        .and("messages.timestamp").as("timestamp"),
//                Aggregation.sort(Sort.Direction.DESC, "timestamp"),
//                Aggregation.group("groupId")
//                        .last("groupId").as("groupId")
//                        .push("$$ROOT").as("messages"));
//        AggregationResults<SavedGroupMessage> results = mongoTemplate.aggregate(aggregation, "groupmessages", SavedGroupMessage.class);
//        return results.getMappedResults();
        Criteria criteria = Criteria.where("receiverId").is(receiverId);
        List<GroupMessage> messages = mongoTemplate.find(Query.query(criteria), GroupMessage.class, "groupmessages");
        return messages.parallelStream()
                .sorted(Comparator.comparing(GroupMessage::getTimestamp).reversed())
                .collect(Collectors.groupingBy(message -> new Tuple(message.getGroupId(), message.getType())));
    }

    @Override
    public void dropMessages(String receiverId) {
//        Criteria criteria = Criteria.where("_id").is(receiverId);
//        mongoTemplate.remove(Query.query(criteria), "messages");
        Criteria criteria = Criteria.where("receiverId").is(receiverId);
        mongoTemplate.remove(Query.query(criteria), "messages");
    }

    @Override
    public void dropGroupMessages(String receiverId) {
//        Criteria criteria = Criteria.where("_id").is(receiverId);
//        mongoTemplate.remove(Query.query(criteria), "groupmessages");
        Criteria criteria = Criteria.where("receiverId").is(receiverId);
        mongoTemplate.remove(Query.query(criteria), "groupmessages");
    }

    @Override
    public void saveMessage(Message message) {
//        Criteria criteria = Criteria.where("_id").is(receiverId);
//        Update update = new Update().push("messages", message);
//        UpdateResult updateResult = mongoTemplate.updateFirst(Query.query(criteria), update, SavedMessage.class, "messages");
//
//        if (updateResult.getModifiedCount() == 0) {
//            List<Message> messages = new ArrayList<>();
//            messages.add(message);
//            mongoTemplate.save(new SavedMessage(receiverId, messages), "messages");
//        }
        mongoTemplate.save(message, "messages");
    }

    @Override
    public void saveGroupMessage(GroupMessage message) {
//        Criteria criteria = Criteria.where("_id").is(receiverId);
//        Update update = new Update().push("messages", message);
//        UpdateResult updateResult = mongoTemplate.updateFirst(Query.query(criteria), update, SavedGroupMessage.class, "groupmessages");
//
//        if (updateResult.getModifiedCount() == 0) {
//            List<GroupMessage> messages = new ArrayList<>();
//            messages.add(message);
//            mongoTemplate.save(new SavedGroupMessage(receiverId, messages), "groupmessages");
//        }
        mongoTemplate.save(message, "groupmessages");
    }

    @Override
    public void sendNotification(String receiverId, Object msg) {
        sendOneNotification(receiverId, msg);
    }

    @Override
    public void sendGroupNotification(String groupId, Object msg) {
        Optional<Group> group = groupService.getGroup(groupId);

        if (group.isPresent()) {
            for (String receiverId : group.get().getUsersId()) {
                sendOneNotification(receiverId, msg);
            }
        } else {
            throw new ForbiddenException("No such group, or you don't have access to the group.");
        }
    }

    @Override
    public void sendAllNotification(String userId, Object msg) {
        List<String> friends = friendService.getFriendsSimple(userId);
        for (String receiverId : friends) {
            sendOneNotification(receiverId, msg);
        }
    }

    private void sendOneNotification(String receiverId, Object msg) {
        Optional<ChannelHandlerContext> ctx = MessagingSession.getChannelHandlerContext(receiverId);
        ctx.ifPresent(channelHandlerContext -> channelHandlerContext.writeAndFlush(msg));
    }
}
