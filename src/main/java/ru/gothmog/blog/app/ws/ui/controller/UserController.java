package ru.gothmog.blog.app.ws.ui.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.gothmog.blog.app.ws.exceptions.UserServiceException;
import ru.gothmog.blog.app.ws.service.UserService;
import ru.gothmog.blog.app.ws.shared.dto.UserDto;
import ru.gothmog.blog.app.ws.ui.model.request.UserDetailsRequestModel;
import ru.gothmog.blog.app.ws.ui.model.response.ErrorMessages;
import ru.gothmog.blog.app.ws.ui.model.response.OperationStatusModel;
import ru.gothmog.blog.app.ws.ui.model.response.RequestOperationStatus;
import ru.gothmog.blog.app.ws.ui.model.response.UserRest;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users")//http://localhost:8080/users
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(@PathVariable String id){
        UserRest returnValue = new UserRest();
        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto,returnValue);
        return returnValue;
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
            )
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {

//        if (userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
//        UserDto userDto = new UserDto();
//        BeanUtils.copyProperties(userDetails,userDto);
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createUser = userService.createUser(userDto);
        UserRest returnValue = modelMapper.map(createUser, UserRest.class);
//        BeanUtils.copyProperties(createUser,returnValue);
        return returnValue;
    }

    @PutMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public UserRest updateUser(@PathVariable String id,@RequestBody UserDetailsRequestModel userDetails){
        UserRest returnValue = new UserRest();
//        UserDto userDto = new UserDto();
//        BeanUtils.copyProperties(userDetails,userDto);
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto updateUser = userService.updateUser(id,userDto);
        BeanUtils.copyProperties(updateUser,returnValue);
        return returnValue;
    }

    @DeleteMapping(
            path = "/{id}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public OperationStatusModel deleteUser(@PathVariable String id){
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());

        userService.deleteUser(id);

        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return returnValue;
    }

    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<UserRest> getUsers(
            @RequestParam(value = "page", defaultValue = "0")int page,
            @RequestParam(value = "limit", defaultValue = "25")int limit){
        List<UserRest> returnValue = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page,limit);
        users.forEach(userDto -> {
            UserRest userModel = new UserRest();
            BeanUtils.copyProperties(userDto,userModel);
            returnValue.add(userModel);
        });
        return returnValue;
    }
}
