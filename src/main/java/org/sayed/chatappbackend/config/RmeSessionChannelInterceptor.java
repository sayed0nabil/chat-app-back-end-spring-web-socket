package org.sayed.chatappbackend.config;


import io.jsonwebtoken.ExpiredJwtException;
import org.sayed.chatappbackend.security.config.JwtUserDetailsService;
import org.sayed.chatappbackend.security.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class RmeSessionChannelInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    HttpServletRequest httpServletRequest;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        System.out.println("Channel Interceptor");

        MessageHeaders headers = message.getHeaders();
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        MultiValueMap<String, String> multiValueMap = headers.get(StompHeaderAccessor.NATIVE_HEADERS,MultiValueMap.class);
        if(multiValueMap != null){
            multiValueMap.forEach((key, value) -> {
                System.out.println("Key");
                System.out.println(key);
                System.out.println(value);
                if(key.equalsIgnoreCase("auth")){
                    System.out.println(value.size());
                    String jwt = value.get(0);
                    if(jwt != null){
                        try{
                            String username = jwtTokenUtil.getUsernameFromToken(jwt);
                            System.out.println("doFilter" + username);
                            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                                UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
                                if(jwtTokenUtil.validateToken(jwt, userDetails)){
                                    System.out.println("Validated");
                                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                            new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                                    usernamePasswordAuthenticationToken.setDetails(
                                            new WebAuthenticationDetailsSource().buildDetails(httpServletRequest)
                                    );
                                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                                    System.out.println("Validate Token");
                                }
                            }
                        }catch (IllegalArgumentException ex){
                            System.out.println("Enable To Get JWT");
                        }
                        catch (ExpiredJwtException ex){
                            System.out.println("JWT has been expired");
                        }

                    }
                }
            });
        }

        return message;
    }
}