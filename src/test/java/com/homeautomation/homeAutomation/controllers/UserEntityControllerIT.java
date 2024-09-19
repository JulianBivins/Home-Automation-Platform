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
//        userService.save(testUser);
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
    }

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

//    @Test
//    @Transactional
//    public void testThatCreateUserSuccessfullyReturnsHttp201Created() throws Exception {
//        UserEntity testUserEntityA = TestDataUtil.createTestUserEntityA();
//        testUserEntityA.setUserId(null);
//        UserDto testUserDtoA = userMapper.mapTo(testUserEntityA);
//        String userJson = objectMapper.writeValueAsString(testUserDtoA);
//
//        System.out.println("USER_JSON = " + userJson ); // logging
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(userJson)
//        ).andExpect(
//                MockMvcResultMatchers.status().isCreated()
//        );
//    }
//
//    @Test
//    @Transactional
//    public void testThatCreateUserSuccessfullyReturnsSavedUser() throws Exception {
//        UserEntity testUserEntityA = TestDataUtil.createTestUserEntityA();
//        testUserEntityA.setUserId(null);
//        UserDto testUserDtoA = userMapper.mapTo(testUserEntityA);
//        String authorJson = objectMapper.writeValueAsString(testUserDtoA);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(authorJson)
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.userId").isNumber()
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.username").value("testuserA")
//        );
//    }









//    @Test
//    @Transactional
//    public void testThatGetCurrentUserReturnsHttpStatus200WhenAuthenticatedUserExists() throws Exception {
//        UserEntity testUserEntityA = TestDataUtil.createTestUserEntityA();
//        userService.save(testUserEntityA);
//
//        Authentication authentication = new UsernamePasswordAuthenticationToken(testUserEntityA.getUsername(), testUserEntityA.getPassword(), testUserEntityA.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        mockMvc.perform(
//                        MockMvcRequestBuilders.get("/users/me")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .principal(authentication)
//                ).andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(testUserEntityA.getUsername()));
//    }
//
//    @Test
//    @Transactional
//    public void testThatGetCurrentUserReturnsHttpStatus404WhenAuthenticatedUserDoesNotExist() throws Exception {
//        UserEntity testUserEntityA = TestDataUtil.createTestUserEntityA();
//        userService.save(testUserEntityA);
//        testUserEntityA.setUsername("NONEXISTING");
//        testUserEntityA.setPassword("0000");
//        System.out.println(testUserEntityA);
//
//        Authentication authentication = new UsernamePasswordAuthenticationToken(testUserEntityA.getUsername(), testUserEntityA.getPassword(), testUserEntityA.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.get("/users/me")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .principal(authentication)  // Set the principal for the request
//        ).andExpect(MockMvcResultMatchers.status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    public void testThatFullUpdateUpdatesExistingUser() throws Exception {
//        UserEntity testUserEntityA = TestDataUtil.createTestUserEntityA();
//        UserEntity savedUser = userService.save(testUserEntityA);
//
//        UserEntity testUserEntityB = TestDataUtil.createTestUserEntityB();
//        UserDto testUserDtoB = userMapper.mapTo(testUserEntityB);
//
//        String userDtoUpdateJson = objectMapper.writeValueAsString(testUserDtoB);
//
//        Authentication authentication = new UsernamePasswordAuthenticationToken(
//                testUserEntityA.getUsername(),
//                testUserEntityA.getPassword(),
//                testUserEntityA.getAuthorities()
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.put("/users/" + savedUser.getUserId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .principal(authentication)
//                        .content(userDtoUpdateJson)
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.userId").value(savedUser.getUserId())
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.username").value(testUserDtoB.getUsername())
//        );
//    }
//
//    @Test
//    @Transactional
//    public void testThatFullUpdateUserReturnsHttpStatus404WhenNoUserExists() throws Exception {
//        UserEntity testUserEntityA = TestDataUtil.createTestUserEntityA();
//        UserDto testUserDtoA = userMapper.mapTo(testUserEntityA);
//        String userDtoJson = objectMapper.writeValueAsString(testUserDtoA);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.put("/users/-1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(userDtoJson)
//        ).andExpect(MockMvcResultMatchers.status().isNotFound());
//    }
//
//    @Test
//    @Transactional
//    public void testThatPartialUpdateExistingUserReturnsHttpStatus200() throws Exception {
//        UserEntity testUserEntityA = TestDataUtil.createTestUserEntityA();
//        UserEntity savedUser = userService.save(testUserEntityA);
//
//        UserDto testUserDtoA = userMapper.mapTo(testUserEntityA);
//        testUserDtoA.setUsername("UPDATED");
//        String userDtoJson = objectMapper.writeValueAsString(testUserDtoA);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.patch("/users/" + savedUser.getUserId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(userDtoJson)
//        ).andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @Test
//    @Transactional
//    public void testThatPartialUpdateExistingUserReturnsUpdatedUser() throws Exception {
//        UserEntity testUserEntityA = TestDataUtil.createTestUserEntityA();
//        UserEntity savedUser = userService.save(testUserEntityA);
//
//        UserDto testUserDtoA = userMapper.mapTo(testUserEntityA);
//        testUserDtoA.setUsername("UPDATED");
//        String userDtoJson = objectMapper.writeValueAsString(testUserDtoA);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.patch("/users/" + savedUser.getUserId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(userDtoJson)
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.userId").value(savedUser.getUserId())
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.username").value("UPDATED")
//        );
//    }
//
//
//
//    @Test
//    @Transactional
//    public void testThatDeleteUserReturnsHttpStatus204ForExistingUser() throws Exception {
//        UserEntity testUserEntityA = TestDataUtil.createTestUserEntityA();
//        UserEntity savedUser = userService.save(testUserEntityA);
//
//        System.out.println("LOGGING = " + testUserEntityA);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.delete("/users/" + savedUser.getUserId())
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(MockMvcResultMatchers.status().isNoContent());
//    }
//
//    @Test
//    @Transactional
//    public void testThatDeleteUserReturnsHttpStatus404ForExistingUser() throws Exception {
//        UserEntity testUserEntityA = TestDataUtil.createTestUserEntityA();
//        userService.save(testUserEntityA);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.delete("/users/-1")
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(MockMvcResultMatchers.status().isNotFound());
//    }

    @Test
    @Transactional
    public void testThatGetCurrentUserReturnsHttpStatus200WhenAuthenticatedUserExists() throws Exception {
        UserEntity testUser = TestDataUtil.createTestUserEntityA();
        userService.save(testUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(testUser.getUsername()));
    }

    @Test
    @Transactional
    public void testThatFullUpdateUpdatesExistingUser() throws Exception {
//        UserEntity testUser = TestDataUtil.createTestUserEntityA();
//        userService.save(testUser);

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
//        UserEntity testUser = TestDataUtil.createTestUserEntityA();
//        userService.save(testUser);

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
//        UserEntity testUser = TestDataUtil.createTestUserEntityA();
//        UserEntity savedUser = userService.save(testUser);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + testUser.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}
