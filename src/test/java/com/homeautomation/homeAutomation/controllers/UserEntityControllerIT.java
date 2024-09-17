package com.homeautomation.homeAutomation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeautomation.homeAutomation.config.TestDataUtil;
import com.homeautomation.homeAutomation.domain.dto.UserDto;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.mapper.Mapper;
import com.homeautomation.homeAutomation.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
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

    @Test
    @Transactional
    public void testThatGetUserReturnsHttpStatus200WhenUserExist() throws Exception {
        UserEntity testUserEntityA = TestDataUtil.createTestUserEntityA();
        userService.save(testUserEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/" + testUserEntityA.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetUserReturnsUserWhenUserExist() throws Exception {
        UserEntity testUserEntityA = TestDataUtil.createTestUserEntityA();
        userService.save(testUserEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/" + testUserEntityA.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.userId").value(testUserEntityA.getUserId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.username").value("testuserA")
        );
    }

    @Test
    @Transactional
    public void testThatGetUserReturnsHttpStatus404WhenNoUserExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/-1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Transactional
    public void testThatCreateUserSuccessfullyReturnsHttp201Created() throws Exception {
        UserEntity testUserEntityA = TestDataUtil.createTestUserEntityA();
        testUserEntityA.setUserId(null);
        UserDto testUserDtoA = userMapper.mapTo(testUserEntityA);
        String userJson = objectMapper.writeValueAsString(testUserDtoA);

        System.out.println("USER_JSON = " + userJson ); // logging

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    @Transactional
    public void testThatCreateUserSuccessfullyReturnsSavedUser() throws Exception {
        UserEntity testUserEntityA = TestDataUtil.createTestUserEntityA();
        testUserEntityA.setUserId(null);
        UserDto testUserDtoA = userMapper.mapTo(testUserEntityA);
        String authorJson = objectMapper.writeValueAsString(testUserDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.userId").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.username").value("testuserA")
        );
    }

    @Test
    @Transactional
    public void testThatFullUpdateUpdatesExistingUser() throws Exception {
        UserEntity testUserEntityA = TestDataUtil.createTestUserEntityA();
        UserEntity savedUser = userService.save(testUserEntityA);

        UserEntity testUserEntityB = TestDataUtil.createTestUserEntityB();
        UserDto testUserDtoB = userMapper.mapTo(testUserEntityB);

        String authorDtoUpdateJson = objectMapper.writeValueAsString(testUserDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/users/" + savedUser.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoUpdateJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.userId").value(savedUser.getUserId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.username").value(testUserDtoB.getUsername())
        );
    }

    @Test
    @Transactional
    public void testThatFullUpdateUserReturnsHttpStatus404WhenNoUserExists() throws Exception {
        UserEntity testUserEntityA = TestDataUtil.createTestUserEntityA();
        UserDto testUserDtoA = userMapper.mapTo(testUserEntityA);
        String userDtoJson = objectMapper.writeValueAsString(testUserDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/users/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Transactional
    public void testThatPartialUpdateExistingUserReturnsHttpStatus200() throws Exception {
        UserEntity testUserEntityA = TestDataUtil.createTestUserEntityA();
        UserEntity savedUser = userService.save(testUserEntityA);

        UserDto testUserDtoA = userMapper.mapTo(testUserEntityA);
        testUserDtoA.setUsername("UPDATED");
        String userDtoJson = objectMapper.writeValueAsString(testUserDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/users/" + savedUser.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    public void testThatPartialUpdateExistingUserReturnsUpdatedUser() throws Exception {
        UserEntity testUserEntityA = TestDataUtil.createTestUserEntityA();
        UserEntity savedUser = userService.save(testUserEntityA);

        UserDto testUserDtoA = userMapper.mapTo(testUserEntityA);
        testUserDtoA.setUsername("UPDATED");
        String userDtoJson = objectMapper.writeValueAsString(testUserDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/users/" + savedUser.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.userId").value(savedUser.getUserId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.username").value("UPDATED")
        );
    }



    @Test
    @Transactional
    public void testThatDeleteUserReturnsHttpStatus204ForExistingUser() throws Exception {
        UserEntity testUserEntityA = TestDataUtil.createTestUserEntityA();
        UserEntity savedUser = userService.save(testUserEntityA);

        System.out.println("LOGGING = " + testUserEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/" + savedUser.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Transactional
    public void testThatDeleteUserReturnsHttpStatus404ForExistingUser() throws Exception {
        UserEntity testUserEntityA = TestDataUtil.createTestUserEntityA();
        UserEntity savedUser = userService.save(testUserEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/-1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
