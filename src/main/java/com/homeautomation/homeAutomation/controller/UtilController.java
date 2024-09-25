package com.homeautomation.homeAutomation.controller;

import com.homeautomation.homeAutomation.domain.dto.HomeAutomationRuleDto;
import com.homeautomation.homeAutomation.domain.dto.UserDto;
import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.mapper.Mapper;
import com.homeautomation.homeAutomation.services.HomeAutomationRuleService;
import com.homeautomation.homeAutomation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/util")
public class UtilController {
    @Autowired
    private UserService userService;
    @Autowired
    private Mapper<UserEntity, UserDto> userMapper;
    @Autowired
    private HomeAutomationRuleService homeAutomationRuleService;
    @Autowired
    private Mapper<HomeAutomationRuleEntity, HomeAutomationRuleDto> ruleMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        Optional<UserEntity> retrievedUser = userService.findById(userId);
        return retrievedUser.map(userEntity -> {
            UserDto userDto = userMapper.mapTo(userEntity);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<HomeAutomationRuleDto>> listHomeAutomationRules(Authentication authentication) {
        String currentUsername = authentication.getName();
        Optional<UserEntity> retrievedUser = userService.findByUsername(currentUsername);
        if (retrievedUser.isEmpty()) {
            throw new RuntimeException("User is not present");
        }

        List<HomeAutomationRuleEntity> returnedRules = homeAutomationRuleService.getRulesByUserId(retrievedUser.get());
        List<HomeAutomationRuleDto> ruleDtos = returnedRules.stream()
                .map(ruleMapper::mapTo)
                .collect(Collectors.toList());
        return new ResponseEntity<>(ruleDtos, HttpStatus.OK);
    }

}
