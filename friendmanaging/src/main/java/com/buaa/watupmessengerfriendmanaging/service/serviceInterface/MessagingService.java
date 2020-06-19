package com.buaa.watupmessengerfriendmanaging.service.serviceInterface;

import com.buaa.watupmessengerfriendmanaging.model.Notification;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Cast
 */
@FeignClient(value = "MESSAGING-SERVICE")
public interface MessagingService {
    @RequestMapping(value = "/notification", method = POST)
    void sendNotification(
            @RequestParam(name = "type", defaultValue = "unicast") String type,
            @RequestParam(name = "from", required = false) String from,
            @RequestParam(name = "to", required = false) String to,
            @RequestBody Notification msg);
}
