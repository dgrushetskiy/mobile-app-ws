package ru.gothmog.blog.app.ws.shared.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter

public class UserDto implements Serializable {
    private long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String encryptedPassword;
    private String emailVerificationToken;
    private Boolean emailVerificationStatus;
}
