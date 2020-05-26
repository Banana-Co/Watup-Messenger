package com.buaa.whatupmessengermessaging.websocket;

import com.buaa.whatupmessengermessaging.model.Message;
import com.buaa.whatupmessengermessaging.service.UserTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
@ChannelHandler.Sharable
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Autowired
    UserTokenService userTokenService;

    public static ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public TextWebSocketFrameHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        Message message = mapper.readValue(msg.text(), Message.class);

        System.out.println(message.toString());

        ChannelHandlerContext outCtx = MessagingSession.getChannelHandlerContext(message.getId());
        message.setId(MessagingSession.getId(ctx));
        message.setTimestamp(LocalDateTime.now());

        System.out.println(message.toString());

        TextWebSocketFrame outMsg = new TextWebSocketFrame(mapper.writeValueAsString(message));
        outCtx.writeAndFlush(outMsg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println(evt.getClass());
       if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
           WebSocketServerProtocolHandler.HandshakeComplete handshake = (WebSocketServerProtocolHandler.HandshakeComplete)evt;

           String uri = handshake.requestUri();
           QueryStringDecoder decoder = new QueryStringDecoder(uri);
           Map<String, List<String>> params = decoder.parameters();

           if (params.containsKey("token")) {
               String token = params.get("token").get(0);
               System.out.println(token);
               System.out.println(String.format("token:%s", token));
               String id = userTokenService.getId(String.format("token:%s", token));
               System.out.println(id);

               if (id != null) {
                   ctx.pipeline().remove(HTTPRequestHandler.class);

                   MessagingSession.addSession(id, ctx);
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
