package org.sayed.chatappbackend.user.Login;


import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginRequest implements Serializable {

    private String username;

    private String password;
}
