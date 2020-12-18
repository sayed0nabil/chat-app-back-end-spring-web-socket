package org.sayed.chatappbackend.chat.message;


import org.sayed.chatappbackend.chat.message.Message;
import org.sayed.chatappbackend.chat.message.MessageDTO;
import org.sayed.chatappbackend.chat.message.MessageRepository;
import org.sayed.chatappbackend.user.User;
import org.sayed.chatappbackend.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@Controller
@CrossOrigin("http://localhost:4200")
public class MessageController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserServiceImpl userService;

    @MessageMapping("/chat/{to}")
    public void sendSpecific(
            @DestinationVariable String to,
            @Payload MessageDTO message
            ) throws Exception {
        User fromUser = userService.findUserByUsername(message.getFromUsername());
        User toUser = userService.findUserByUsername(to);
        Message newMessage = new Message(fromUser, toUser, message.getContent());
        messageRepository.save(newMessage);
        System.out.println(to);
        System.out.println(message.getContent());
        simpMessagingTemplate.convertAndSend( "/topic/messages/" + to , newMessage);
        simpMessagingTemplate.convertAndSend( "/topic/messages/" + message.getFromUsername() , newMessage);


    }

}
