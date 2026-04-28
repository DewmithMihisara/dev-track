package com.dev.dev_track.controller;

import com.dev.dev_track.dto.ResponseDto;
import com.dev.dev_track.dto.UserDto;
import com.dev.dev_track.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
@Tag(name = "user Controller", description = "user related operations...")
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
