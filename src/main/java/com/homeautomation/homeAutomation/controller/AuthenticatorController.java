package com.homeautomation.homeAutomation.controller;


import com.homeautomation.homeAutomation.domain.dto.UserDto;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.mapper.Mapper;
import com.homeautomation.homeAutomation.repository.UserRepository;
import com.homeautomation.homeAutomation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AuthenticatorController {

    @Autowired
    private Mapper<UserEntity, UserDto> userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/Login")
    public ResponseEntity<Void> authenticateUser(@RequestBody UserDto userDto) {
        UserEntity userEntity = userMapper.mapFrom(userDto);

        Optional<UserEntity> retrievedUser = userService.findByUsername(userEntity.getUsername());
        if (!retrievedUser.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        boolean matches = passwordEncoder.matches(userEntity.getPassword(), retrievedUser.get().getPassword());
        if (matches){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
