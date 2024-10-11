package com.homeautomation.homeAutomation.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeautomation.homeAutomation.config.TestDataUtil;
import com.homeautomation.homeAutomation.controller.util.AuthenticationRequest;
import com.homeautomation.homeAutomation.controller.util.AuthenticationResponse;
import com.homeautomation.homeAutomation.controller.util.RegisterRequest;
import com.homeautomation.homeAutomation.domain.dto.GroupDto;
import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.GroupEntity;
import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.mapper.Mapper;
import com.homeautomation.homeAutomation.repository.DeviceRepository;
import com.homeautomation.homeAutomation.repository.GroupRepository;
import com.homeautomation.homeAutomation.repository.HomeAutomationRuleRepository;
import com.homeautomation.homeAutomation.services.DeviceService;
import com.homeautomation.homeAutomation.services.GroupService;
import com.homeautomation.homeAutomation.services.HomeAutomationRuleService;
import com.homeautomation.homeAutomation.services.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class GroupEntityControllerIT {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private Mapper<GroupEntity, GroupDto> groupMapper;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private HomeAutomationRuleRepository ruleRepository;
    @Autowired
    private DeviceRepository deviceRepository;


    private GroupEntity groupEntityA;
    private UserEntity testUser;
    private String jwtToken;
    private DeviceEntity deviceEntityA;
    private DeviceEntity deviceEntityB;
    private HomeAutomationRuleEntity ruleEntity;

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

        groupEntityA = TestDataUtil.createGroupEntityA(testUser);
        groupService.save(groupEntityA);


        deviceEntityA = TestDataUtil.createDeviceEntityA(testUser);
        deviceEntityB = TestDataUtil.createDeviceEntityB(testUser);
        deviceRepository.saveAll(List.of(deviceEntityA,deviceEntityB));

        ruleEntity = TestDataUtil.createTestRuleEntityA(testUser, groupEntityA, new ArrayList<>(List.of(deviceEntityA, deviceEntityB)));
        ruleRepository.save(ruleEntity);

    }

    @Test
    @Transactional
    public void TestThatGetGroupByIdReturnsHttp200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/groups/" + groupEntityA.getGroupId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    @Transactional
    public void TestThatGetGroupByIdReturnsCorrectGroup() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/groups/" + groupEntityA.getGroupId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.groupId").value(groupEntityA.getGroupId())
        );
    }

    @Test
    @Transactional
    public void testThatCreateDeviceSuccessfullyReturnsHttp201Created() throws Exception {
        GroupDto groupDto = groupMapper.mapTo(groupEntityA);
        groupDto.setGroupId(null);

        String deviceJson = objectMapper.writeValueAsString(groupDto);
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
        GroupDto groupDto = groupMapper.mapTo(groupEntityA);
        groupDto.setGroupId(null);

        String deviceJson = objectMapper.writeValueAsString(groupDto);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/groups/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(deviceJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        GroupDto returnedGroupDto = objectMapper.readValue(contentAsString, GroupDto.class);
        GroupEntity groupEntityFromDB = groupRepository.findById(returnedGroupDto.getGroupId()).orElseThrow(() -> new RuntimeException("Group could not be retrieved from the DB"));

        assertEquals("should be equal", groupEntityA.getName(), groupEntityFromDB.getName());
        assertEquals("should be equal", groupEntityA.getUserEntity(), groupEntityFromDB.getUserEntity());
    }

    @Test
    @Transactional
    public void TestThatPartialUpdateReturnsHttp200() throws Exception {

        GroupDto groupDto = new GroupDto();
        groupDto.setName("TO BE PARTIALLY UPDATED");
        //should I really always include the ID? I want to actually only pass the necessary value
        groupDto.setGroupId(groupEntityA.getGroupId());

        String groupJson = objectMapper.writeValueAsString(groupDto);


        mockMvc.perform(MockMvcRequestBuilders.patch("/groups/update/" + groupEntityA.getGroupId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .content(groupJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    public void TestThatPartialUpdateReturnsUpdatedGroup() throws Exception {

        GroupDto groupDto = new GroupDto();
        groupDto.setName("TO BE PARTIALLY UPDATED");
        //should I really always include the ID? I want to actually only pass the necessary value
        groupDto.setGroupId(groupEntityA.getGroupId());
        String groupJson = objectMapper.writeValueAsString(groupDto);


        mockMvc.perform(MockMvcRequestBuilders.patch("/groups/update/" + groupEntityA.getGroupId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(groupJson)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.groupId").value(groupEntityA.getGroupId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TO BE PARTIALLY UPDATED"));
    }


    @Test
    @Transactional
    public void TestThatDeleteGroupReturnsHttp204() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/groups/delete/" + groupEntityA.getGroupId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    @Transactional
    public void TestThatAddRuleToGroupReturnsHttp200() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.patch("/groups/" + groupEntityA.getGroupId()+"/addRule/"+ ruleEntity.getRuleId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtToken)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }





}
