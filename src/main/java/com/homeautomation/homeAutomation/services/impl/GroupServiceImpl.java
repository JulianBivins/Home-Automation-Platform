package com.homeautomation.homeAutomation.services.impl;

import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.GroupEntity;
import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import com.homeautomation.homeAutomation.repository.DeviceRepository;
import com.homeautomation.homeAutomation.repository.GroupRepository;
import com.homeautomation.homeAutomation.repository.HomeAutomationRuleRepository;
import com.homeautomation.homeAutomation.services.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("groupService")//SpEL expressions
public class GroupServiceImpl implements GroupService {

    final private GroupRepository groupRepository;
    final private DeviceRepository deviceRepository;
    final private HomeAutomationRuleRepository ruleRepository;

    public GroupServiceImpl(GroupRepository groupRepository, DeviceRepository deviceRepository, HomeAutomationRuleRepository ruleRepository) {
        this.groupRepository = groupRepository;
        this.deviceRepository = deviceRepository;
        this.ruleRepository = ruleRepository;
    }


    @Override
    public GroupEntity save(GroupEntity groupEntity) {
        return groupRepository.save(groupEntity);
    }

    @Override
    public GroupEntity saveUpdate(Long id, GroupEntity groupEntity) {
        groupEntity.setGroupId(id);
        return groupRepository.save(groupEntity);
    }

    @Override
    public Iterable<GroupEntity> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public Optional<GroupEntity> findById(Long id) {
        return groupRepository.findById(id);
    }

    @Override
    public List<GroupEntity> findByUserEntity_UserId(Long userId) {
        return groupRepository.findByUserEntity_UserId(userId);
    }

//


    @Override
    public boolean isExists(Long id) {
        return groupRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        groupRepository.deleteById(id);
    }

    @Override
    public GroupEntity partialUpdate(Long id, GroupEntity groupEntity) {
        groupEntity.setGroupId(id);
        return groupRepository.findById(id).map(existingGroup -> {
//            Optional.ofNullable(groupEntity.getUserEntity()).ifPresent(existingGroup::setUserEntity);
            Optional.ofNullable(groupEntity.getName()).ifPresent(existingGroup::setName);
            Optional.ofNullable(groupEntity.getRuleEntities()).ifPresent(newRules -> existingGroup.setRuleEntities(new ArrayList<>(newRules)));
            return groupRepository.save(existingGroup);
        }).orElseThrow(() -> new RuntimeException("Group does not exist"));
    }

    @Override
    public boolean isOwner(Long groupId, String currentUsername) {
        Optional<GroupEntity> group = groupRepository.findById(groupId);
        return group.isPresent() && group.get().getUserEntity().getUsername().equals(currentUsername);
    }

    @Override
    public boolean isOwner(Long groupId, Long ruleId, String currentUsername) {
        Optional<GroupEntity> group = groupRepository.findById(groupId);
        if (group.isEmpty()) {
            System.err.println("Group ID not found: " + groupId);
            return false;
        }

        if (!group.get().getUserEntity().getUsername().equals(currentUsername)) {
            System.err.println("User : " + currentUsername + "does not own Group with ID: " + groupId);
            return false;
        }

        Optional<HomeAutomationRuleEntity> rule = ruleRepository.findById(ruleId);
        if (rule.isEmpty()) {
            System.err.println("Device not found with ID : " + ruleId);
            return false;
        }

        boolean isAssociated = rule.get().getGroupEntity().equals(group.get());
        if (!isAssociated) {
            System.err.println("Rule with ID : " + ruleId + " is not associated to group with ID : " + groupId);
        }

        return isAssociated;
    }


}
