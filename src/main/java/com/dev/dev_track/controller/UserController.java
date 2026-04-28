package com.dev.dev_track.controller;

import com.dev.dev_track.dto.ResponseDto;
import com.dev.dev_track.dto.UserDto;
import com.dev.dev_track.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin("*")
public class UserController {
    private final UserService userService;

    private final Logger log = LogManager.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseDto createUserOrUpdate(@RequestBody UserDto userDto) {
        try {
            if (userDto.getId() == null) {
                return userService.saveUser(userDto);
            }else {
                return userService.updateUser(userDto);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseDto(400, "User is not saved successfully");
        }
    }
}
