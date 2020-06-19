package com.buaa.whatupmessengermessaging.controller;

import com.buaa.whatupmessengermessaging.exception.BadRequestException;
import com.buaa.whatupmessengermessaging.model.GroupMessage;
import com.buaa.whatupmessengermessaging.model.Message;
import com.buaa.whatupmessengermessaging.model.Notification;
import com.buaa.whatupmessengermessaging.model.Tuple;
import com.buaa.whatupmessengermessaging.service.FriendService;
import com.buaa.whatupmessengermessaging.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@CrossOrigin
public class MessageController {
    @Autowired
    MessagingService messagingService;
    @Autowired
    FriendService friendService;

    @RequestMapping(value = "/api/message", method = RequestMethod.GET)
    Object getMessages(
            @RequestParam String id,
            @RequestParam(name = "sort", defaultValue = "asc") String sort,
            @RequestParam(name = "drop", defaultValue = "true") Boolean drop,
            @RequestParam(name = "type", defaultValue = "all") String type) {

        Map<Tuple, List<Message>> messages = null;
        Map<Tuple, List<GroupMessage>> groupMessages = null;

        if (type.equalsIgnoreCase("UNICAST") || type.equalsIgnoreCase("ALL")) {
            if (sort.equalsIgnoreCase("asc"))
                messages = messagingService.getMessagesAsc(id);
            else if (sort.equalsIgnoreCase("desc"))
                messages = messagingService.getMessagesDesc(id);
            else {
                throw new BadRequestException("Wrong value for parameter sort");
            }

            if (drop)
                messagingService.dropMessages(id);
        }

        if (type.equalsIgnoreCase("MULTICAST") || type.equalsIgnoreCase("ALL")) {
            if (sort.equalsIgnoreCase("asc"))
                groupMessages = messagingService.getGroupMessagesAsc(id);
            else if (sort.equalsIgnoreCase("desc"))
                groupMessages = messagingService.getGroupMessagesDesc(id);
            else {
                throw new BadRequestException("Wrong value for parameter sort");
            }

            if (drop)
                messagingService.dropGroupMessages(id);
        }

        if (type.equalsIgnoreCase("UNICAST")) {
            return messages;
        } else if (type.equalsIgnoreCase("MULTICAST")) {
            return groupMessages;
        } else if (type.equalsIgnoreCase("ALL")) {
            assert messages != null;
            Map<Tuple, Object> allMessages = new HashMap<>(messages);
            assert groupMessages != null;
            allMessages.putAll(groupMessages);
            return allMessages;
        } else {
            throw new BadRequestException("Wrong value for parameter type");
        }

    }

    @RequestMapping(value = "/notification", method = POST)
    public void sendNotification(
            @RequestParam(name = "type", defaultValue = "unicast") String type,
            @RequestParam(name = "from", required = false) String from,
            @RequestParam(name = "to", required = false) String to,
            @RequestBody Notification msg) {

        switch (type) {
            case "unicast":
                if (to != null)
                    messagingService.sendNotification(from, to, msg);
                else
                    throw new BadRequestException("You are sending a unicast notification, please specify receiver's id as parameter \"to\".");
                break;
            case "multicast":
                if (to != null)
                    messagingService.sendGroupNotification(from, to, msg);
                else
                    throw new BadRequestException("You are sending a multicast notification, please specify group's id as parameter \"to\".");
                break;
            case "broadcast":
                if (from != null)
                    messagingService.sendAllNotification(from, msg);
                else
                    throw new BadRequestException("You are sending a broadcast notification, please specify senders's id as parameter \"to\".");
                break;
        }
    }
}
