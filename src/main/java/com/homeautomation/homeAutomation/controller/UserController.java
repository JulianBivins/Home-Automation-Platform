package com.homeautomation.homeAutomation.controller;

import com.homeautomation.homeAutomation.config.ValidationGroups;
import com.homeautomation.homeAutomation.domain.dto.UserDto;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.mapper.Mapper;
import com.homeautomation.homeAutomation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private Mapper<UserEntity, UserDto> userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        String currentUsername = authentication.getName();
        Optional<UserEntity> retrievedUser = userService.findByUsername(currentUsername);
        return retrievedUser.map(userEntity -> {
            UserDto userDto = userMapper.mapTo(userEntity);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    //For replacing the entire user (most likely won't be to relevant)
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.userId")
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateFullUser(@PathVariable Long userId, @Validated(ValidationGroups.Update.class) @RequestBody UserDto userDto) {
        if(!userService.isExists(userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userDto.setUserId(userId);
        UserEntity updatedUser = userMapper.mapFrom(userDto);
        UserEntity savedUpdatedUser = userService.saveUpdate(userId, updatedUser);
        return new ResponseEntity<>(userMapper.mapTo(savedUpdatedUser), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.userId")
    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> partialUpdate(@PathVariable Long userId, @Validated(ValidationGroups.Update.class) @RequestBody UserDto userDto) {
            if(!userService.isExists(userId)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            UserEntity userEntity = userMapper.mapFrom(userDto);
            UserEntity updatedDevice = userService.partialUpdate(userId, userEntity);
            return new ResponseEntity<>(
                    userMapper.mapTo(updatedDevice),
                    HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.userId")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        if (!userService.isExists(userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.deleteByIdCustom(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
