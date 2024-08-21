package com.homeautomation.homeAutomation.repositories;

import com.homeautomation.homeAutomation.config.TestDataUtil;
import com.homeautomation.homeAutomation.domain.entities.*;
import com.homeautomation.homeAutomation.repository.*;
import com.homeautomation.homeAutomation.services.HomeAutomationRuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private GroupRepository groupRepository;

    @Autowired
    private BehaviourRepository behaviourRepository;

    @Autowired
    private DeviceRepository deviceRepository;


    private HomeAutomationRuleEntity ruleEntity;
    private UserEntity userEntity;
    private GroupEntity groupEntity;


    @BeforeEach
    @Transactional
    public void setUp() {
        userEntity = TestDataUtil.createTestUserEntityA();
        userRepository.save(userEntity);
        groupEntity = TestDataUtil.createGroupEntityA(userEntity);
        groupRepository.save(groupEntity);
        ruleEntity = TestDataUtil.createTestRuleEntityA(userEntity, groupEntity);
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
    public void testThatMultipleUsersCanBeCreatedAndRecalled() {
        ruleRepository.delete(ruleEntity);
        assertThat(ruleRepository.findById(ruleEntity.getRuleId())).isNotPresent();

        HomeAutomationRuleEntity ruleEntityA = TestDataUtil.createTestRuleEntityA(userEntity, groupEntity);
        ruleRepository.save(ruleEntityA);
        HomeAutomationRuleEntity ruleEntityB = TestDataUtil.createTestRuleEntityB(userEntity, groupEntity);
        ruleRepository.save(ruleEntityB);
        HomeAutomationRuleEntity ruleEntityC = TestDataUtil.createTestRuleEntityC(userEntity, groupEntity);
        ruleRepository.save(ruleEntityC);

        List<HomeAutomationRuleEntity> resultEntities = ruleRepository.findAll();
        assertThat(resultEntities).hasSize(3).containsExactly(ruleEntityA, ruleEntityB, ruleEntityC);
    }

    @Test
    @Transactional
    public void testThatMultipleRulesCanBeCreatedAndRecalled() {
        ruleRepository.delete(ruleEntity);
        assertThat(ruleRepository.findById(ruleEntity.getRuleId())).isNotPresent();

        HomeAutomationRuleEntity ruleEntityA = TestDataUtil.createTestRuleEntityA(userEntity, groupEntity);
        ruleRepository.save(ruleEntityA);
        HomeAutomationRuleEntity ruleEntityB = TestDataUtil.createTestRuleEntityB(userEntity, groupEntity);
        ruleRepository.save(ruleEntityB);
        HomeAutomationRuleEntity ruleEntityC = TestDataUtil.createTestRuleEntityC(userEntity,groupEntity);
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
    public void testDeleteUserEntity(){
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

        HomeAutomationRuleEntity updatedRuleEntity = TestDataUtil.createTestRuleEntityB(userEntity, groupEntity);
        updatedRuleEntity.setRuleName("testRuleB");

        HomeAutomationRuleEntity ruleEntityAfterPartialUpdate = ruleService.partialUpdate(retrievedRule.get().getRuleId(), updatedRuleEntity);
        ruleRepository.save(ruleEntityAfterPartialUpdate);

        Optional<HomeAutomationRuleEntity> retrievedRuleAfterUpdate = ruleRepository.findById(ruleEntityAfterPartialUpdate.getRuleId());
        assertThat(retrievedRuleAfterUpdate).isPresent();

        assertThat(retrievedRuleAfterUpdate.get()).isEqualTo(updatedRuleEntity);
        assertThat(retrievedRule.get()).isNotEqualTo(retrievedRuleAfterUpdate.get());
    }


    @Test
    @Transactional
    public void testHomeAutomationRuleToUserAssociation() {
        // Retrieve all rules
        List<HomeAutomationRuleEntity> rules = ruleRepository.findByUserEntity_UserId(userEntity.getUserId());
        assertThat(rules).hasSize(1);

        HomeAutomationRuleEntity retrievedRule1 = rules.get(0);
//        HomeAutomationRuleEntity retrievedRule2 = rules.get(1);

        // Check the user associated with the rules
        assertThat(retrievedRule1.getUserEntity().getUsername()).isEqualTo("testuser");
//        assertThat(retrievedRule2.getUserEntity().getUsername()).isEqualTo("testuser");

        // Check the rules associated with the user
        assertThat(retrievedRule1.getUserEntity().getRules()).hasSize(1);
//        assertThat(retrievedRule2.getUserEntity().getRules()).hasSize(2);
    }

    @Test
    @Transactional
    public void testFindHomeAutomationRulesByUserId() {
        List<HomeAutomationRuleEntity> retrievedRule = ruleRepository.findByUserEntity_UserId(userEntity.getUserId());
        assertThat(retrievedRule).hasSize(1);
    }

    @Test
    @Transactional
    public void testFindHomeAutomationRuleEntitiesByGroupId() {
        List<HomeAutomationRuleEntity> retrievedRule = ruleRepository.findByGroupEntity_GroupId(groupEntity.getGroupId());
        assertThat(retrievedRule).hasSize(1);
    }

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
    public void testAddBehaviourToHomeAutomationRule(){
        Optional<HomeAutomationRuleEntity> retrievedRule = ruleRepository.findById(ruleEntity.getRuleId());
        assertThat(retrievedRule).isPresent();

        DeviceEntity deviceEntity = TestDataUtil.createDeviceEntityA(userEntity, groupEntity);
        deviceRepository.save(deviceEntity);
        BehaviourEntity behaviourEntity = TestDataUtil.generateBehaviourEntity(ruleEntity, deviceEntity);
        behaviourRepository.save(behaviourEntity);
        retrievedRule.get().setBehaviourEntities(List.of(behaviourEntity));

        Optional<HomeAutomationRuleEntity> retrievedRuleAfterSettingBehaviour = ruleRepository.findById(ruleEntity.getRuleId());
        assertThat(retrievedRuleAfterSettingBehaviour).isPresent();
        assertThat(retrievedRuleAfterSettingBehaviour.get().getBehaviourEntities()).contains(behaviourEntity);
    }

    @Test
    @Transactional
    public void testRemoveBehaviourFromHomeAutomationRule() {
        Optional<HomeAutomationRuleEntity> retrievedRule = ruleRepository.findById(ruleEntity.getRuleId());
        assertThat(retrievedRule).isPresent();

        DeviceEntity deviceEntity = TestDataUtil.createDeviceEntityA(userEntity, groupEntity);
        deviceRepository.save(deviceEntity);
        BehaviourEntity behaviourEntity = TestDataUtil.generateBehaviourEntity(ruleEntity, deviceEntity);
        behaviourRepository.save(behaviourEntity);
        retrievedRule.get().setBehaviourEntities(List.of(behaviourEntity));
        ruleRepository.save(retrievedRule.get());
//        ruleService.saveUpdate(retrievedRule.get().getRuleId(), retrievedRule.get());

        Optional<HomeAutomationRuleEntity> retrievedRuleAfterSettingBehaviour = ruleRepository.findById(ruleEntity.getRuleId());
        assertThat(retrievedRuleAfterSettingBehaviour).isPresent();
        assertThat(retrievedRuleAfterSettingBehaviour.get().getBehaviourEntities()).contains(behaviourEntity);

        List<BehaviourEntity> updatedBehaviours = retrievedRuleAfterSettingBehaviour.get().getBehaviourEntities()
                .stream()
                .filter(b -> !b.getBehaviour().equals(behaviourEntity.getBehaviour()))
                .collect(Collectors.toList());
        retrievedRuleAfterSettingBehaviour.get().setBehaviourEntities(updatedBehaviours);
        ruleRepository.save(retrievedRuleAfterSettingBehaviour.get());

        Optional<HomeAutomationRuleEntity> retrievedRuleAfterRemovingBehaviour = ruleRepository.findById(ruleEntity.getRuleId());
        assertThat(retrievedRuleAfterRemovingBehaviour).isPresent();
        assertThat(retrievedRuleAfterRemovingBehaviour.get().getBehaviourEntities()).doesNotContain(behaviourEntity);
    }

}