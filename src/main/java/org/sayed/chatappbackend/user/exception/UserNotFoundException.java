package org.sayed.chatappbackend.user.exception;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(){
        super("username or password is incorrect");
    }
}
