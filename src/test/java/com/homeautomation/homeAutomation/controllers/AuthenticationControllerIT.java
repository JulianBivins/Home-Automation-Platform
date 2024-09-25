package com.homeautomation.homeAutomation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeautomation.homeAutomation.config.TestDataUtil;
import com.homeautomation.homeAutomation.controller.util.AuthenticationRequest;
import com.homeautomation.homeAutomation.controller.util.AuthenticationResponse;
import com.homeautomation.homeAutomation.controller.util.RegisterRequest;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.services.AuthenticationService;
import jakarta.transaction.Transactional;
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

import java.util.stream.Collectors;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthenticationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthenticationService authenticationService;

    private UserEntity testUser;

    private RegisterRequest registerRequest;

    private static int counter;

    @BeforeEach
    @Transactional
    public void setUp() throws Exception {
        testUser = TestDataUtil.createTestUserEntityA();
        registerRequest = new RegisterRequest();
        registerRequest.setUsername(testUser.getUsername());
        registerRequest.setPassword(testUser.getPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    public void testThatRegisterReturnsHttpStatus200WhenRegistrationIsSuccessful() throws Exception {
        UserEntity testUserForMethod = TestDataUtil.createTestUserEntityA();
        testUserForMethod.setUsername("ForTestMethod" + counter++);
        RegisterRequest registerRequestForMethod = new RegisterRequest();
        registerRequestForMethod.setUsername(testUserForMethod.getUsername());
        registerRequestForMethod.setPassword(testUserForMethod.getPassword());

        mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestForMethod))
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    public void testThatRegisterReturnsTokenOnSuccessfulRegistration() throws Exception {
        UserEntity testUserForMethod = TestDataUtil.createTestUserEntityA();
        testUserForMethod.setUsername("ForTestMethod" + counter++);
        RegisterRequest registerRequestForMethod = new RegisterRequest();
        registerRequestForMethod.setUsername(testUserForMethod.getUsername());
        registerRequestForMethod.setPassword(testUserForMethod.getPassword());

        mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestForMethod))
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.token").exists()
        );
    }

    @Test
    @Transactional
    public void testThatLoginReturnsTokenOnSuccessfulAuthentication() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername(testUser.getUsername());
        request.setPassword(testUser.getPassword());
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists());
    }


}