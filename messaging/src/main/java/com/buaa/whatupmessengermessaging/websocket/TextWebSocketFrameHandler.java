package com.buaa.whatupmessengermessaging.websocket;

import com.buaa.whatupmessengermessaging.model.CheckTokenResult;
import com.buaa.whatupmessengermessaging.model.Group;
import com.buaa.whatupmessengermessaging.model.GroupMessage;
import com.buaa.whatupmessengermessaging.model.Message;
import com.buaa.whatupmessengermessaging.service.AuthServer;
import com.buaa.whatupmessengermessaging.service.FriendService;
import com.buaa.whatupmessengermessaging.service.GroupService;
import com.buaa.whatupmessengermessaging.service.MessagingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@ChannelHandler.Sharable
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Autowired
    MessagingService messagingService;
    @Autowired
    FriendService friendService;
    @Autowired
    GroupService groupService;
    @Autowired
    AuthServer authServer;

    public static ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public TextWebSocketFrameHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        try {
            ObjectNode node = mapper.readValue(msg.text(), ObjectNode.class);

            JsonNode type = node.findValue("type");
            if (type.asText().equalsIgnoreCase("unicast")) {
                Message message = mapper.readValue(msg.text(), Message.class);
                handleUnicast(message, ctx);
            } else if (type.asText().equalsIgnoreCase("multicast")) {
                GroupMessage groupMessage = mapper.readValue(msg.text(), GroupMessage.class);
                handleMulticast(groupMessage, ctx);
            }

        } catch (Exception ignored) {
        }
    }

    private void handleUnicast(Message message, ChannelHandlerContext ctx) throws JsonProcessingException {
        message.setTimestamp(LocalDateTime.now());

        String receiverId = message.getReceiverId();
        String senderId = MessagingSession.getId(ctx);
        message.setSenderId(senderId);

        System.out.println(String.format("%s: incoming unicast message. from: %s, to: %s", LocalDateTime.now().toString(), senderId, receiverId));

        if (receiverId == null || !friendService.isFriend(senderId, receiverId) || friendService.isBlock(receiverId, senderId)) {
            System.out.println("no such user, or not friend, or blocked, drop");
            return;
        }

        Optional<ChannelHandlerContext> outCtx = MessagingSession.getChannelHandlerContext(receiverId);

        if (outCtx.isPresent() && outCtx.get().channel().isActive()) {
            outCtx.get().writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(message)));
            ctx.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(message)));
            System.out.println(receiverId + " online, written to " + outCtx.get());
        } else {
            messagingService.saveMessage(message);
            ctx.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(message)));
            System.out.println(receiverId + " not online, saved to db");
        }
    }

    private void handleMulticast(GroupMessage message, ChannelHandlerContext ctx) throws JsonProcessingException {
        message.setTimestamp(LocalDateTime.now());
        String senderId = MessagingSession.getId(ctx);
        message.setSenderId(senderId);
        String groupId = message.getGroupId();

        System.out.println(String.format("%s: incoming multicast message. from: %s, to: %s", LocalDateTime.now().toString(), senderId, groupId));

        if (groupId == null) {
            System.out.println("group not specified, drop");
            return;
        }

        Group group = groupService.getGroupById(groupId);
        if (group == null || !group.getUsersId().contains(senderId)) {
            System.out.println("group not accessible, drop");
            return;
        }

        List<String> usersId = group.getUsersId();
        for (String userId : usersId) {
            Optional<ChannelHandlerContext> outCtx = MessagingSession.getChannelHandlerContext(userId);

            message.setReceiverId(userId);

            if (outCtx.isPresent() && outCtx.get().channel().isActive()) {
                outCtx.get().writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(message)));
                System.out.println(userId + " online, written to " + outCtx.get());
            } else {
                messagingService.saveGroupMessage(message);
                System.out.println(userId + " not online, saved to db");
            }
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
       if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
           try {
               System.out.println(String.format("%s: [HANDSHAKE COMPLETE] connection established with %s, now performing authorization", LocalDateTime.now().toString(), ctx.toString()));
               WebSocketServerProtocolHandler.HandshakeComplete handshake = (WebSocketServerProtocolHandler.HandshakeComplete) evt;

               String uri = handshake.requestUri();
               QueryStringDecoder decoder = new QueryStringDecoder(uri);
               Map<String, List<String>> params = decoder.parameters();

               if (!params.containsKey("access_token"))
                   throw new RuntimeException("no access token");

               String token = params.get("access_token").get(0);

               CheckTokenResult result = authServer.checkToken(token);

               if (result != null && result.getId() != null) {
                   ctx.pipeline().remove(HTTPRequestHandler.class);
                   MessagingSession.addSession(result.getId(), ctx);
                   System.out.println(String.format("%s: [ESTABLISHED] connection established with %s(%s)", LocalDateTime.now().toString(), result.getId(), ctx.toString()));
               } else {
                   throw new RuntimeException("auth failed");
               }

           } catch (Exception e) {
               ctx.channel().close();
               System.out.println(String.format("%s: [REMOVED] connection with %s closed due to some error: %s", LocalDateTime.now().toString(), ctx.toString(), e.getMessage()));
           }

       } else {
           super.userEventTriggered(ctx, evt);
       }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        MessagingSession.removeSession(ctx);
    }
}
