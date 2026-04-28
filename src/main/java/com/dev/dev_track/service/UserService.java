package com.dev.dev_track.service;

import com.dev.dev_track.dto.ResponseDto;
import com.dev.dev_track.dto.UserDto;
import com.dev.dev_track.entities.UserEntity;
import com.dev.dev_track.repo.UserRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepo userRepo;

    private final Logger log = LogManager.getLogger(UserService.class);

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public ResponseDto saveUser(UserDto userDto){
        try {
            UserEntity save = userRepo.save(UserEntity.builder()
                    .userName(userDto.getUsername())
                    .email(userDto.getEmail())
                    .password(userDto.getPassword())
                    .build());

            if(save != null){
                return new ResponseDto(200, "User has been saved successfully");
            }else {
                return new ResponseDto(400, "User is not saved successfully");
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseDto(500, "OOPS! Something went Wrong..");
        }
    }

    public ResponseDto updateUser(UserDto userDto) {
        try {
            int isHave = userRepo.countByEmail(userDto.getEmail());

            if (isHave > 0) {
                log.info("Email already exist");
                return new ResponseDto(400, "Email already exist");
            }

            Optional<UserEntity> user = userRepo.findByUserId(userDto.getId());

            if (user.isPresent()) {
                UserEntity userEntity = user.get();

                userEntity.setUserName(userDto.getUsername());
                userEntity.setEmail(userDto.getEmail());

                UserEntity save = userRepo.save(userEntity);

                if(save != null){
                    log.info("User has been saved successfully");
                    return new ResponseDto(200, "User has been saved successfully");
                }else  {
                    log.info("User is not saved successfully");
                    return new ResponseDto(400, "User is not saved successfully");
                }

            }else {
                log.info("User not found");
                return new ResponseDto(400, "User not found");
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            return new ResponseDto(500, "OOPS! Something went Wrong..");
        }
    }
}
