package com.homeautomation.homeAutomation.controller;

import com.homeautomation.homeAutomation.domain.dto.UserDto;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.mapper.Mapper;
import com.homeautomation.homeAutomation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private Mapper<UserEntity, UserDto> userMapper;

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDto> getUser (@PathVariable Long userId) {
        Optional<UserEntity> newUser = userService.findOne(userId);
        return newUser.map(userEntity -> {
            UserDto userDto = userMapper.mapTo(userEntity);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserEntity userEntity = userMapper.mapFrom(userDto);
        UserEntity createdUserEntity = userService.save(userEntity);
        return new ResponseEntity<>(userMapper.mapTo(createdUserEntity), HttpStatus.CREATED);
    }

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
        userService.delete(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
