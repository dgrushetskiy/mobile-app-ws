package ru.gothmog.blog.app.ws.ui.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserRest {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
}
