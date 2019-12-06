package ru.gothmog.blog.app.ws.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.gothmog.blog.app.ws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto user);

    UserDto getUser(String email);
}
