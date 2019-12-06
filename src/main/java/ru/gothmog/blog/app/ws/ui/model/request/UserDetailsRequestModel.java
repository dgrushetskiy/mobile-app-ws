package ru.gothmog.blog.app.ws.ui.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDetailsRequestModel {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
