package com.homeautomation.homeAutomation.controller;

import com.homeautomation.homeAutomation.domain.dto.UserDto;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.mapper.Mapper;
import com.homeautomation.homeAutomation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private Mapper<UserEntity, UserDto> userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDto> getUser (@PathVariable Long userId) {
        Optional<UserEntity> retrievedUser = userService.findById(userId);
        return retrievedUser.map(userEntity -> {
            UserDto userDto = userMapper.mapTo(userEntity);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/users", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
//    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserEntity userEntity = userMapper.mapFrom(userDto);
        String encodedPassword = passwordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);
        UserEntity createdUserEntity = userService.save(userEntity);
        return new ResponseEntity<>(userMapper.mapTo(createdUserEntity), HttpStatus.CREATED);
    }



    //For replacing the entire user (most likely won't be to relevant)
    @PutMapping("/users/{userId}")
    public ResponseEntity<UserDto> updateFullUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        if(!userService.isExists(userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userDto.setUserId(userId);
        UserEntity updatedUser = userMapper.mapFrom(userDto);
        UserEntity savedUpdatedUser = userService.saveUpdate(userId, updatedUser);
        return new ResponseEntity<>(userMapper.mapTo(savedUpdatedUser), HttpStatus.OK);
    }

    // Passing in the whole user and updating the necessary fields.
    @PatchMapping("/users/{userId}")
    public ResponseEntity<UserDto> partialUpdate(@PathVariable Long id, @RequestBody UserDto userDto) {
            if(!userService.isExists(id)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            UserEntity userEntity = userMapper.mapFrom(userDto);
            UserEntity updatedDevice = userService.partialUpdate(id, userEntity);
            return new ResponseEntity<>(
                    userMapper.mapTo(updatedDevice),
                    HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        if (!userService.isExists(userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.deleteByIdCustom(userId);
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
