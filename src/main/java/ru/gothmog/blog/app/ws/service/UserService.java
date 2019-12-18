package ru.gothmog.blog.app.ws.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.gothmog.blog.app.ws.shared.dto.UserDto;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto user);

    UserDto getUser(String email);

    UserDto getUserByUserId(String userId);

    UserDto updateUser(String userId, UserDto userDto);

    void deleteUser(String userId);

    List<UserDto> getUsers(int page, int limit);

}
