package ru.gothmog.blog.app.ws.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.gothmog.blog.app.ws.io.entity.UserEntity;
import ru.gothmog.blog.app.ws.io.repository.UserRepository;
import ru.gothmog.blog.app.ws.service.UserService;
import ru.gothmog.blog.app.ws.shared.Utils;
import ru.gothmog.blog.app.ws.shared.dto.UserDto;

import java.util.ArrayList;

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

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user,userEntity);

        String publicUserId = utils.generateUserId(30);
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userEntity.setEmailVerificationStatus(false);

        UserEntity storedUserDetailsSave = userRepository.save(userEntity);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(storedUserDetailsSave,returnValue);

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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) throw new UsernameNotFoundException(email);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }
}
