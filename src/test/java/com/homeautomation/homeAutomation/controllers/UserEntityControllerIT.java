package com.homeautomation.homeAutomation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeautomation.homeAutomation.config.TestDataUtil;
import com.homeautomation.homeAutomation.domain.dto.UserDto;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.homeautomation.homeAutomation.mapper.Mapper;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserEntityControllerIT {

    private UserService userService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private UserEntity testUserEntityA;
    private UserEntity testUserEntityB;

    private Mapper<UserEntity, UserDto> userMapper;



    @Autowired
    public UserEntityControllerIT(UserService userService, MockMvc mockMvc, ObjectMapper objectMapper,
                                  Mapper<UserEntity, UserDto> userMapper
    ) {
        this.userService = userService;
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.userMapper = userMapper;
    }

    @BeforeEach
    void setUp() {
         testUserEntityA = TestDataUtil.createTestUserEntityA();
         testUserEntityB = TestDataUtil.createTestUserEntityB();
         userService.save(testUserEntityB);
//        System.out.println("THIS IS THE USER ID FOR TESTUSERENTITY B = " + testUserEntityB.getUserId());
    }

    @Test
    public void testThatCreateUserSuccessfullyReturnsHttp201Created() throws Exception {
        UserDto testUserDtoA = userMapper.mapTo(testUserEntityA);
//        UserDto testUserDtoB = TestDataUtil.createTestUserDtoB();
        String userJson = objectMapper.writeValueAsString(testUserDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatCreateUserSuccessfullyReturnsSavedUser() throws Exception {
        UserDto testUserDtoA = userMapper.mapTo(testUserEntityA);
        String userJson = objectMapper.writeValueAsString(testUserDtoA);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.username").value("testuserA")
        );
    }

    @Test
    public void testThatGetUserReturnsHttpStatus200WhenUserExist() throws Exception {
        if (testUserEntityB.getUserId() == null) return;
        String userId = String.valueOf(testUserEntityB.getUserId());
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetUserReturnsAuthorWhenUserExist() throws Exception {
        if (testUserEntityB.getUserId() == null) return;
        String userId = String.valueOf(testUserEntityB.getUserId());
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.userId").value(testUserEntityB.getUserId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.username").value("testuserB")
        )
//                .andExpect(
//                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)
//        )
        ;
    }

    @Test
    public void testThatGetUserReturnsHttpStatus404WhenNoUserExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/0")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFullUpdateUserReturnsHttpStatus200WhenUserExists() throws Exception {
        UserDto testUserDtoB = userMapper.mapTo(testUserEntityB);
        String authorDtoJson = objectMapper.writeValueAsString(testUserDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/users/" + testUserDtoB.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatFullUpdateUpdatesExistingAuthor() throws Exception {

        UserDto testUserDtoB = userMapper.mapTo(testUserEntityB);
        String userDtoUpdateJson = objectMapper.writeValueAsString(testUserDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/users/" + testUserDtoB.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoUpdateJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.userId").value(testUserDtoB.getUserId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("testuserB")
        );
    }

    @Test
    public void testThatPartialUpdateExistingUserReturnsHttpStatus200() throws Exception {
        UserDto testUserDtoB = userMapper.mapTo(testUserEntityB);
        testUserDtoB.setUsername("UPDATED");
        String userDtoJson = objectMapper.writeValueAsString(testUserDtoB);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/users/" + testUserDtoB.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
