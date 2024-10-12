package com.homeautomation.homeAutomation.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeautomation.homeAutomation.config.TestDataUtil;
import com.homeautomation.homeAutomation.controller.util.AuthenticationRequest;
import com.homeautomation.homeAutomation.controller.util.AuthenticationResponse;
import com.homeautomation.homeAutomation.controller.util.RegisterRequest;
import com.homeautomation.homeAutomation.domain.dto.DeviceDto;
import com.homeautomation.homeAutomation.domain.dto.HomeAutomationRuleDto;
import com.homeautomation.homeAutomation.domain.dto.UserDto;
import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.mapper.Mapper;
import com.homeautomation.homeAutomation.repository.DeviceRepository;
import com.homeautomation.homeAutomation.repository.UserRepository;
import com.homeautomation.homeAutomation.services.UserService;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class DeviceEntityControllerIT {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Mapper<DeviceEntity, DeviceDto> deviceMapper;
    @Autowired
    private Mapper<UserEntity, UserDto> userMapper;


    private UserEntity testUser;
    private DeviceEntity deviceEntityA;
    private String jwtToken;


    @BeforeEach
    @Transactional
    void setUp() throws Exception {

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

        testUser = userService.findByUsername(testUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));


        deviceEntityA = TestDataUtil.createDeviceEntityA(testUser);
        deviceRepository.save(deviceEntityA);

    }

    @Test
    @Transactional
    public void TestThatGetDeviceByIdReturnsHttp200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/devices/" + deviceEntityA.getDeviceId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    @Transactional
    public void TestThatGetDeviceByIdReturnsCorrectDevice() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/devices/" + deviceEntityA.getDeviceId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.deviceId").value(deviceEntityA.getDeviceId())
        );
    }

    @Test
    @Transactional
    public void testThatCreateDeviceSuccessfullyReturnsHttp201Created() throws Exception {
        DeviceDto deviceDto = deviceMapper.mapTo(deviceEntityA);
        deviceDto.setDeviceId(null);
//        UserDto userDto = userMapper.mapTo(testUser);
//        deviceDto.setUserDto(userDto);

        String deviceJson = objectMapper.writeValueAsString(deviceDto);
        System.out.println("THIS IS DEVICE JSON" + deviceJson);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/devices/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(deviceJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    @Transactional
    public void testThatCreateDeviceSuccessfullyReturnsCreatedDevice() throws Exception {
        DeviceDto deviceDto = deviceMapper.mapTo(deviceEntityA);
        deviceDto.setDeviceId(null);

        String deviceJson = objectMapper.writeValueAsString(deviceDto);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/devices/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(deviceJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        DeviceDto returnedDeviceDto = objectMapper.readValue(contentAsString, DeviceDto.class);
        DeviceEntity deviceEntityFromDB = deviceRepository.findById(returnedDeviceDto.getDeviceId()).orElseThrow(() -> new RuntimeException("Device could not be retrieved from the DB"));

        assertEquals("should be equal", deviceEntityA.getName(), deviceEntityFromDB.getName());
        assertEquals("should be equal", deviceEntityA.getType(), deviceEntityFromDB.getType());
    }


    @Test
    @Transactional
    public void TestThatPartialUpdateReturnsHttp200() throws Exception {

        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setName("TO BE PARTIALLY UPDATED");

        String deviceJson = objectMapper.writeValueAsString(deviceDto);


        mockMvc.perform(MockMvcRequestBuilders.patch("/devices/update/" + deviceEntityA.getDeviceId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .content(deviceJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    public void TestThatPartialUpdateReturnsUpdatedDevice() throws Exception {

        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setName("TO BE PARTIALLY UPDATED");

        String deviceJson = objectMapper.writeValueAsString(deviceDto);


        mockMvc.perform(MockMvcRequestBuilders.patch("/devices/update/" + deviceEntityA.getDeviceId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(deviceJson)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.deviceId").value(deviceEntityA.getDeviceId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TO BE PARTIALLY UPDATED"));
    }


    @Test
    @Transactional
    public void TestThatDeleteDeviceReturnsHttp204() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/devices/delete/" + deviceEntityA.getDeviceId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}
