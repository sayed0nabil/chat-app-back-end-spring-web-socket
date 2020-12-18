package org.sayed.chatappbackend.user;


import org.sayed.chatappbackend.chat.message.Message;
import org.sayed.chatappbackend.chat.message.MessageRepository;
import org.sayed.chatappbackend.security.config.JwtUserDetailsService;
import org.sayed.chatappbackend.security.jwt.JwtTokenUtil;
import org.sayed.chatappbackend.user.Login.LoginRequest;
import org.sayed.chatappbackend.user.Login.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin("http://localhost:4200")
public class UserController {


    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    MessageRepository messageRepository;


    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/login")
    public String justTestLoginGet(){
        return "Login Work With No Authenticate";
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws BadCredentialsException {
        authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(loginRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody LoginRequest loginRequest) throws Exception{
        User user = new User(loginRequest.getUsername(), loginRequest.getPassword());
        userService.addUser(user);
        simpMessagingTemplate.convertAndSend( "/topic/users/" , user);
        return ResponseEntity.ok(user);
    }

    private void authenticate(String username, String password) throws BadCredentialsException {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }catch (BadCredentialsException ex){
            throw new BadCredentialsException("username or password is incorrect");
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/not-logged-user")
    public ResponseEntity<List<User>> getAllUsersExceptLoggedUser(){
        return ResponseEntity.ok(userService.findAllExceptLoggedUser());
    }
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessagesRelatedToLoggedUser(){
        String loggedUsername = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User loggedUser = userService.findUserByUsername(loggedUsername);
        return ResponseEntity.ok(messageRepository.findByFromUserOrToUserOrderByDate(loggedUser, loggedUser));
    }
}
