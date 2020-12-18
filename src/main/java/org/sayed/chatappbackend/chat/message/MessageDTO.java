package org.sayed.chatappbackend.chat.message;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sayed.chatappbackend.user.User;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MessageDTO {

    private String content;

    private String fromUsername;

    private long toUserId;
}
