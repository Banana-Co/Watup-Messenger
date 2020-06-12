package com.buaa.watupmessengerfriendmanaging.model.factory;

import com.buaa.watupmessengerfriendmanaging.model.FriendRequest;

import java.time.LocalDateTime;

/**
 * @author Cast
 */
public class FriendRequestFactory {
    public static FriendRequest produce(String senderId,String receiverId,String remark,String url){
        FriendRequest friendRequest=new FriendRequest();
        friendRequest.setSenderId(senderId);
        friendRequest.setReceiverId(receiverId);
        friendRequest.setRemark(remark);
        friendRequest.setSenderAvatarUrl(url);
        friendRequest.setCreatedDate(LocalDateTime.now());
        return friendRequest;
    }
}
