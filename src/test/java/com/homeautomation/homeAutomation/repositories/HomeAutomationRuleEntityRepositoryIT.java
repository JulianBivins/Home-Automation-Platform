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
public class HomeAutomationRuleEntityRepositoryIT {

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
    }

    @Test
    @Transactional
    public void testHomeAutomationRuleToUserAssociation() {
        // Retrieve all rules
        List<HomeAutomationRuleEntity> rules = ruleRepository.findByUserEntity_UserId(userEntity.getUserId());
        assertThat(rules).hasSize(2);

        HomeAutomationRuleEntity retrievedRule1 = rules.get(0);
        HomeAutomationRuleEntity retrievedRule2 = rules.get(1);

        // Check the user associated with the rules
        assertThat(retrievedRule1.getUserEntity().getUsername()).isEqualTo("testuser");
        assertThat(retrievedRule2.getUserEntity().getUsername()).isEqualTo("testuser");

        // Check the rules associated with the user
//        assertThat(retrievedRule1.getUserEntity().getRules()).hasSize(2);
//        assertThat(retrievedRule2.getUserEntity().getRules()).hasSize(2);
        assertThat(ruleRepository.findByUserEntity_UserId(userEntity.getUserId())).hasSize(2);
    }
}