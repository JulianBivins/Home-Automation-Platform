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
   private DeviceRepository deviceRepository;

   @Autowired
   private GroupService groupService;


   private GroupEntity groupEntity;
   private UserEntity userEntity;
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
//        groupRepository.save(groupEntityAfterPartialUpdate);

        Optional<GroupEntity> retrievedGroupAfterUpdate = groupRepository.findById(groupEntityAfterPartialUpdate.getGroupId());
        assertThat(retrievedGroupAfterUpdate).isPresent();

        assertThat(retrievedGroupAfterUpdate.get()).isEqualTo(updatedGroupEntity);
        assertThat(retrievedGroup.get()).isNotEqualTo(retrievedGroupAfterUpdate.get());
    }

    @Test
    @Transactional
    public void testFindGroupEntitiesByUserId() {
        List<GroupEntity> retrievedGroup = groupRepository.findByUserEntity_UserId(userEntity.getUserId());
        assertThat(retrievedGroup).hasSize(1);
        assertThat(retrievedGroup.get(0)).isEqualTo(groupEntity);
    }


    @Test
    @Transactional
    public void testAddHomeAutomationRuleFromGroup() {

        Optional<GroupEntity> retrievedGroup = groupRepository.findById(groupEntity.getGroupId());
        assertThat(retrievedGroup).isPresent();

        HomeAutomationRuleEntity ruleEntity = TestDataUtil.createTestRuleEntityA(userEntity, groupEntity, new ArrayList<>(List.of(deviceEntityA, deviceEntityB)));
        ruleRepository.save(ruleEntity);

        HomeAutomationRuleEntity rule = retrievedGroup.get().getRule();

        retrievedGroup.get().setRule(ruleEntity);

        groupRepository.save(retrievedGroup.get());

        Optional<GroupEntity> retrievedGroupAfterSettingRule = groupRepository.findById(retrievedGroup.get().getGroupId());
        assertThat(retrievedGroupAfterSettingRule).isPresent();
        assertThat(retrievedGroupAfterSettingRule.get().getRule()).isEqualTo(ruleEntity);
    }

    @Test
    @Transactional
    public void testRemoveHomeAutomationRuleFromGroup() {

        Optional<GroupEntity> retrievedGroup = groupRepository.findById(groupEntity.getGroupId());
        assertThat(retrievedGroup).isPresent();

        HomeAutomationRuleEntity ruleEntity = TestDataUtil.createTestRuleEntityA(userEntity, groupEntity, new ArrayList<>(List.of(deviceEntityA, deviceEntityB)));
        ruleRepository.save(ruleEntity);

        HomeAutomationRuleEntity rule = retrievedGroup.get().getRule();

        retrievedGroup.get().setRule(ruleEntity);

        groupRepository.save(retrievedGroup.get());

        Optional<GroupEntity> retrievedGroupAfterSettingRule = groupRepository.findById(retrievedGroup.get().getGroupId());
        assertThat(retrievedGroupAfterSettingRule).isPresent();
        assertThat(retrievedGroupAfterSettingRule.get().getRule()).isEqualTo(ruleEntity);

        retrievedGroup.get().setRule(null);
        groupRepository.save(retrievedGroupAfterSettingRule.get());

        Optional<GroupEntity> retrievedGroupAfterDeletingRule = groupRepository.findById(groupEntity.getGroupId());
        assertThat(retrievedGroupAfterDeletingRule).isPresent();
        assertThat(retrievedGroupAfterDeletingRule.get().getRule()).isNotEqualTo(ruleEntity);
        assertThat(retrievedGroupAfterDeletingRule.get().getRule()).isNull();
    }

    @Test
    @Transactional
    public void testGroupToHomeAutomationRuleAssociation() {
        Optional<GroupEntity> retrievedGroup = groupRepository.findById(groupEntity.getGroupId());
        assertThat(retrievedGroup).isPresent();

        HomeAutomationRuleEntity ruleEntityC = TestDataUtil.createTestRuleEntityC(userEntity, groupEntity, new ArrayList<>(List.of(deviceEntityA, deviceEntityB)));
        ruleRepository.save(ruleEntityC);

        retrievedGroup.get().setRule(ruleEntityC);
        groupRepository.save(retrievedGroup.get());

        Optional<GroupEntity> retrievedGroupAfterSettingRule = groupRepository.findById(retrievedGroup.get().getGroupId());
        assertThat(retrievedGroupAfterSettingRule).isPresent();
        assertThat(retrievedGroupAfterSettingRule.get().getRule()).isEqualTo(ruleEntityC);

        assertThat(retrievedGroupAfterSettingRule.get().getRule().getGroupEntities().contains(groupEntity));
    }

    @Test
    @Transactional
    public void testGroupToDeviceAssociation() {

        Optional<GroupEntity> retrievedGroup = groupRepository.findById(groupEntity.getGroupId());
        assertThat(retrievedGroup).isPresent();

        HomeAutomationRuleEntity ruleEntityC = TestDataUtil.createTestRuleEntityC(userEntity, groupEntity, new ArrayList<>(List.of(deviceEntityA, deviceEntityB)));
        ruleRepository.save(ruleEntityC);

        retrievedGroup.get().setRule(ruleEntityC);

        groupRepository.save(retrievedGroup.get());

        Optional<GroupEntity> retrievedGroupAfterAddingRulesWithDevices = groupRepository.findById(retrievedGroup.get().getGroupId());
        assertThat(retrievedGroupAfterAddingRulesWithDevices).isPresent();
        List<DeviceEntity> devices = retrievedGroupAfterAddingRulesWithDevices.get().getRule().getDeviceEntities();

        assertThat(devices).hasSize(2).containsExactlyInAnyOrder(deviceEntityA, deviceEntityB);
    }

    @Test
    @Transactional
    public void testGroupEntityToUserEntityAssociation() {
        Optional<GroupEntity> retrievedGroup = groupRepository.findById(groupEntity.getGroupId());
        assertThat(retrievedGroup).isPresent();
        assertThat(retrievedGroup.get().getUserEntity()).isEqualTo(userEntity);
    }
}