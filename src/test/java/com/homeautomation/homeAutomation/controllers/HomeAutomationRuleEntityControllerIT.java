package com.homeautomation.homeAutomation.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeautomation.homeAutomation.config.TestDataUtil;
import com.homeautomation.homeAutomation.controller.util.AuthenticationRequest;
import com.homeautomation.homeAutomation.controller.util.AuthenticationResponse;
import com.homeautomation.homeAutomation.controller.util.RegisterRequest;
import com.homeautomation.homeAutomation.domain.dto.HomeAutomationRuleDto;
import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.GroupEntity;
import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.mapper.Mapper;
import com.homeautomation.homeAutomation.repository.DeviceRepository;
import com.homeautomation.homeAutomation.repository.GroupRepository;
import com.homeautomation.homeAutomation.repository.HomeAutomationRuleRepository;
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

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class HomeAutomationRuleEntityControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userRepository;

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private HomeAutomationRuleRepository ruleRepository;
    @Autowired
    private Mapper<HomeAutomationRuleEntity, HomeAutomationRuleDto> ruleMapper;
    @Autowired
    private UserService userService;

    private UserEntity testUser;
    private HomeAutomationRuleEntity ruleEntityA;
    private GroupEntity groupEntity;
    private DeviceEntity deviceEntityA;
    private DeviceEntity deviceEntityB;
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
    public void testThatCreateRuleSuccessfullyReturnsHttp201Created() throws Exception {
        HomeAutomationRuleDto ruleDtoA = ruleMapper.mapTo(ruleEntityA);
        String ruleJson = objectMapper.writeValueAsString(ruleDtoA);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/rules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(ruleJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    @Transactional
    public void testThatGetRuleByIdReturnsRuleForOwner() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/rules/" + ruleEntityA.getRuleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.ruleId").value(ruleEntityA.getRuleId())
        );
    }

    @Test
    @Transactional
    public void testThatDeleteRuleReturnsHttpStatus204ForOwner() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/rules/" + ruleEntityA.getRuleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }




}
