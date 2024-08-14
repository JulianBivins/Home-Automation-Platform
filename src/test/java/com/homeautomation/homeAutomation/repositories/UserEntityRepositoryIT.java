package com.homeautomation.homeAutomation.repositories;

import com.homeautomation.homeAutomation.config.TestDataUtil;
import com.homeautomation.homeAutomation.domain.entities.*;
import com.homeautomation.homeAutomation.repository.BehaviourRepository;
import com.homeautomation.homeAutomation.repository.DeviceRepository;
import com.homeautomation.homeAutomation.repository.HomeAutomationRuleRepository;
import com.homeautomation.homeAutomation.repository.UserRepository;
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

        groupEntity = TestDataUtil.createGroupEntityA(userEntity);

        rule1 = TestDataUtil.createTestRuleEntityA(userEntity, groupEntity);

        rule2 = TestDataUtil.createTestRuleEntityC(userEntity, groupEntity);
        userEntity.setRules(List.of(rule1, rule2));

        userRepository.save(userEntity);
    }

    @Test
    @Transactional
    public void testThatUserCanBeCreatedAndRecalled() {
        // Retrieve the user by ID
        Optional<UserEntity> result = userRepository.findById(userEntity.getUserId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userEntity);
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
    public void testUserToHomeAutomationRuleAssociation() {

        List<HomeAutomationRuleEntity> rules = userEntity.getRules();
        assertThat(rules).hasSize(2);

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



//	testUpdateUserEntity() {}
//	testDeleteUserEntity()
//	testAddHomeAutomationRuleToUser()
//    testRemoveHomeAutomationRuleFromUser()
//    testUserEntityToGroupEntityAssociation()
//    testFindHomeAutomationRulesByUserId()
//    testFindGroupEntitiesByUserId()
//    testCascadeDeleteUserEntity()
}