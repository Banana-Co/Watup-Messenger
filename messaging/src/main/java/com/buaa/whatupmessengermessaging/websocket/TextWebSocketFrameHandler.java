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

        if (receiverId == null || !friendService.isFriend(senderId, receiverId) || friendService.isBlock(receiverId, senderId))
            return;

        Optional<ChannelHandlerContext> outCtx = MessagingSession.getChannelHandlerContext(receiverId);
        message.setSenderId(senderId);

        if (outCtx.isPresent()) {
            TextWebSocketFrame outMsg = new TextWebSocketFrame(mapper.writeValueAsString(message));
            outCtx.get().writeAndFlush(outMsg);
            ctx.writeAndFlush(outMsg);
        } else {
            messagingService.saveMessage(receiverId, message);
        }
    }

    private void handleMulticast(GroupMessage message, ChannelHandlerContext ctx) throws JsonProcessingException {
        message.setTimestamp(LocalDateTime.now());

        String groupId = message.getGroupId();

        if (groupId == null)
            return;

        Group group = groupService.getGroupById(groupId);
        String senderId = MessagingSession.getId(ctx);
        if (group == null || !group.getUsersId().contains(senderId))
            return;

        message.setSenderId(senderId);
        List<String> usersId = group.getUsersId();
        for (String userId : usersId) {
            Optional<ChannelHandlerContext> outCtx = MessagingSession.getChannelHandlerContext(userId);

            if (outCtx.isPresent()) {
                TextWebSocketFrame outMsg = new TextWebSocketFrame(mapper.writeValueAsString(message));
                outCtx.get().writeAndFlush(outMsg);
            } else {
                messagingService.saveGroupMessage(userId, message);
            }
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
       if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
           WebSocketServerProtocolHandler.HandshakeComplete handshake = (WebSocketServerProtocolHandler.HandshakeComplete)evt;

           String uri = handshake.requestUri();
           QueryStringDecoder decoder = new QueryStringDecoder(uri);
           Map<String, List<String>> params = decoder.parameters();

           if (params.containsKey("access_token")) {
               String token = params.get("access_token").get(0);

               CheckTokenResult result = authServer.checkToken(token);
               String id = result.getId();

               if (id != null) {
                   ctx.pipeline().remove(HTTPRequestHandler.class);

                   MessagingSession.addSession(token, id, ctx);
               } else {
                   ctx.channel().close();
               }
           } else {
               ctx.channel().close();
           }

       } else {
           System.out.println(evt.getClass());
           super.userEventTriggered(ctx, evt);
       }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        MessagingSession.removeSession(ctx);
    }
}
