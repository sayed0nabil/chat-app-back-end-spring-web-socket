package org.sayed.chatappbackend.chat.message;

import org.sayed.chatappbackend.chat.message.Message;
import org.sayed.chatappbackend.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface MessageRepository extends CrudRepository<Message, Long> {

    public List<Message> findByFromUserOrToUserOrderByDate(User fromUser, User toUser);
}
