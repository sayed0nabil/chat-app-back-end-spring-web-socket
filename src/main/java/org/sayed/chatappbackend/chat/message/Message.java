package org.sayed.chatappbackend.chat.message;


import lombok.Getter;
import lombok.Setter;
import org.sayed.chatappbackend.user.User;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "messages")
@Getter
@Setter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Date date;

    @ManyToOne
    @JoinColumn(name="from_user_id", nullable=false)
    private User fromUser;


    @ManyToOne
    @JoinColumn(name="to_user_id", nullable=false)
    private User toUser;

    public Message(){
        this.date = new Date();
    }

    public Message(User fromUser, User toUser, String content) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.content = content;
        this.date = new Date();
    }
}
