package com.buaa.whatupmessengermessaging.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessagingServerInitializer extends ChannelInitializer<Channel> {
    @Autowired
    TextWebSocketFrameHandler textWebSocketFrameHandler;
    @Autowired
    SslContext context;

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HTTPRequestHandler("/ws"));
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws", true));
        pipeline.addLast(textWebSocketFrameHandler);
//        SSLEngine engine = context.newEngine(ch.alloc());
//        engine.setUseClientMode(false);
//        ch.pipeline().addLast(new SslHandler(engine));
    }
}
