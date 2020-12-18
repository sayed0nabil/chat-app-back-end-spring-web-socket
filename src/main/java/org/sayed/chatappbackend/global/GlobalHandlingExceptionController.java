package org.sayed.chatappbackend.global;


import org.sayed.chatappbackend.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalHandlingExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UsernameNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<ApiException> handleNotFoundException(Exception ex, WebRequest request){
        return new ResponseEntity<>(new ApiException(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiException> handleAnyException(Exception ex, WebRequest request){
        return new ResponseEntity<>(new ApiException(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

}