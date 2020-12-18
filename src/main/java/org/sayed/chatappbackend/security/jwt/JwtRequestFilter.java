package org.sayed.chatappbackend.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import org.sayed.chatappbackend.security.config.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    JwtUserDetailsService jwtUserDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String jwt = httpServletRequest.getHeader("auth");
//        if(jwt == null)
//            jwt = httpServletRequest.getHeader("access-control-request-headers");
        System.out.println("JWT: " + jwt);
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
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
