package com.homeautomation.homeAutomation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
import com.homeautomation.homeAutomation.domain.enums.Behaviour;
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

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import java.util.*;


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

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);


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

//        HomeAutomationRuleEntity ruleEntityB = TestDataUtil.createTestRuleEntityB(testUser, groupEntity, List.of(deviceEntityA, deviceEntityB));
//
//        HomeAutomationRuleDto ruleDto = ruleMapper.mapTo(ruleEntityB);
//
//        ruleDto.setRuleId(null);

        HomeAutomationRuleDto ruleDto = new HomeAutomationRuleDto();
        ruleDto.setRuleName("TO BE PARTIALLY UPDATED");

        String ruleJson = objectMapper.writeValueAsString(ruleDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/rules/update/" + ruleEntityA.getRuleId())
                        .contentType(MediaType.APPLICATION_JSON)
                         .header("Authorization", "Bearer " + jwtToken)
                        .content(ruleJson)
                ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    public void testThatPartialUpdateRuleReturnsUpdatedRule() throws Exception {

//        HomeAutomationRuleEntity ruleEntityB = TestDataUtil.createTestRuleEntityB(testUser, groupEntity, List.of(deviceEntityA, deviceEntityB));
//
//        //only necessary because otherwise the properties (device,group,etc) aren't persisted
//        ruleRepository.save(ruleEntityB);
//        HomeAutomationRuleDto ruleDto = ruleMapper.mapTo(ruleEntityA);

//        ruleDto.setRuleId(null);
//        ruleDto.setRuleName("TO BE PARTIALLY UPDATED");

        HomeAutomationRuleDto ruleDto = new HomeAutomationRuleDto();
        ruleDto.setRuleName("TO BE PARTIALLY UPDATED");

        String ruleJson = objectMapper.writeValueAsString(ruleDto);


        mockMvc.perform(MockMvcRequestBuilders.patch("/rules/update/" + ruleEntityA.getRuleId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .content(ruleJson)
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.ruleId").value(ruleEntityA.getRuleId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.ruleName").value("TO BE PARTIALLY UPDATED"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Mock RuleA"));

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

        mockMvc.perform(MockMvcRequestBuilders.patch("/rules/devices/add/" + ruleEntityA.getRuleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(deviceJson)
                ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    public void testAddDeviceReturnsUpdatedDevices() throws Exception {
        DeviceEntity deviceEntity = TestDataUtil.createDeviceEntityB(testUser);
        deviceEntity.setName("TO BE ADDED");
        deviceEntity.setType(DeviceEntity.DeviceType.CAMERA);
        deviceRepository.save(deviceEntity);

        DeviceDto deviceDto = deviceMapper.mapTo(deviceEntity);
        String deviceJson = objectMapper.writeValueAsString(deviceDto);
        System.out.println("Serialized DeviceDto JSON:\n" + deviceJson);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/rules/devices/add/" + ruleEntityA.getRuleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(deviceJson)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(ruleEntityA.getDeviceEntities().size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].deviceId", hasItem(deviceEntity.getDeviceId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", hasItem("TO BE ADDED")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].type", hasItem("CAMERA")))
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        System.out.println("Returned Content \n" + contentAsString);

        DeviceDto[] deviceDtos = objectMapper.readValue(contentAsString, DeviceDto[].class);


        List<Integer> responseDeviceIds = Arrays.stream(deviceDtos)
                .map(device -> device.getDeviceId().intValue())
                .toList();

        List<DeviceEntity> retrievedDeviceEntities = ruleRepository.findById(ruleEntityA.getRuleId())
                .orElseThrow(() -> new RuntimeException("Rule not found with id " + ruleEntityA.getRuleId()))
                .getDeviceEntities();

        List<Integer> expectedDeviceIds = retrievedDeviceEntities.stream()
                .map(DeviceEntity::getDeviceId)
                .map(Long::intValue)
                .toList();

        assertEquals("should have increased by one", expectedDeviceIds.size(), deviceDtos.length);
        assertTrue(responseDeviceIds.containsAll(expectedDeviceIds),
                "Response device IDs should match the repository's device IDs");
    }


    @Test
    @Transactional
    public void TestThatAddBehaviourToDeviceReturnsHttpStatus200() throws Exception {
        Behaviour behaviour = Behaviour.ON;
        String behaviourString = objectMapper.writeValueAsString(behaviour);

        DeviceEntity newDeviceEntity = TestDataUtil.createDeviceEntityB(testUser);
        newDeviceEntity.setName("TO BE ADDED");
        List<HomeAutomationRuleEntity> deviceRules = newDeviceEntity.getRules();
        deviceRules.add(ruleEntityA);
        newDeviceEntity.setRules(new ArrayList<>(deviceRules));
        deviceRepository.save(newDeviceEntity);


//        DeviceEntity deviceEntityFromDB = deviceRepository.findById(newDeviceEntity.getDeviceId()).orElseThrow(() -> new RuntimeException("Device couldn't be retrieved with Id = " + deviceEntityA.getDeviceId()));


        mockMvc.perform(MockMvcRequestBuilders.patch("/rules/" + ruleEntityA.getRuleId( )+ "/devices/addBehaviour/" + newDeviceEntity.getDeviceId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .content(behaviourString)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    @Transactional
    public void TestThatAddBehaviourToDeviceReturnsCorrectlyAlteredDevice() throws Exception {
        Behaviour behaviour = Behaviour.ON;
        String behaviourString = objectMapper.writeValueAsString(behaviour);

        DeviceEntity newDeviceEntity = TestDataUtil.createDeviceEntityB(testUser);
        newDeviceEntity.setName("TO BE ADDED");
        List<HomeAutomationRuleEntity> deviceRules = newDeviceEntity.getRules();
        deviceRules.add(ruleEntityA);
        newDeviceEntity.setRules(new ArrayList<>(deviceRules));
        deviceRepository.save(newDeviceEntity);

        //not really necessary (I think) just troubleshooting
        DeviceEntity deviceEntityFromDB = deviceRepository.findById(newDeviceEntity.getDeviceId()).orElseThrow(() -> new RuntimeException("Device couldn't be retrieved with Id = " + deviceEntityA.getDeviceId()));
        DeviceDto deviceDto = deviceMapper.mapTo(deviceEntityFromDB);

         MvcResult result = mockMvc.perform(MockMvcRequestBuilders.patch("/rules/" + ruleEntityA.getRuleId() + "/devices/addBehaviour/" + newDeviceEntity.getDeviceId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(behaviourString))
                .andExpect(MockMvcResultMatchers.status().isOk())
                 .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        DeviceDto deviceEntityFromEndPoint = objectMapper.readValue(responseContent, DeviceDto.class);

        HomeAutomationRuleEntity updatedRuleEntity = ruleRepository.findById(ruleEntityA.getRuleId())
                .orElseThrow(() -> new RuntimeException("Rule couldn't be retrieved with Id = " + ruleEntityA.getRuleId()));


        Behaviour behaviourFromDevice = updatedRuleEntity.getDeviceBehaviours().get(deviceEntityFromEndPoint.getDeviceId());

        assertEquals("Behavior should be ON", Behaviour.ON, behaviourFromDevice);

//              .andExpect(MockMvcResultMatchers.jsonPath("$.deviceId").value(newDeviceEntity.getDeviceId().intValue()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.behaviour").value("ON"));


//                .andExpect(MockMvcResultMatchers.jsonPath("$.deviceId").value(newDeviceEntity.getDeviceId().intValue()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.behaviour").value("ON"));
    }

//    @Test
//    @Transactional
//    public void TestThatRemoveDeviceFromRuleReturnsHttps200 () throws Exception {
//        List<DeviceEntity> deviceEntities = ruleEntityA.getDeviceEntities();
//        DeviceEntity deviceEntity = deviceEntities.get(0);
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/rules/"+ ruleEntityA.getRuleId()+ "/devices/removeDevice/" + deviceEntity.getDeviceId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .header("Authorization", "Bearer " + jwtToken)
//        ).andExpect(MockMvcResultMatchers.status().isOk());
//    }

    @Test
    @Transactional
    public void TestThatRemoveDeviceFromRuleReturnsHttps200() throws Exception {;

        DeviceEntity deviceEntity = TestDataUtil.createDeviceEntityB(testUser);
        HomeAutomationRuleEntity ruleEntityB;
        deviceRepository.save(deviceEntity);
        ruleEntityB = TestDataUtil.createTestRuleEntityB(testUser, groupEntity, new ArrayList<>(List.of(deviceEntityA, deviceEntity)));

        deviceEntity.setRules(new ArrayList<>(List.of(ruleEntityB)));
        ruleRepository.save(ruleEntityB);



        mockMvc.perform(MockMvcRequestBuilders.delete("/rules/" + ruleEntityB.getRuleId() + "/devices/removeDevice/" + deviceEntity.getDeviceId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtToken)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    public void TestThatRemoveDeviceFromRuleReturnsRuleWithoutToBeRemovedDevice() throws Exception {
        DeviceEntity deviceEntity = TestDataUtil.createDeviceEntityB(testUser);
        HomeAutomationRuleEntity ruleEntityB;
        deviceRepository.save(deviceEntity);
        ruleEntityB = TestDataUtil.createTestRuleEntityB(testUser, groupEntity, new ArrayList<>(List.of(deviceEntityA, deviceEntity)));

        deviceEntity.setRules(new ArrayList<>(List.of(ruleEntityB)));
        ruleRepository.save(ruleEntityB);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/rules/" + ruleEntityB.getRuleId() + "/devices/removeDevice/" + deviceEntity.getDeviceId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.deviceDtos").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.deviceDtos[*].deviceId", not(hasItem(deviceEntity.getDeviceId().intValue()))))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        HomeAutomationRuleDto updatedRule = objectMapper.readValue(responseBody, HomeAutomationRuleDto.class);

        assertFalse(updatedRule.getDeviceDtos().stream()
                        .anyMatch(deviceDto -> deviceDto.getDeviceId().equals(deviceEntity.getDeviceId())),
                "Device should have been removed from the rule's device list.");

        DeviceEntity updatedDevice = deviceRepository.findById(deviceEntity.getDeviceId()).orElseThrow(() -> new RuntimeException("Device not found"));
        assertFalse(updatedDevice.getRules().contains(ruleEntityB),
                "Rule should have been removed from the device's rules list.");
    }


}
