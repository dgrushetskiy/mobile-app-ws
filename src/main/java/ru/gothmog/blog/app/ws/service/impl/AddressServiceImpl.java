package ru.gothmog.blog.app.ws.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.gothmog.blog.app.ws.io.entity.AddressEntity;
import ru.gothmog.blog.app.ws.io.entity.UserEntity;
import ru.gothmog.blog.app.ws.io.repository.AddressRepository;
import ru.gothmog.blog.app.ws.io.repository.UserRepository;
import ru.gothmog.blog.app.ws.service.AddressService;
import ru.gothmog.blog.app.ws.shared.dto.AddressDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private UserRepository userRepository;
    private AddressRepository addressRepository;

    public AddressServiceImpl(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public List<AddressDto> getAddresses(String userId) {
        List<AddressDto> returnValue = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity==null) return returnValue;

        Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);

        addresses.forEach(addressEntity -> {
            returnValue.add(modelMapper.map(addressEntity,AddressDto.class));
        });
        return returnValue;
    }

    @Override
    public AddressDto getAddress(String addressId) {
        AddressDto returnValue = null;
        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
        if(addressEntity!=null) {
            returnValue = new ModelMapper().map(addressEntity, AddressDto.class);
        }
        return returnValue;
    }
}
