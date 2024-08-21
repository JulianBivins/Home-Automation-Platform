package com.homeautomation.homeAutomation.repositories;

import com.homeautomation.homeAutomation.config.TestDataUtil;
import com.homeautomation.homeAutomation.domain.entities.*;
import com.homeautomation.homeAutomation.repository.*;
import com.homeautomation.homeAutomation.services.GroupService;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GroupEntityRepositoryIT {

   @Autowired
   private GroupRepository groupRepository;

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private HomeAutomationRuleRepository ruleRepository;

   @Autowired
   private GroupService groupService;


   private GroupEntity groupEntity;
   private UserEntity userEntity;

    @BeforeEach
    @Transactional
    public void setUp() {
        userEntity = TestDataUtil.createTestUserEntityA();
        userRepository.save(userEntity);
        groupEntity = TestDataUtil.createGroupEntityA(userEntity);
        groupRepository.save(groupEntity);

    }

    @Test
    @Transactional
    public void testThatGroupCanBeCreatedAndRecalled() {
        Optional<GroupEntity> result = groupRepository.findById(groupEntity.getGroupId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(groupEntity);
    }

    @Test
    @Transactional
    public void testDeleteGroupEntity() {
        Optional<GroupEntity> retrievedGroup = groupRepository.findById(groupEntity.getGroupId());
        assertThat(retrievedGroup).isPresent();
        groupRepository.delete(retrievedGroup.get());
        Optional<GroupEntity> retrievedRuleAfterDeletion = groupRepository.findById(groupEntity.getGroupId());
        assertThat(retrievedRuleAfterDeletion).isNotPresent();
    }

    @Test
    @Transactional
    public void testDeleteGroupEntityById(){
        Optional<GroupEntity> retrievedGroup = groupRepository.findById(groupEntity.getGroupId());
        assertThat(retrievedGroup).isPresent();

        groupRepository.deleteById(retrievedGroup.get().getGroupId());
        Optional<GroupEntity> retrievedUserAfterDeletion = groupRepository.findById(groupEntity.getGroupId());
        assertThat(retrievedUserAfterDeletion).isNotPresent();
    }

    @Test
    @Transactional
    public void testThatMultipleGroupsCanBeCreatedAndRecalled() {
        groupRepository.delete(groupEntity);
        assertThat(groupRepository.findById(groupEntity.getGroupId())).isNotPresent();

        GroupEntity groupEntityA = TestDataUtil.createGroupEntityA(userEntity);
        groupRepository.save(groupEntityA);
        GroupEntity groupEntityB = TestDataUtil.createGroupEntityB(userEntity);
        groupRepository.save(groupEntityB);

        List<GroupEntity> resultEntities = groupRepository.findAll();
        assertThat(resultEntities).hasSize(2).containsExactly(groupEntityA, groupEntityB);
    }

    @Test
    @Transactional
    public void testThatHomeAutomationRuleCanBeUpdated () {
        Optional<GroupEntity> retrievedGroup = groupRepository.findById(groupEntity.getGroupId());
        assertThat(retrievedGroup).isPresent();

        GroupEntity updatedGroupEntity = TestDataUtil.createGroupEntityB(userEntity);
        updatedGroupEntity.setName("testGroupB");

        GroupEntity groupEntityAfterPartialUpdate = groupService.partialUpdate(retrievedGroup.get().getGroupId(), updatedGroupEntity);
        groupRepository.save(groupEntityAfterPartialUpdate);

        Optional<GroupEntity> retrievedGroupAfterUpdate = groupRepository.findById(groupEntityAfterPartialUpdate.getGroupId());
        assertThat(retrievedGroupAfterUpdate).isPresent();

        assertThat(retrievedGroupAfterUpdate.get()).isEqualTo(updatedGroupEntity);
        assertThat(retrievedGroup.get()).isNotEqualTo(retrievedGroupAfterUpdate.get());
    }

    @Test
    @Transactional
    public void testFindGroupEntitiesByUserId() {

    }


    @Test
    @Transactional
    public void testAddHomeAutomationRuleFromGroup() {

        Optional<GroupEntity> retrievedGroup = groupRepository.findById(groupEntity.getGroupId());
        assertThat(retrievedGroup).isPresent();

        HomeAutomationRuleEntity ruleEntity = TestDataUtil.createTestRuleEntityA(userEntity, groupEntity);
        ruleRepository.save(ruleEntity);

        List<HomeAutomationRuleEntity> rules = retrievedGroup.get().getRules();

        //rules might be unmodifiable
//        retrievedGroup.get().setRules(new ArrayList<>(rules));

        if (!rules.contains(ruleEntity)) {
            rules.add(ruleEntity);
        }

        retrievedGroup.get().setRules(new ArrayList<>(rules));

         groupService.save(retrievedGroup.get());

        Optional<GroupEntity> retrievedGroupAfterSettingRule = groupRepository.findById(retrievedGroup.get().getGroupId());
        assertThat(retrievedGroupAfterSettingRule).isPresent();
        assertThat(retrievedGroupAfterSettingRule.get().getRules()).contains(ruleEntity);
    }

    @Test
    @Transactional
    public void testRemoveHomeAutomationRuleFromGroup() {

        Optional<GroupEntity> retrievedGroup = groupRepository.findById(groupEntity.getGroupId());
        assertThat(retrievedGroup).isPresent();

        HomeAutomationRuleEntity ruleEntity = TestDataUtil.createTestRuleEntityA(userEntity, groupEntity);
        ruleRepository.save(ruleEntity);

        List<HomeAutomationRuleEntity> rules = retrievedGroup.get().getRules();

        //rules might be unmodifiable
//        retrievedGroup.get().setRules(new ArrayList<>(rules));

//        if (!rules.contains(ruleEntity)) {
            rules.add(ruleEntity);
//        }

        retrievedGroup.get().setRules(new ArrayList<>(rules));

         groupService.save(retrievedGroup.get());

        Optional<GroupEntity> retrievedGroupAfterSettingRule = groupRepository.findById(retrievedGroup.get().getGroupId());
        assertThat(retrievedGroupAfterSettingRule).isPresent();
        assertThat(retrievedGroupAfterSettingRule.get().getRules()).contains(ruleEntity);

        List<HomeAutomationRuleEntity> updatedRules = retrievedGroupAfterSettingRule.get().getRules().stream()
                .filter(rule -> !rule.equals(ruleEntity))
                .collect(Collectors.toCollection(ArrayList::new));

        retrievedGroupAfterSettingRule.get().setRules(new ArrayList<>(updatedRules));
        GroupEntity save = groupService.save(retrievedGroupAfterSettingRule.get());

        Optional<GroupEntity> retrievedGroupAfterDeletingRule = groupRepository.findById(groupEntity.getGroupId());
        assertThat(retrievedGroupAfterDeletingRule).isPresent();
        assertThat(retrievedGroupAfterDeletingRule.get().getRules()).doesNotContain(ruleEntity);
    }

    @Test
    @Transactional
    public void testGroupToHomeAutomationRuleAssociation() {
        Optional<GroupEntity> retrievedGroup = groupRepository.findById(groupEntity.getGroupId());
        assertThat(retrievedGroup).isPresent();

        HomeAutomationRuleEntity ruleEntityA = TestDataUtil.createTestRuleEntityA(userEntity, groupEntity);
        HomeAutomationRuleEntity ruleEntityB = TestDataUtil.createTestRuleEntityB(userEntity, groupEntity);
        HomeAutomationRuleEntity ruleEntityC = TestDataUtil.createTestRuleEntityC(userEntity, groupEntity);

        ruleRepository.save(ruleEntityA);
        ruleRepository.save(ruleEntityB);
        ruleRepository.save(ruleEntityC);

        List<HomeAutomationRuleEntity> rules = retrievedGroup.get().getRules();

        //rules might be unmodifiable
//        retrievedGroup.get().setRules(new ArrayList<>(rules));
//
////        if (!rules.contains(ruleEntityA)) {
//            rules.add(ruleEntityA);
////        }
////        if (!rules.contains(ruleEntityB)) {
//            rules.add(ruleEntityB);
////        }
////        if (!rules.contains(ruleEntityC)) {
//            rules.add(ruleEntityC);
////        }

         List<HomeAutomationRuleEntity> newRules = new ArrayList<>(List.of(ruleEntityA, ruleEntityB, ruleEntityC));

        retrievedGroup.get().setRules(newRules);

        GroupEntity save = groupService.save(retrievedGroup.get());

        Optional<GroupEntity> retrievedGroupAfterSettingRules = groupRepository.findById(retrievedGroup.get().getGroupId());
        assertThat(retrievedGroupAfterSettingRules).isPresent();
        assertThat(retrievedGroupAfterSettingRules.get().getRules()).contains(ruleEntityA, ruleEntityB, ruleEntityC);

        assertThat(retrievedGroupAfterSettingRules.get().getRules().get(0).getGroupEntity()).isEqualTo(groupEntity);
        assertThat(retrievedGroupAfterSettingRules.get().getRules().get(1).getGroupEntity()).isEqualTo(groupEntity);
        assertThat(retrievedGroupAfterSettingRules.get().getRules().get(2).getGroupEntity()).isEqualTo(groupEntity);
    }




    @Test
    @Transactional
    public void testCascadeDeleteGroupWithHomeAutomationRules() {}

    
//             testGroupToHomeAutomationRuleAssociation()
//             testAddDeviceToGroup()
//             testRemoveDeviceFromGroup()
//             testGroupToDeviceAssociation()
//             testGroupEntityToUserEntityAssociation()

}