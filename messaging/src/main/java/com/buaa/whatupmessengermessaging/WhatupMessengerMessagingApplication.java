package com.buaa.whatupmessengermessaging;

import com.buaa.whatupmessengermessaging.model.Message;
import com.buaa.whatupmessengermessaging.websocket.MessagingServer;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;

import java.net.InetSocketAddress;

@SpringBootApplication
public class WhatupMessengerMessagingApplication implements CommandLineRunner {
    @Value("${websocket.port}")
    private Integer port;

    @Autowired
    MessagingServer endpoint;

    public static void main(String[] args) {
        SpringApplication.run(WhatupMessengerMessagingApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ChannelFuture future = endpoint.start(new InetSocketAddress(port));

        Runtime.getRuntime().addShutdownHook(new Thread(endpoint::destroy));
        future.channel().closeFuture().syncUninterruptibly();
    }
}
