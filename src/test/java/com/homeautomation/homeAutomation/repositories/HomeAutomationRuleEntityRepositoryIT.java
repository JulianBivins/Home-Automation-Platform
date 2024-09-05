package com.homeautomation.homeAutomation.repositories;

import com.homeautomation.homeAutomation.config.TestDataUtil;
import com.homeautomation.homeAutomation.domain.entities.*;
import com.homeautomation.homeAutomation.repository.*;
import com.homeautomation.homeAutomation.services.HomeAutomationRuleService;
import com.homeautomation.homeAutomation.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class HomeAutomationRuleEntityRepositoryIT {

    @Autowired
    private HomeAutomationRuleRepository ruleRepository;

    @Autowired
    private HomeAutomationRuleService ruleService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private DeviceRepository deviceRepository;


    private HomeAutomationRuleEntity ruleEntity;
    private UserEntity userEntity;
    private GroupEntity groupEntity;
    private DeviceEntity deviceEntityA;
    private DeviceEntity deviceEntityB;


    @BeforeEach
    @Transactional
    public void setUp() {
        userEntity = TestDataUtil.createTestUserEntityA();
        userRepository.save(userEntity);
        groupEntity = TestDataUtil.createGroupEntityA(userEntity);
        groupRepository.save(groupEntity);
        deviceEntityA = TestDataUtil.createDeviceEntityA(userEntity);
        deviceEntityB = TestDataUtil.createDeviceEntityB(userEntity);
        deviceRepository.saveAll(List.of(deviceEntityA, deviceEntityB));

        ruleEntity = TestDataUtil.createTestRuleEntityA(userEntity, groupEntity, new ArrayList<>(List.of(deviceEntityA, deviceEntityB)));
        ruleRepository.save(ruleEntity);

    }

    @Test
    @Transactional
    public void testThatHomeAutomationRuleCanBeCreatedAndRecalled() {
        Optional<HomeAutomationRuleEntity> result = ruleRepository.findById(ruleEntity.getRuleId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(ruleEntity);
    }

    @Test
    @Transactional
    public void testThatMultipleRulesCanBeCreatedAndRecalled() {
        ruleRepository.delete(ruleEntity);
        assertThat(ruleRepository.findById(ruleEntity.getRuleId())).isNotPresent();

        HomeAutomationRuleEntity ruleEntityA = TestDataUtil.createTestRuleEntityA(userEntity, groupEntity, new ArrayList<>(List.of(deviceEntityA, deviceEntityB)));
        ruleRepository.save(ruleEntityA);
        HomeAutomationRuleEntity ruleEntityB = TestDataUtil.createTestRuleEntityB(userEntity, groupEntity, new ArrayList<>(List.of(deviceEntityA, deviceEntityB)));
        ruleRepository.save(ruleEntityB);
        HomeAutomationRuleEntity ruleEntityC = TestDataUtil.createTestRuleEntityC(userEntity,groupEntity, new ArrayList<>(List.of(deviceEntityA, deviceEntityB)));
        ruleRepository.save(ruleEntityC);

        List<HomeAutomationRuleEntity> resultEntities = ruleRepository.findAll();
        assertThat(resultEntities).hasSize(3).containsExactly(ruleEntityA, ruleEntityB, ruleEntityC);
    }

    @Test
    @Transactional
    public void testFindHomaAutomationRuleByID() {
        Optional<HomeAutomationRuleEntity> retrievedRule = ruleRepository.findById(ruleEntity.getRuleId());
        assertThat(retrievedRule).isPresent();
    }

    @Test
    @Transactional
    public void testFindRuleByName() {
        Optional<HomeAutomationRuleEntity> retrievedRule = ruleRepository.findByRuleName(ruleEntity.getRuleName());
        assertThat(retrievedRule).isPresent();
        retrievedRule.ifPresent(user -> {
            assertThat(user.getRuleName()).isEqualTo("RuleA");
        });
    }

    @Test
    @Transactional
    public void testDeleteRuleEntity(){
        Optional<HomeAutomationRuleEntity> retrievedRule = ruleRepository.findById(ruleEntity.getRuleId());
        assertThat(retrievedRule).isPresent();

        ruleRepository.delete(retrievedRule.get());
        Optional<HomeAutomationRuleEntity> retrievedRuleAfterDeletion = ruleRepository.findById(ruleEntity.getRuleId());
        assertThat(retrievedRuleAfterDeletion).isNotPresent();
    }

    @Test
    @Transactional
    public void testDeleteHomeAutomationRuleEntityById(){
        Optional<HomeAutomationRuleEntity> retrievedRule = ruleRepository.findById(ruleEntity.getRuleId());
        assertThat(retrievedRule).isPresent();

        ruleRepository.deleteById(retrievedRule.get().getRuleId());
        Optional<HomeAutomationRuleEntity> retrievedRuleAfterDeletion = ruleRepository.findById(ruleEntity.getRuleId());
        assertThat(retrievedRuleAfterDeletion).isNotPresent();
    }

    @Test
    @Transactional
    public void testThatHomeAutomationRuleCanBeUpdated () {
        Optional<HomeAutomationRuleEntity> retrievedRule = ruleRepository.findById(ruleEntity.getRuleId());
        assertThat(retrievedRule).isPresent();

        HomeAutomationRuleEntity updatedRuleEntity = TestDataUtil.createTestRuleEntityB(userEntity, groupEntity, new ArrayList<>(List.of(deviceEntityA, deviceEntityB)));
        updatedRuleEntity.setRuleName("testRuleB");

        HomeAutomationRuleEntity ruleEntityAfterPartialUpdate = ruleService.partialUpdate(ruleEntity.getRuleId(), updatedRuleEntity);

        Optional<HomeAutomationRuleEntity> retrievedRuleAfterUpdate = ruleRepository.findById(ruleEntityAfterPartialUpdate.getRuleId());
        assertThat(retrievedRuleAfterUpdate).isPresent();

        assertThat(retrievedRuleAfterUpdate.get()).isEqualTo(ruleEntityAfterPartialUpdate);
//        assertThat(retrievedRule.get()).isNotEqualTo(retrievedRuleAfterUpdate.get());
    }


    @Test
    @Transactional
    public void testHomeAutomationRuleToUserAssociation() {
        List<HomeAutomationRuleEntity> rules = ruleRepository.findByUserEntity_UserId(userEntity.getUserId());
        assertThat(rules).hasSize(1);

        HomeAutomationRuleEntity retrievedRule1 = rules.get(0);
        assertThat(retrievedRule1.getUserEntity().getUsername()).isEqualTo("testuserA");

        retrievedRule1.getUserEntity().setUsername("UPDATED");
        userService.partialUpdate(retrievedRule1.getUserEntity().getUserId(), retrievedRule1.getUserEntity());


        Optional<HomeAutomationRuleEntity> updatedRule = ruleRepository.findById(retrievedRule1.getRuleId());
        assertThat(updatedRule).isPresent();
        assertThat(updatedRule.get().getUserEntity()).isEqualTo(userEntity);
        assertThat(retrievedRule1.getUserEntity().getUsername()).isEqualTo("UPDATED");

    }

    @Test
    @Transactional
    public void testFindHomeAutomationRulesByUserId() {
        List<HomeAutomationRuleEntity> retrievedRule = ruleRepository.findByUserEntity_UserId(userEntity.getUserId());
        assertThat(retrievedRule).hasSize(1);
    }

//    @Test
//    @Transactional
//    public void testFindHomeAutomationRuleEntitiesByGroupId() {
//        List<HomeAutomationRuleEntity> retrievedRule = ruleRepository.findByGroupEntities_GroupId(groupEntity.getGroupId());
//        assertThat(retrievedRule).hasSize(1);
//    }

    @Test
    @Transactional
    public void testThatHomeAutomationRuleCanBeDeleted() {
        Optional<HomeAutomationRuleEntity> retrievedRule = ruleRepository.findById(ruleEntity.getRuleId());
        assertThat(retrievedRule).isPresent();

        ruleRepository.delete(retrievedRule.get());
        Optional<HomeAutomationRuleEntity> retrievedRuleAfterDeletion = ruleRepository.findById(ruleEntity.getRuleId());
        assertThat(retrievedRuleAfterDeletion).isNotPresent();
    }

    @Test
    @Transactional
    public void testThatHomeAutomationRuleCanBeDeletedById() {
        Optional<HomeAutomationRuleEntity> retrievedRule = ruleRepository.findById(ruleEntity.getRuleId());
        assertThat(retrievedRule).isPresent();

        ruleRepository.deleteById(retrievedRule.get().getRuleId());
        Optional<HomeAutomationRuleEntity> retrievedRuleAfterDeletion = ruleRepository.findById(ruleEntity.getRuleId());
        assertThat(retrievedRuleAfterDeletion).isNotPresent();
    }

    @Test
    @Transactional
    public void testAddDeviceToHomeAutomationRule() {
        Optional<HomeAutomationRuleEntity> retrievedRule = ruleRepository.findById(ruleEntity.getRuleId());
        assertThat(retrievedRule).isPresent();

        DeviceEntity newDevice = TestDataUtil.createDeviceEntityB(userEntity);
        deviceRepository.save(newDevice);
        retrievedRule.get().getDeviceEntities().add(newDevice);

//        ruleRepository.save(retrievedRule.get());

        Optional<HomeAutomationRuleEntity> retrievedRuleAfterAddingDevice = ruleRepository.findById(retrievedRule.get().getRuleId());
        assertThat(retrievedRuleAfterAddingDevice).isPresent();
        assertThat(retrievedRuleAfterAddingDevice.get().getDeviceEntities()).contains(newDevice);
    }

    @Test
    @Transactional
    public void testRemoveDeviceFromHomeAutomationRule() {
        Optional<HomeAutomationRuleEntity> retrievedRule = ruleRepository.findById(ruleEntity.getRuleId());
        assertThat(retrievedRule).isPresent();

        retrievedRule.get().getDeviceEntities().remove(deviceEntityA);

//        ruleRepository.save(retrievedRule.get());

        Optional<HomeAutomationRuleEntity> retrievedRuleAfterRemovingDevice = ruleRepository.findById(retrievedRule.get().getRuleId());
        assertThat(retrievedRuleAfterRemovingDevice).isPresent();
        assertThat(retrievedRuleAfterRemovingDevice.get().getDeviceEntities()).doesNotContain(deviceEntityA);
    }

    @Test
    @Transactional
    public void testAddBehaviourToHomeAutomationRule() {
        Optional<HomeAutomationRuleEntity> retrievedRule = ruleRepository.findById(ruleEntity.getRuleId());
        assertThat(retrievedRule).isPresent();

        retrievedRule.get().setDeviceBehaviours(new HashMap<>(retrievedRule.get().getDeviceBehaviours()));
        retrievedRule.get().getDeviceBehaviours().put(deviceEntityA, HomeAutomationRuleEntity.Behaviour.TIMED);
        ruleRepository.save(retrievedRule.get());

        Optional<HomeAutomationRuleEntity> retrievedRuleAfterSettingBehaviour = ruleRepository.findById(retrievedRule.get().getRuleId());
        assertThat(retrievedRuleAfterSettingBehaviour).isPresent();
        assertThat(retrievedRuleAfterSettingBehaviour.get().getDeviceBehaviours()).containsEntry(deviceEntityA, HomeAutomationRuleEntity.Behaviour.TIMED);
    }

    @Test
    @Transactional
    public void testRemoveBehaviourFromHomeAutomationRule() {
        Optional<HomeAutomationRuleEntity> retrievedRule = ruleRepository.findById(ruleEntity.getRuleId());
        assertThat(retrievedRule).isPresent();

        retrievedRule.get().setDeviceBehaviours(new HashMap<>(retrievedRule.get().getDeviceBehaviours()));
        retrievedRule.get().getDeviceBehaviours().remove(deviceEntityA);
        ruleRepository.save(retrievedRule.get());

        Optional<HomeAutomationRuleEntity> retrievedRuleAfterRemovingBehaviour = ruleRepository.findById(retrievedRule.get().getRuleId());
        assertThat(retrievedRuleAfterRemovingBehaviour).isPresent();
        assertThat(retrievedRuleAfterRemovingBehaviour.get().getDeviceBehaviours()).doesNotContainKey(deviceEntityA);
    }

}