package com.homeautomation.homeAutomation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeautomation.homeAutomation.config.TestDataUtil;
import com.homeautomation.homeAutomation.controller.util.AuthenticationRequest;
import com.homeautomation.homeAutomation.controller.util.AuthenticationResponse;
import com.homeautomation.homeAutomation.controller.util.RegisterRequest;
import com.homeautomation.homeAutomation.domain.dto.DeviceDto;
import com.homeautomation.homeAutomation.domain.dto.HomeAutomationRuleDto;
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
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.containsInAnyOrder;

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
    @Autowired
    private Mapper<UserEntity, UserDto> userMapper;
    @Autowired
    private Mapper<DeviceEntity, DeviceDto> deviceMapper;

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
        ruleDtoA.setUserDto(null);

        //normally not necessary, only here because of how I set up the TestDataUtil
        ruleDtoA.setRuleId(null);
        if (ruleDtoA.getDeviceDtos() != null) {
            ruleDtoA.getDeviceDtos().forEach(deviceDto -> deviceDto.setDeviceId(null));
        }
        if (ruleDtoA.getGroupDtos() != null) {
            ruleDtoA.getGroupDtos().forEach(groupDto -> groupDto.setGroupId(null));
        }


        String ruleJson = objectMapper.writeValueAsString(ruleDtoA);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/rules/create")
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
                MockMvcRequestBuilders.delete("/rules/delete/" + ruleEntityA.getRuleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    @Transactional
    public void testThatListHomeAutomationRulesReturnsHttpStatus200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/rules/ruleList")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)

        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    public void testThatListHomeAutomationRulesReturnsListOfRules() throws Exception {
        List<HomeAutomationRuleEntity> all = ruleRepository.findAll();

        List<HomeAutomationRuleEntity> usersList = all.stream()
                .filter(rule -> rule.getUserEntity().getUserId().equals(testUser.getUserId()))
                .toList();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/rules/ruleList")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)

        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(greaterThan(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].ruleId").value(usersList.get(0).getRuleId()));
    }

    @Test
    @Transactional
    public void testThatGetEventFromRuleReturnsHttpStatus200() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.get("/rules/Event/"+ ruleEntityA.getRuleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    public void testThatGetEventFromRuleReturnsStringOfEvents() throws Exception{
        String expectedEvent = ruleEntityA.getEvent().toString();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/rules/Event/"+ ruleEntityA.getRuleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(org.hamcrest.Matchers.instanceOf(String.class)))
        .andExpect(MockMvcResultMatchers.content().string(expectedEvent));

    }

    @Test
    @Transactional
    public void testThatGetDevicesAssociatedWithRuleReturnsHttpStatus200 () throws Exception{

        mockMvc.perform(
                MockMvcRequestBuilders.get("/rules/devices/"+ ruleEntityA.getRuleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    public void testThatGetDevicesAssociatedWithRuleReturnsListOfDevices () throws Exception{
    List<DeviceEntity> deviceList = ruleEntityA.getDeviceEntities();

    List<Integer> deviceIds = deviceList.stream()
            .map(device -> device.getDeviceId().intValue())
            .toList();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/rules/devices/"+ ruleEntityA.getRuleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(greaterThan(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].deviceId").value(containsInAnyOrder(deviceIds.toArray())));
    }

    @Test
    @Transactional
    public void testThatPartialUpdateRuleReturnsHttpsStatus200() throws Exception {

        HomeAutomationRuleEntity ruleEntityB = TestDataUtil.createTestRuleEntityB(testUser, groupEntity, List.of(deviceEntityA, deviceEntityB));

        HomeAutomationRuleDto ruleDto = ruleMapper.mapTo(ruleEntityB);

        ruleDto.setRuleId(null);

        String ruleJson = objectMapper.writeValueAsString(ruleDto);


        mockMvc.perform(MockMvcRequestBuilders.post("/rules/update/" + ruleEntityA.getRuleId())
                        .contentType(MediaType.APPLICATION_JSON)
                         .header("Authorization", "Bearer " + jwtToken)
                        .content(ruleJson)
                ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    public void testThatPartialUpdateRuleReturnsUpdatedRule() throws Exception {

        HomeAutomationRuleEntity ruleEntityB = TestDataUtil.createTestRuleEntityB(testUser, groupEntity, List.of(deviceEntityA, deviceEntityB));

        HomeAutomationRuleDto ruleDto = ruleMapper.mapTo(ruleEntityB);

        ruleDto.setRuleId(null);

        String ruleJson = objectMapper.writeValueAsString(ruleDto);


        mockMvc.perform(MockMvcRequestBuilders.patch("/rules/update/" + ruleEntityA.getRuleId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .content(ruleJson)
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.ruleId").value(ruleEntityA.getRuleId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.ruleName").value(ruleEntityA.getRuleName()));
    }

    @Test
    @Transactional
    public void TestThatAddDeviceReturnsHttpStatus200() throws Exception {

        DeviceEntity deviceEntity = TestDataUtil.createDeviceEntityB(testUser);
        deviceEntity.setName("TO BE ADDED");
        deviceEntity.setType(DeviceEntity.DeviceType.CAMERA);
        deviceRepository.save(deviceEntity);

        DeviceDto deviceDto = deviceMapper.mapTo(deviceEntity);
        String deviceJson = objectMapper.writeValueAsString(deviceDto);


        mockMvc.perform(MockMvcRequestBuilders.patch("/rules/update/" + ruleEntityA.getRuleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(deviceJson)
                ).andExpect(MockMvcResultMatchers.status().isOk());
    }







}
