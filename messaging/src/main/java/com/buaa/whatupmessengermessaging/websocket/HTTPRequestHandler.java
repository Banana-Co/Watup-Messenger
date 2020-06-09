package com.buaa.whatupmessengermessaging.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

public class HTTPRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    String wsUri;

    public HTTPRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String uri = request.uri();
        QueryStringDecoder decoder = new QueryStringDecoder(uri);

        if (wsUri.equalsIgnoreCase(decoder.path())) {
            ctx.fireChannelRead(request.retain());
        } else {
            ctx.channel().close();
        }
    }
}
