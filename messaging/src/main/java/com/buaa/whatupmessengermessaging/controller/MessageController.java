package com.buaa.whatupmessengermessaging.controller;

import com.buaa.whatupmessengermessaging.exception.BadRequestException;
import com.buaa.whatupmessengermessaging.model.SavedGroupMessage;
import com.buaa.whatupmessengermessaging.model.SavedMessage;
import com.buaa.whatupmessengermessaging.service.FriendService;
import com.buaa.whatupmessengermessaging.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class MessageController {
    @Autowired
    MessagingService messagingService;
    @Autowired
    FriendService friendService;

    @RequestMapping(value = "/api/message", method = RequestMethod.GET)
    List<SavedMessage> getMessages(
            @RequestParam String id,
            @RequestParam(name = "sort", defaultValue = "asc") String sort,
            @RequestParam(name = "drop", defaultValue = "true") Boolean drop) {

        List<SavedMessage> messages;

        if (sort.equalsIgnoreCase("asc"))
            messages = messagingService.getMessagesAsc(id);
        else if (sort.equalsIgnoreCase("desc"))
            messages = messagingService.getMessagesDesc(id);
        else {
            throw new BadRequestException("Wrong value for parameter sort");
        }

        if (drop)
            messagingService.dropMessages(id);

        return messages;
    }

    @RequestMapping(value = "/api/groupmessage", method = RequestMethod.GET)
    List<SavedGroupMessage> getGroupMessages(
            @RequestParam String id,
            @RequestParam(name = "sort", defaultValue = "asc") String sort,
            @RequestParam(name = "drop", defaultValue = "true") Boolean drop) {

        List<SavedGroupMessage> messages;

        if (sort.equalsIgnoreCase("asc"))
            messages = messagingService.getGroupMessagesAsc(id);
        else if (sort.equalsIgnoreCase("desc"))
            messages = messagingService.getGroupMessagesDesc(id);
        else {
            throw new BadRequestException("Wrong value for parameter sort");
        }

        if (drop)
            messagingService.dropGroupMessages(id);

        return messages;
    }

    @RequestMapping(value = "/notification", method = POST)
    public void sendNotification(
            @RequestParam(name = "from", defaultValue = "unicast", required = false) String from,
            @RequestParam(name = "type", defaultValue = "unicast") String type,
            @RequestParam(name = "to", required = false) String to,
            @RequestBody Object msg) {

        switch (type) {
            case "unicast":
                if (to != null)
                    messagingService.sendNotification(to, msg);
                else
                    throw new BadRequestException("You are sending a unicast notification, please specify receiver's id as parameter \"id\".");
                break;
            case "multicast":
                if (to != null)
                    messagingService.sendGroupNotification(to, msg);
                else
                    throw new BadRequestException("You are sending a multicast notification, please specify group's id as parameter \"id\".");
                break;
            case "broadcast":
                messagingService.sendAllNotification(from, msg);
                break;
        }
    }
}
