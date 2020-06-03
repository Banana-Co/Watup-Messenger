package com.buaa.watupmessengerfriendmanaging.model.factory;

import com.buaa.watupmessengerfriendmanaging.model.FriendRequest;

import java.time.LocalDateTime;

/**
 * @author Cast
 */
public class FriendRequestFactory {
    public static FriendRequest produce(String senderId,String remark){
        FriendRequest friendRequest=new FriendRequest();
        friendRequest.setSenderId(senderId);
        friendRequest.setRemark(remark);
        friendRequest.setCreatedDate(LocalDateTime.now());
        return friendRequest;
    }
}
