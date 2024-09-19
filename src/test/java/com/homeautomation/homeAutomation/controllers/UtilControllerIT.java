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
    @Transactional
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

}
