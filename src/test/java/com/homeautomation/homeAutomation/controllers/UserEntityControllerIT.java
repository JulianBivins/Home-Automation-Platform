package com.homeautomation.homeAutomation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeautomation.homeAutomation.config.TestDataUtil;
import com.homeautomation.homeAutomation.controller.util.AuthenticationRequest;
import com.homeautomation.homeAutomation.controller.util.AuthenticationResponse;
import com.homeautomation.homeAutomation.controller.util.RegisterRequest;
import com.homeautomation.homeAutomation.domain.dto.UserDto;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.mapper.Mapper;
import com.homeautomation.homeAutomation.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserEntityControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Mapper<UserEntity, UserDto> userMapper;

    private String jwtToken;
    private UserEntity testUser;

    @BeforeEach
    public void setUp() throws Exception {
        testUser = TestDataUtil.createTestUserEntityA();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(testUser.getUsername());
        registerRequest.setPassword(testUser.getPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername(testUser.getUsername());
        authenticationRequest.setPassword(testUser.getPassword());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        AuthenticationResponse authResponse = objectMapper.readValue(responseBody, AuthenticationResponse.class);
        jwtToken = authResponse.getToken();

        testUser = userService.findByUsername(testUser.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
    }


    @Test
    @Transactional
    public void testThatGetCurrentUserReturnsHttpStatus200WhenAuthenticatedUserExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(testUser.getUsername()));
    }

    @Test
    @Transactional
    public void testThatFullUpdateUpdatesExistingUser() throws Exception {

        UserEntity updatedUser = TestDataUtil.createTestUserEntityB();
        UserDto userDto = userMapper.mapTo(updatedUser);

        String userDtoUpdateJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/" + testUser.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(userDtoUpdateJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(updatedUser.getUsername()));
    }

    @Test
    @Transactional
    public void testThatPartialUpdateExistingUserReturnsHttpStatus200() throws Exception {
        UserDto userDto = userMapper.mapTo(testUser);
        userDto.setUsername("UPDATED");
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/" + testUser.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(userDtoJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    public void testThatDeleteUserReturnsHttpStatus204ForExistingUser() throws Exception {
         mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + testUser.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}
