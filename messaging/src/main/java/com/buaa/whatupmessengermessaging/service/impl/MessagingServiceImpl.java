package com.buaa.whatupmessengermessaging.service.impl;

import com.buaa.whatupmessengermessaging.exception.ForbiddenException;
import com.buaa.whatupmessengermessaging.model.*;
import com.buaa.whatupmessengermessaging.service.FriendService;
import com.buaa.whatupmessengermessaging.service.GroupService;
import com.buaa.whatupmessengermessaging.service.MessagingService;
import com.buaa.whatupmessengermessaging.websocket.MessagingSession;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
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
    @Autowired
    ObjectMapper mapper;

    @Override
    public Map<Tuple, List<Message>> getMessagesAsc(String receiverId) {
        Criteria criteria = Criteria.where("receiverId").is(receiverId);
        List<Message> messages = mongoTemplate.find(Query.query(criteria), Message.class, "messages");
        return messages.parallelStream()
                .sorted(Comparator.comparing(Message::getTimestamp))
                .collect(Collectors.groupingBy(message -> new Tuple(message.getSenderId(), message.getType())));
    }

    @Override
    public Map<Tuple, List<Message>> getMessagesDesc(String receiverId) {
        Criteria criteria = Criteria.where("receiverId").is(receiverId);
        List<Message> messages = mongoTemplate.find(Query.query(criteria), Message.class, "messages");
        return messages.parallelStream()
                .sorted(Comparator.comparing(Message::getTimestamp).reversed())
                .collect(Collectors.groupingBy(message -> new Tuple(message.getSenderId(), message.getType())));
    }

    @Override
    public Map<Tuple, List<GroupMessage>> getGroupMessagesAsc(String receiverId) {
        Criteria criteria = Criteria.where("receiverId").is(receiverId);
        List<GroupMessage> messages = mongoTemplate.find(Query.query(criteria), GroupMessage.class, "groupmessages");
        return messages.parallelStream()
                .sorted(Comparator.comparing(GroupMessage::getTimestamp))
                .collect(Collectors.groupingBy(message -> new Tuple(message.getGroupId(), message.getType())));
    }

    @Override
    public Map<Tuple, List<GroupMessage>> getGroupMessagesDesc(String receiverId) {
        Criteria criteria = Criteria.where("receiverId").is(receiverId);
        List<GroupMessage> messages = mongoTemplate.find(Query.query(criteria), GroupMessage.class, "groupmessages");
        return messages.parallelStream()
                .sorted(Comparator.comparing(GroupMessage::getTimestamp).reversed())
                .collect(Collectors.groupingBy(message -> new Tuple(message.getGroupId(), message.getType())));
    }

    @Override
    public void dropMessages(String receiverId) {
        Criteria criteria = Criteria.where("receiverId").is(receiverId);
        mongoTemplate.remove(Query.query(criteria), "messages");
    }

    @Override
    public void dropGroupMessages(String receiverId) {
        Criteria criteria = Criteria.where("receiverId").is(receiverId);
        mongoTemplate.remove(Query.query(criteria), "groupmessages");
    }

    @Override
    public void saveMessage(Message message) {
        mongoTemplate.save(message, "messages");
    }

    @Override
    public void saveGroupMessage(GroupMessage message) {
        mongoTemplate.save(message, "groupmessages");
    }

    @Override
    public void sendNotification(String from, String to, Notification msg) {
        if (from == null || !friendService.isBlock(from, to)) {
            sendOneNotification(to, msg);
        }
    }

    @Override
    public void sendGroupNotification(String from, String to, Notification msg) {
        Optional<Group> group = groupService.getGroup(to);

        if (group.isPresent()) {
            for (String receiverId : group.get().getUsersId()) {
                if (from == null || !friendService.isBlock(from, receiverId)) {
                    sendOneNotification(receiverId, msg);
                }
            }
        } else {
            throw new ForbiddenException("No such group, or you don't have access to the group.");
        }
    }

    @Override
    public void sendAllNotification(String from, Notification msg) {
        List<String> friends = friendService.getFriendsSimple(from);
        for (String receiverId : friends) {
            if (!friendService.isBlock(from, receiverId))
                sendOneNotification(receiverId, msg);
        }
    }

    private void sendOneNotification(String receiverId, Notification msg) {
        try {
            msg.setType("NOTIFICATION");
            System.out.println(msg);
            Optional<ChannelHandlerContext> ctx = MessagingSession.getChannelHandlerContext(receiverId);
            System.out.println(ctx.isPresent());;
            ctx.ifPresent(System.out::println);
            System.out.println(msg);
            if (ctx.isPresent() && ctx.get().channel().isActive()) {
                ctx.get().writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(msg)));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
