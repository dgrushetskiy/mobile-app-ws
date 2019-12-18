package ru.gothmog.blog.app.ws.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gothmog.blog.app.ws.io.entity.AddressEntity;
import ru.gothmog.blog.app.ws.io.entity.UserEntity;
import ru.gothmog.blog.app.ws.io.repository.UserRepository;
import ru.gothmog.blog.app.ws.service.UserService;
import ru.gothmog.blog.app.ws.shared.Utils;
import ru.gothmog.blog.app.ws.shared.dto.AddressDto;
import ru.gothmog.blog.app.ws.shared.dto.UserDto;
import ru.gothmog.blog.app.ws.ui.model.response.ErrorMessages;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private Utils utils;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, Utils utils, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.utils = utils;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDto createUser(UserDto user) {

        if (userRepository.findByEmail(user.getEmail()) != null) throw  new RuntimeException("Record already exists");

        for (int i = 0; i < user.getAddresses().size(); i++) {
            AddressDto addresses = user.getAddresses().get(i);
            addresses.setUserDetails(user);
            addresses.setAddressId(utils.generateAddressId(30));
            user.getAddresses().set(i,addresses);
        }

//        UserEntity userEntity = new UserEntity();
//        BeanUtils.copyProperties(user,userEntity);
        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity =  modelMapper.map(user, UserEntity.class);

        String publicUserId = utils.generateUserId(30);
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userEntity.setEmailVerificationStatus(false);

        UserEntity storedUserDetailsSave = userRepository.save(userEntity);

        UserDto returnValue = modelMapper.map(storedUserDetailsSave,UserDto.class);
//        BeanUtils.copyProperties(storedUserDetailsSave,returnValue);

        return returnValue;
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity,returnValue);
        return returnValue;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) throw new UsernameNotFoundException("User with ID: " + userId + " not found");
        BeanUtils.copyProperties(userEntity,returnValue);
        return returnValue;
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) {
        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());

        UserEntity updateUserDetails = userRepository.save(userEntity);

        BeanUtils.copyProperties(updateUserDetails,returnValue);

        return returnValue;
    }

    @Transactional
    @Override
    public void deleteUser(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        userRepository.delete(userEntity);
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> returnValue = new ArrayList<>();
        if (page>0) page = page - 1;
        Pageable pageableResult = PageRequest.of(page, limit);

        Page<UserEntity> userPage = userRepository.findAll(pageableResult);
        List<UserEntity> users = userPage.getContent();

        users.forEach(userEntity -> {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity,userDto);
            returnValue.add(userDto);
        });

        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) throw new UsernameNotFoundException(email);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }
}
