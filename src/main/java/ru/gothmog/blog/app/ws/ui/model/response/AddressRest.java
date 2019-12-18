package ru.gothmog.blog.app.ws.ui.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRest {
    private String addressId;
    private String city;
    private String country;
    private String streetName;
    private String postalCode;
    private String type;
}
