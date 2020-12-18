package org.sayed.chatappbackend.user;

import org.sayed.chatappbackend.user.exception.UsernameAlreadyExistException;
import org.sayed.chatappbackend.user.exception.UserNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    User findUserByUsername(String username) throws UsernameNotFoundException;

    User findUserByUsernameAndPassword(String username, String password) throws UserNotFoundException;

    void addUser(User user) throws UsernameAlreadyExistException;

    List<User> findAll();

    List<User> findAllExceptLoggedUser();
}
