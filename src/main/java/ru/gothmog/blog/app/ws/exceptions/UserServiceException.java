package ru.gothmog.blog.app.ws.exceptions;

public class UserServiceException extends RuntimeException {

    public UserServiceException(String message) {
        super(message);
    }
}
