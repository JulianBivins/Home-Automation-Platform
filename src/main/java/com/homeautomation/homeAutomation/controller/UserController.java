package com.homeautomation.homeAutomation.controller;

import com.homeautomation.homeAutomation.domain.dto.UserDto;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.mapper.Mapper;
import com.homeautomation.homeAutomation.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private UserService userService;

    private Mapper<UserEntity, UserDto> userMapper;

    public UserController(UserService authorService, Mapper<UserEntity, UserDto> authorMapper) {
        this.userService = authorService;
        this.userMapper = authorMapper;
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> createAuthor(@RequestBody UserDto userDto) {
        UserEntity userEntity = userMapper.mapFrom(userDto);
        UserEntity savedUserEntity = userService.save(userEntity);
        return new ResponseEntity<>(userMapper.mapTo(savedUserEntity), HttpStatus.CREATED);
    }
}
