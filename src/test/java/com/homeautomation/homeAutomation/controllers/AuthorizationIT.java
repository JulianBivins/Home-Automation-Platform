package com.homeautomation.homeAutomation.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeautomation.homeAutomation.config.TestDataUtil;
import com.homeautomation.homeAutomation.controller.util.AuthenticationRequest;
import com.homeautomation.homeAutomation.controller.util.AuthenticationResponse;
import com.homeautomation.homeAutomation.controller.util.RegisterRequest;
import com.homeautomation.homeAutomation.domain.dto.UserDto;
import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.GroupEntity;
import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.mapper.Mapper;
import com.homeautomation.homeAutomation.repository.DeviceRepository;
import com.homeautomation.homeAutomation.repository.GroupRepository;
import com.homeautomation.homeAutomation.repository.HomeAutomationRuleRepository;
import com.homeautomation.homeAutomation.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class AuthorizationIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Mapper<UserEntity, UserDto> userMapper;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private HomeAutomationRuleRepository ruleRepository;

    private String jwtToken;
    private UserEntity testUser;
    private HomeAutomationRuleEntity ruleEntityA;
    private GroupEntity groupEntity;
    private DeviceEntity deviceEntityA;
    private DeviceEntity deviceEntityB;



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

        groupEntity = TestDataUtil.createGroupEntityA(testUser);
        groupRepository.save(groupEntity);

        deviceEntityA = TestDataUtil.createDeviceEntityA(testUser);
        deviceEntityB = TestDataUtil.createDeviceEntityB(testUser);
        deviceRepository.saveAll(List.of(deviceEntityA, deviceEntityB));

        ruleEntityA = TestDataUtil.createTestRuleEntityA(testUser, groupEntity, new ArrayList<>(List.of(deviceEntityA, deviceEntityB)));
        ruleRepository.save(ruleEntityA);
    }


    @Test
    @Transactional
    public void testThatDeleteUserReturnsHttpStatus403WhenUserIsNotAuthorized() throws Exception {
        UserEntity anotherUser = TestDataUtil.createTestUserEntityB();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(anotherUser.getUsername());
        registerRequest.setPassword(anotherUser.getPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        anotherUser = userService.findByUsername(anotherUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + anotherUser.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @Transactional
    public void testThatFullUpdateReturnsHttpStatus403WhenUserIsNotAuthorized() throws Exception {
        UserEntity anotherUser = TestDataUtil.createTestUserEntityB();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(anotherUser.getUsername());
        registerRequest.setPassword(anotherUser.getPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        anotherUser = userService.findByUsername(anotherUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDto userDto = userMapper.mapTo(anotherUser);
        userDto.setUsername("UpdatedUsername");
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/" + anotherUser.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(userDtoJson))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @Transactional
    public void testThatPartialUpdateReturnsHttpStatus403WhenUserIsNotAuthorized() throws Exception {
        UserEntity anotherUser = TestDataUtil.createTestUserEntityB();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(anotherUser.getUsername());
        registerRequest.setPassword(anotherUser.getPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        anotherUser = userService.findByUsername(anotherUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDto userDto = new UserDto();
        userDto.setUsername("PartiallyUpdatedUsername");
        String userDtoJson = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/" + anotherUser.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(userDtoJson))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @Transactional
    public void testThatGetCurrentUserReturnsHttpStatus401WhenUserIsUnauthenticated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @Transactional
    public void testThatDeleteRuleReturnsHttpStatus403ForNonOwner() throws Exception {
        UserEntity nonOwner = TestDataUtil.createTestUserEntityB();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(nonOwner.getUsername());
        registerRequest.setPassword(nonOwner.getPassword());


        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        nonOwner = userService.findByUsername(nonOwner.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        GroupEntity anotherGroupEntity = TestDataUtil.createGroupEntityA(nonOwner);
        groupRepository.save(anotherGroupEntity);

        DeviceEntity anotherDeviceEntityA = TestDataUtil.createDeviceEntityA(nonOwner);
        DeviceEntity anotherDeviceEntityB = TestDataUtil.createDeviceEntityB(nonOwner);
        deviceRepository.saveAll(List.of(anotherDeviceEntityA, anotherDeviceEntityB));

        HomeAutomationRuleEntity anotherRuleEntity = TestDataUtil.createTestRuleEntityA(nonOwner, anotherGroupEntity, new ArrayList<>(List.of(anotherDeviceEntityA, anotherDeviceEntityB)));
        ruleRepository.save(anotherRuleEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/rules/" + anotherRuleEntity.getRuleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken) //test user token
        ).andExpect(
                MockMvcResultMatchers.status().isForbidden()
        );
    }


    @Test
    @Transactional
    public void testThatUserCannotAccessAnotherUsersRule() throws Exception {

        UserEntity anotherUser = TestDataUtil.createTestUserEntityB();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(anotherUser.getUsername());
        registerRequest.setPassword(anotherUser.getPassword());


        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        anotherUser = userService.findByUsername(anotherUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        GroupEntity anotherGroupEntity = TestDataUtil.createGroupEntityA(anotherUser);
        groupRepository.save(anotherGroupEntity);

        DeviceEntity anotherDeviceEntityA = TestDataUtil.createDeviceEntityA(anotherUser);
        DeviceEntity anotherDeviceEntityB = TestDataUtil.createDeviceEntityB(anotherUser);
        deviceRepository.saveAll(List.of(anotherDeviceEntityA, anotherDeviceEntityB));

        HomeAutomationRuleEntity anotherRuleEntity = TestDataUtil.createTestRuleEntityA(anotherUser, anotherGroupEntity, new ArrayList<>(List.of(anotherDeviceEntityA, anotherDeviceEntityB)));
        ruleRepository.save(anotherRuleEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/rules/" + anotherRuleEntity.getRuleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
        ).andExpect(
                MockMvcResultMatchers.status().isForbidden()
        );
    }
}
