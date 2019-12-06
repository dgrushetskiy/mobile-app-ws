package ru.gothmog.blog.app.ws.ui.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import ru.gothmog.blog.app.ws.service.UserService;
import ru.gothmog.blog.app.ws.shared.dto.UserDto;
import ru.gothmog.blog.app.ws.ui.model.request.UserDetailsRequestModel;
import ru.gothmog.blog.app.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("users")//http://localhost:8080/users
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUsers(){
        return "get user was called";
    }
    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails){
        UserRest returnValue = new UserRest();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails,userDto);
        UserDto createUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createUser,returnValue);
        return returnValue;
    }
    @PutMapping
    public String updateUser(){
        return "update user was called";
    }
    @DeleteMapping
    public String deleteUser(){
        return "delete user was called";
    }
}
