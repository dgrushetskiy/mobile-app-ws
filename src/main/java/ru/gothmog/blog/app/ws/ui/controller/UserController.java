package ru.gothmog.blog.app.ws.ui.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.gothmog.blog.app.ws.service.AddressService;
import ru.gothmog.blog.app.ws.service.UserService;
import ru.gothmog.blog.app.ws.shared.dto.AddressDto;
import ru.gothmog.blog.app.ws.shared.dto.UserDto;
import ru.gothmog.blog.app.ws.ui.model.request.UserDetailsRequestModel;
import ru.gothmog.blog.app.ws.ui.model.response.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("users")//http://localhost:8080/users
public class UserController {

    private UserService userService;

    private AddressService addressService;

    public UserController(UserService userService, AddressService addressService) {
        this.userService = userService;
        this.addressService = addressService;
    }

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(@PathVariable String id){
        UserDto userDto = userService.getUserByUserId(id);
        ModelMapper modelMapper = new ModelMapper();
        UserRest returnValue = modelMapper.map(userDto,UserRest.class);
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
    @GetMapping(path = "/{id}/addresses", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<AddressRest> getUserAddresses(@PathVariable String id){
        List<AddressRest> returnValue = new ArrayList<>();
        List<AddressDto> addressesDto = addressService.getAddresses(id);
        if (addressesDto !=null && !addressesDto.isEmpty()){
            Type listType = new TypeToken<List<AddressRest>>() {}.getType();
            returnValue = new ModelMapper().map(addressesDto,listType);
        }
        return returnValue;
    }
    @GetMapping(path = "/{userId}/addresses/{addressId}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public AddressRest getUserAddress(@PathVariable String userId,@PathVariable String addressId){
        AddressDto addressDto = addressService.getAddress(addressId);

        Link addressLink = linkTo(UserController.class)
                .slash(userId)
                .slash("addresses")
                .slash(addressId)
                .withSelfRel();

        Link userLink = linkTo(UserController.class).slash(userId).withRel("user");
        Link addressesLink = linkTo(UserController.class).slash(userId).slash("addresses").withRel("addresses");

        AddressRest addressRestModel = new ModelMapper().map(addressDto,AddressRest.class);

        addressRestModel.add(addressLink);
        addressRestModel.add(userLink);
        addressRestModel.add(addressesLink);

        return addressRestModel;
    }
}
