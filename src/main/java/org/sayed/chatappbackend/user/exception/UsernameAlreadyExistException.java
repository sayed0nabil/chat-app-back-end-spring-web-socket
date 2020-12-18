package org.sayed.chatappbackend.user.exception;

public class UsernameAlreadyExistException extends Exception {

    public UsernameAlreadyExistException(){
        super("username already exists");
    }
}
