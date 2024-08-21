package com.homeautomation.homeAutomation.repositories;

import com.homeautomation.homeAutomation.config.TestDataUtil;
import com.homeautomation.homeAutomation.domain.entities.*;
import com.homeautomation.homeAutomation.repository.*;
import com.homeautomation.homeAutomation.services.UserService;
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
public class UserEntityRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HomeAutomationRuleRepository ruleRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private BehaviourRepository behaviourRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserService userService;

    private UserEntity userEntity;
    private HomeAutomationRuleEntity rule1;
    private HomeAutomationRuleEntity rule2;
    private GroupEntity groupEntity;
    private DeviceEntity deviceEntity;
    private List<BehaviourEntity> behaviourEntities;

    @BeforeEach
    @Transactional
    public void setUp() {

        userEntity = TestDataUtil.createTestUserEntityA();
//        userRepository.saveAndFlush(userEntity);
        userRepository.save(userEntity);

        groupEntity = TestDataUtil.createGroupEntityA(userEntity);
        groupRepository.save(groupEntity);

        rule1 = TestDataUtil.createTestRuleEntityA(userEntity, groupEntity);
        rule2 = TestDataUtil.createTestRuleEntityC(userEntity, groupEntity);
        ruleRepository.saveAll(List.of(rule1,rule2));

        //Maybe should be tested independently
//        userEntity.setRules(List.of(rule1, rule2));

        //

    }

    @Test
    @Transactional
    public void testThatUserCanBeCreatedAndRecalled() {
        Optional<UserEntity> result = userRepository.findById(userEntity.getUserId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userEntity);
    }

    @Test
    @Transactional
    public void testDeleteUserEntity(){
        Optional<UserEntity> retrievedUser = userRepository.findById(userEntity.getUserId());
        assertThat(retrievedUser).isPresent();

        userRepository.delete(retrievedUser.get());
        Optional<UserEntity> retrievedUserAfterDeletion = userRepository.findByUsername(userEntity.getUsername());
        assertThat(retrievedUserAfterDeletion).isNotPresent();
    }

    @Test
    @Transactional
    public void testDeleteUserEntityById(){
        Optional<UserEntity> retrievedUser = userRepository.findById(userEntity.getUserId());
        assertThat(retrievedUser).isPresent();

        userRepository.deleteById(retrievedUser.get().getUserId());
        Optional<UserEntity> retrievedUserAfterDeletion = userRepository.findByUsername(userEntity.getUsername());
        assertThat(retrievedUserAfterDeletion).isNotPresent();
    }


    @Test
    @Transactional
    public void testThatMultipleUsersCanBeCreatedAndRecalled() {
        userRepository.delete(userEntity);
        assertThat(userRepository.findById(userEntity.getUserId())).isNotPresent();

        UserEntity userEntityA = TestDataUtil.createTestUserEntityA();
        userRepository.save(userEntityA);
        UserEntity userEntityB = TestDataUtil.createTestUserEntityA();
        userRepository.save(userEntityB);
        UserEntity userEntityC = TestDataUtil.createTestUserEntityC();
        userRepository.save(userEntityC);

        List<UserEntity> resultEntities = userRepository.findAll();
        assertThat(resultEntities).hasSize(3).containsExactly(userEntityA, userEntityB, userEntityC);
    }

    @Test
    @Transactional
    public void testFindUserByID() {
        Optional<UserEntity> retrievedUser = userRepository.findById(userEntity.getUserId());
        assertThat(retrievedUser).isPresent();
    }

    @Test
    @Transactional
    public void testFindUserByUsername() {
        Optional<UserEntity> retrievedUser = userRepository.findByUsername(userEntity.getUsername());
        assertThat(retrievedUser).isPresent();
        retrievedUser.ifPresent(user -> {
            assertThat(user.getUsername()).isEqualTo("testuser");
        });
    }

    @Test
    @Transactional
    public void testUpdateUserEntity() {
        Optional<UserEntity> retrievedUser = userRepository.findByUsername(userEntity.getUsername());
        assertThat(retrievedUser).isPresent();

        UserEntity updatedUserEntity = TestDataUtil.createTestUserEntityC();
        updatedUserEntity.setUsername("testuserC");

        UserEntity userEntityAfterPartialUpdate = userService.partialUpdate(retrievedUser.get().getUserId(), updatedUserEntity);
        userRepository.save(userEntityAfterPartialUpdate);

        Optional<UserEntity> retrievedUserAfterUpdate = userRepository.findById(userEntityAfterPartialUpdate.getUserId());
        assertThat(retrievedUserAfterUpdate).isPresent();

        assertThat(retrievedUserAfterUpdate.get()).isEqualTo(updatedUserEntity);
        assertThat(retrievedUser.get()).isNotEqualTo(retrievedUserAfterUpdate.get());
    }

    @Test
    @Transactional
    public void testCascadeDeleteUserEntity() {
        Optional<UserEntity> retrievedUser = userRepository.findById(userEntity.getUserId());
        assertThat(retrievedUser).isPresent();

        List<HomeAutomationRuleEntity> rules = ruleRepository.findAll();
        HomeAutomationRuleEntity retrievedRule1 = rules.get(0);
        HomeAutomationRuleEntity retrievedRule2 = rules.get(1);
        retrievedUser.get().setRules(List.of(retrievedRule1, retrievedRule2));

        List<GroupEntity> groups = groupRepository.findAll();
        GroupEntity retrievedGroup = groups.get(0);
        retrievedUser.get().setGroups(List.of(retrievedGroup));

        userRepository.delete(retrievedUser.get());
        Optional<UserEntity> deletedUser = userRepository.findById(userEntity.getUserId());
        assertThat(deletedUser).isNotPresent();

        assertThat(ruleRepository.findById(rule1.getRuleId())).isNotPresent();
        assertThat(ruleRepository.findById(rule2.getRuleId())).isNotPresent();

        assertThat(groupRepository.findById(groupEntity.getGroupId())).isNotPresent();
    }

    @Test
    @Transactional
    public void testAddHomeAutomationRuleToUser() {
        List<HomeAutomationRuleEntity> rules = ruleRepository.findAll();
        HomeAutomationRuleEntity retrievedRule1 = rules.get(0);
        HomeAutomationRuleEntity retrievedRule2 = rules.get(1);

        Optional<UserEntity> retrievedUser = userRepository.findById(userEntity.getUserId());
        assertThat(retrievedUser).isPresent();
        retrievedUser.get().setRules(List.of(retrievedRule1, retrievedRule2));

        assertThat(retrievedUser.get().getRules()).isEqualTo(List.of(retrievedRule1, retrievedRule2));
    }

    @Test
    @Transactional
    public void testRemoveHomeAutomationRuleFromUser() {
        List<HomeAutomationRuleEntity> rules = ruleRepository.findAll();
        HomeAutomationRuleEntity retrievedRule1 = rules.get(0);
        HomeAutomationRuleEntity retrievedRule2 = rules.get(1);

        Optional<UserEntity> retrievedUser = userRepository.findById(userEntity.getUserId());
        assertThat(retrievedUser).isPresent();

        retrievedUser.get().setRules(new ArrayList<>(List.of(retrievedRule1, retrievedRule2)));
        assertThat(retrievedUser.get().getRules()).hasSize(2);

        retrievedUser.get().getRules().clear();

        userRepository.save(retrievedUser.get());
        Optional<UserEntity> retrievedUserAfterRemovedRules = userRepository.findById(userEntity.getUserId());
        assertThat(retrievedUserAfterRemovedRules).isPresent();

        assertThat(retrievedUserAfterRemovedRules.get().getRules()).hasSize(0);
    }

    @Test
    @Transactional
    public void testUserToHomeAutomationRuleAssociation() {
        Optional<UserEntity> retrievedUser = userRepository.findById(userEntity.getUserId());
        assertThat(retrievedUser).isPresent();
        UserEntity localUserEntity = retrievedUser.get();

        localUserEntity.setRules(List.of(rule1, rule2));

        List<HomeAutomationRuleEntity> rules = localUserEntity.getRules();
        assertThat(rules).hasSize(2);
        assertThat(ruleRepository.findByUserEntity_UserId(localUserEntity.getUserId())).hasSize(2);

        HomeAutomationRuleEntity retrievedRule1 = rules.get(0);
        HomeAutomationRuleEntity retrievedRule2 = rules.get(1);


        assertThat(retrievedRule1.getRuleName()).isEqualTo("RuleA");
        assertThat(retrievedRule1.getDescription()).isEqualTo("Mock RuleA");
        assertThat(retrievedRule1.getEvent()).isEqualTo(HomeAutomationRuleEntity.Event.TIME);
        assertThat(retrievedRule1.getUserEntity().getUsername()).isEqualTo("testuser");

        assertThat(retrievedRule2.getRuleName()).isEqualTo("RuleC");
        assertThat(retrievedRule2.getDescription()).isEqualTo("Mock RuleC");
        assertThat(retrievedRule2.getEvent()).isEqualTo(HomeAutomationRuleEntity.Event.AFTER_OTHER);
        assertThat(retrievedRule2.getUserEntity().getUsername()).isEqualTo("testuser");
    }

    @Test
    @Transactional
    public void testUserEntityToGroupEntityAssociation(){
        Optional<UserEntity> retrievedUser = userRepository.findById(userEntity.getUserId());
        assertThat(retrievedUser).isPresent();
        UserEntity localUserEntity = retrievedUser.get();

        localUserEntity.setGroups(List.of(groupEntity));

        List<GroupEntity> groups = localUserEntity.getGroups();
        assertThat(groups).hasSize(1);
        assertThat(groupRepository.findByUserEntity_UserId(localUserEntity.getUserId())).hasSize(1);

        GroupEntity retrievedGroup = groups.get(0);

        assertThat(retrievedGroup.getUserEntity()).isEqualTo(localUserEntity);
        assertThat(retrievedGroup.getName()).isEqualTo("LivingRoom");
    }


}