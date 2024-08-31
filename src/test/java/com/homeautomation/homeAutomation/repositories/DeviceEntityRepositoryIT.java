package com.homeautomation.homeAutomation.repositories;

import com.homeautomation.homeAutomation.config.TestDataUtil;
import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.GroupEntity;
import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.repository.DeviceRepository;
import com.homeautomation.homeAutomation.repository.GroupRepository;
import com.homeautomation.homeAutomation.repository.HomeAutomationRuleRepository;
import com.homeautomation.homeAutomation.repository.UserRepository;
import com.homeautomation.homeAutomation.services.DeviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DeviceEntityRepositoryIT {

    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private HomeAutomationRuleRepository ruleRepository;
    @Autowired
    private GroupRepository groupRepository;

    private DeviceEntity deviceEntityA;
    private DeviceEntity deviceEntityB;
    private UserEntity userEntity;
    private GroupEntity groupEntity;
    private HomeAutomationRuleEntity ruleEntity;

    @BeforeEach
    @Transactional
    public void setUp() {
        userEntity = TestDataUtil.createTestUserEntityA();
        userRepository.save(userEntity);

        deviceEntityA = TestDataUtil.createDeviceEntityA(userEntity);
        deviceEntityB = TestDataUtil.createDeviceEntityB(userEntity);
        deviceRepository.saveAll(List.of(deviceEntityA, deviceEntityB));

        groupEntity = TestDataUtil.createGroupEntityA(userEntity);
        groupRepository.save(groupEntity);

        ruleEntity = TestDataUtil.createTestRuleEntityA(userEntity, groupEntity, new ArrayList<>(List.of(deviceEntityA, deviceEntityB)));
        ruleRepository.save(ruleEntity);
    }

    @Test
    @Transactional
    public void testThatGroupCanBeCreatedAndRecalled() {
        Optional<DeviceEntity> result = deviceRepository.findById(deviceEntityA.getDeviceId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(deviceEntityA);
    }


    @Test
    @Transactional
    public void testDeleteDeviceEntity() {
        Optional<DeviceEntity> retrievedDevice = deviceRepository.findById(deviceEntityA.getDeviceId());
        assertThat(retrievedDevice).isPresent();
        deviceRepository.delete(retrievedDevice.get());
        Optional<DeviceEntity> retrievedRuleAfterDeletion = deviceRepository.findById(deviceEntityA.getDeviceId());
        assertThat(retrievedRuleAfterDeletion).isNotPresent();
    }

    @Test @Transactional public void testDeleteDeviceEntityById() {
        Optional<DeviceEntity> retrievedDevice = deviceRepository.findById(deviceEntityA.getDeviceId());
        assertThat(retrievedDevice).isPresent();
        deviceRepository.deleteById(retrievedDevice.get().getDeviceId());
        Optional<DeviceEntity> retrievedRuleAfterDeletion = deviceRepository.findById(deviceEntityA.getDeviceId());
        assertThat(retrievedRuleAfterDeletion).isNotPresent();
    }

    @Test
    @Transactional
    public void testThatMultipleDevicesCanBeCreatedAndRecalled() {
        Optional<DeviceEntity> retrievedDeviceA = deviceRepository.findById(deviceEntityA.getDeviceId());
        assertThat(retrievedDeviceA).isPresent();

        Optional<DeviceEntity> retrievedDeviceB = deviceRepository.findById(deviceEntityB.getDeviceId());
        assertThat(retrievedDeviceB).isPresent();

        deviceRepository.deleteById(retrievedDeviceA.get().getDeviceId());
        deviceRepository.deleteById(retrievedDeviceA.get().getDeviceId());
        deviceRepository.flush();

        assertThat(deviceRepository.findById(retrievedDeviceA.get().getDeviceId())).isNotPresent();
        assertThat(deviceRepository.findById(retrievedDeviceB.get().getDeviceId())).isNotPresent();


        DeviceEntity deviceEntityA = TestDataUtil.createDeviceEntityA(userEntity);
        deviceRepository.save(deviceEntityA);
        DeviceEntity deviceEntityB = TestDataUtil.createDeviceEntityB(userEntity);
        deviceRepository.save(deviceEntityB);

        List<DeviceEntity> resultEntities = deviceRepository.findAll();
        assertThat(resultEntities).hasSize(2).containsExactly(deviceEntityA, deviceEntityB);
    }



    @Test
    @Transactional
    public void testUpdateDeviceEntity() {

        Optional<DeviceEntity> retrievedDevice = deviceRepository.findById(deviceEntityA.getDeviceId());
        assertThat(retrievedDevice).isPresent();

        DeviceEntity updatedDeviceEntity = TestDataUtil.createDeviceEntityB(userEntity);
        updatedDeviceEntity.setName("testDeviceB");

        DeviceEntity deviceEntityAfterPartialUpdate = deviceService.partialUpdate(retrievedDevice.get().getDeviceId(), updatedDeviceEntity);
        deviceRepository.save(deviceEntityAfterPartialUpdate);

        Optional<DeviceEntity> retrievedDeviceAfterUpdate = deviceRepository.findById(deviceEntityAfterPartialUpdate.getDeviceId());
        assertThat(retrievedDeviceAfterUpdate).isPresent();

        assertThat(retrievedDeviceAfterUpdate.get()).isEqualTo(updatedDeviceEntity);
        assertThat(retrievedDevice.get()).isNotEqualTo(retrievedDeviceAfterUpdate.get());

    }

    @Test
    @Transactional
    public void testDeviceToUserAssociation() {
        Optional<DeviceEntity> retrievedDevice = deviceRepository.findById(deviceEntityA.getDeviceId());
        assertThat(retrievedDevice).isPresent();

        UserEntity userEntityB = TestDataUtil.createTestUserEntityB();
        userRepository.save(userEntityB);

        retrievedDevice.get().setUserEntity(userEntityB);

        deviceRepository.save(retrievedDevice.get());

        Optional<DeviceEntity> retrievedGroupAfterSettingNewRule = deviceRepository.findById(retrievedDevice.get().getDeviceId());
        assertThat(retrievedGroupAfterSettingNewRule).isPresent();
        assertThat(retrievedGroupAfterSettingNewRule.get().getUserEntity()).isEqualTo(userEntityB);
    }

    @Test
    @Transactional
    public void testDeviceToHomeAutomationRuleAdditionAndAssociation() {
        Optional<DeviceEntity> retrievedDevice = deviceRepository.findById(deviceEntityA.getDeviceId());
        assertThat(retrievedDevice).isPresent();

        List<HomeAutomationRuleEntity> retrievedRules = retrievedDevice.get().getRules();
        if (retrievedRules == null) {
            retrievedRules = new ArrayList<>();
        }

        retrievedRules.add(ruleEntity);
        retrievedDevice.get().setRules(new ArrayList<>(retrievedRules));

//        deviceRepository.save(retrievedDevice.get());

        Optional<DeviceEntity> updatedDevice = deviceRepository.findById(deviceEntityA.getDeviceId());
        assertThat(updatedDevice).isPresent();

        assertThat(retrievedDevice.get().getRules()).contains(ruleEntity);
    }

    @Test
    @Transactional
    public void testRemoveHomeAutomationRuleFromDevice() {
        Optional<DeviceEntity> retrievedDevice = deviceRepository.findById(deviceEntityA.getDeviceId());
        assertThat(retrievedDevice).isPresent();

        List<HomeAutomationRuleEntity> retrievedRules = retrievedDevice.get().getRules();
        if (retrievedRules == null) {
            retrievedRules = new ArrayList<>();
        }
        retrievedRules.add(ruleEntity);
        retrievedDevice.get().setRules(new ArrayList<>(retrievedRules));
//        deviceRepository.save(retrievedDevice.get());

        retrievedRules.remove(ruleEntity);
        retrievedDevice.get().setRules(retrievedRules);
        deviceRepository.save(retrievedDevice.get());

        Optional<DeviceEntity> updatedDevice = deviceRepository.findById(deviceEntityA.getDeviceId());
        assertThat(updatedDevice).isPresent();
        assertThat(updatedDevice.get().getRules()).doesNotContain(ruleEntity);
    }

    @Test
    @Transactional
    public void testFindDevicesByUserId() {
        List<DeviceEntity> devices = deviceRepository.findByUserEntity_UserId(userEntity.getUserId());
        assertThat(devices).hasSize(2).containsExactly(deviceEntityA, deviceEntityB);
    }

    @Test
    @Transactional
    public void testFindDevicesByRuleId() {
        List<DeviceEntity> devices = deviceRepository.findByRules_RuleId(ruleEntity.getRuleId());
        assertThat(devices).hasSize(2).containsExactly(deviceEntityA, deviceEntityB);
    }

}






