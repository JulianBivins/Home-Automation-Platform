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
public class UtilControllerIT {

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


//    @Test
//    @Transactional
//    public void testThatGetUserReturnsHttpStatus200WhenUserExist() throws Exception {
//        UserEntity testUserEntityA = TestDataUtil.createTestUserEntityA();
//        userService.save(testUserEntityA);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.get("/users/" + testUserEntityA.getUserId())
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @Test
//    @Transactional
//    public void testThatGetUserReturnsUserWhenUserExist() throws Exception {
//        UserEntity testUserEntityA = TestDataUtil.createTestUserEntityA();
//        userService.save(testUserEntityA);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.get("/users/" + testUserEntityA.getUserId())
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.userId").value(testUserEntityA.getUserId())
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.username").value("testuserA")
//        );
//    }
//
//    @Test
//    @Transactional
//    public void testThatGetUserReturnsHttpStatus404WhenNoUserExists() throws Exception {
//        mockMvc.perform(
//                MockMvcRequestBuilders.get("/users/-1")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(MockMvcResultMatchers.status().isNotFound());
//    }


@BeforeEach
@Transactional
public void setUp() throws Exception {
    testUser = TestDataUtil.createTestUserEntityA();

    RegisterRequest registerRequest = new RegisterRequest();
    registerRequest.setUsername(testUser.getUsername());
    registerRequest.setPassword(testUser.getPassword());

    mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk());

    // Authenticate to get the JWT token
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

    testUser.setRoles(new HashSet<>(Set.of(UserEntity.Roles.ADMIN, UserEntity.Roles.USER)));

    userService.partialUpdate(testUser.getUserId(), testUser);
}

    @Test
    @Transactional
    public void testThatGetUserReturnsHttpStatus200WhenUserExist() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/" + testUser.getUserId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    public void testThatGetUserReturnsUserWhenUserExist() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/" + testUser.getUserId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.userId").value(testUser.getUserId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.username").value(testUser.getUsername())
        );
    }

    @Test
    @Transactional
    public void testThatGetUserReturnsHttpStatus404WhenNoUserExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/-1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
