package ru.gothmog.blog.app.ws.service;

import ru.gothmog.blog.app.ws.shared.dto.AddressDto;

import java.util.List;

public interface AddressService {

    List<AddressDto> getAddresses(String userId);

    AddressDto getAddress(String addressId);
}
