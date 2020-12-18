package org.sayed.chatappbackend.user;

import org.sayed.chatappbackend.user.exception.UsernameAlreadyExistException;
import org.sayed.chatappbackend.user.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;


@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;


    Principal principal;

    public User findUserByUsername(String username) throws UsernameNotFoundException{
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }

    public User findUserByUsernameAndPassword(String username, String password) throws UserNotFoundException {
        return userRepository.findByUsernameAndPassword(username, password).orElseThrow(() -> new UserNotFoundException());
    }

    public void addUser(User user) throws UsernameAlreadyExistException{
        try{
            findUserByUsername(user.getUsername());
            throw new UsernameAlreadyExistException();
        }catch (UsernameNotFoundException ex){
            userRepository.save(user);
        }
    }

    public List<User> findAll(){
        return (List<User>) userRepository.findAll();
    }

    public List<User> findAllExceptLoggedUser(){
        return userRepository.findByUsernameNot(((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
    }

}
