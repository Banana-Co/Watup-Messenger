package com.buaa.whatupmessengermessaging.websocket;

import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ConcurrentHashMap;

public class MessagingSession {
    private static ConcurrentHashMap<String, ChannelHandlerContext> id_ctx_map = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<ChannelHandlerContext, String> ctx_id_map = new ConcurrentHashMap<>();

    public static ChannelHandlerContext getChannelHandlerContext(String id) {
        return id_ctx_map.get(id);
    }

    public static String getId(ChannelHandlerContext ctx) {
        return ctx_id_map.get(ctx);
    }

    public static void addSession(String id, ChannelHandlerContext ctx) {
        id_ctx_map.put(id, ctx);
        ctx_id_map.put(ctx, id);
    }

    public static void removeSession(ChannelHandlerContext ctx) {
        String id = ctx_id_map.get(ctx);
        id_ctx_map.remove(id);
        ctx_id_map.remove(ctx);
    }
}


