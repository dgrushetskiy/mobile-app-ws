package ru.gothmog.blog.app.ws.ui.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserDetailsRequestModel {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<AddressRequestModel> addresses;
}
