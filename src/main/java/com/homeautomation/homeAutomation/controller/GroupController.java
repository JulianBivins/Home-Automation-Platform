package com.homeautomation.homeAutomation.controller;


import com.homeautomation.homeAutomation.config.ValidationGroups;
import com.homeautomation.homeAutomation.domain.dto.DeviceDto;
import com.homeautomation.homeAutomation.domain.dto.GroupDto;
import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.GroupEntity;
import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.mapper.Mapper;
import com.homeautomation.homeAutomation.services.GroupService;
import com.homeautomation.homeAutomation.services.HomeAutomationRuleService;
import com.homeautomation.homeAutomation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/groups")
@Validated
public class GroupController {


    @Autowired
    private GroupService groupService;
    @Autowired
    private Mapper<GroupEntity, GroupDto> groupMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private HomeAutomationRuleService ruleService;

    @PreAuthorize("@groupService.isOwner(#groupId, authentication.name)")
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupDto> getGroupById(@PathVariable Long groupId) {
        Optional<GroupEntity> returnedGroupEntity = groupService.findById(groupId);
        return returnedGroupEntity.map(deviceEntity -> {
            GroupDto groupDto = groupMapper.mapTo(deviceEntity);
            return new ResponseEntity<>(groupDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<GroupDto> createGroup( @Validated(ValidationGroups.Create.class) @RequestBody GroupDto groupDto, Authentication authentication) {
        String currentUsername = authentication.getName();
        UserEntity currentUser = userService.findByUsername(currentUsername).orElseThrow(() -> new RuntimeException("User not found"));

        GroupEntity groupEntity = groupMapper.mapFrom(groupDto);
        groupEntity.setUserEntity(currentUser);
        GroupEntity savedGroupEntity = groupService.save(groupEntity);
        return new ResponseEntity<>(groupMapper.mapTo(savedGroupEntity), HttpStatus.CREATED);
    }

    @PreAuthorize("@groupService.isOwner(#groupId, authentication.name)")
    @PatchMapping("/update/{groupId}")
    public ResponseEntity<GroupDto> partialUpdate(@PathVariable Long groupId,  @Validated(ValidationGroups.Update.class) @RequestBody GroupDto groupDto) {
        if(!groupService.isExists(groupId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        GroupEntity groupEntity = groupMapper.mapFrom(groupDto);
        GroupEntity updatedGroup = groupService.partialUpdate(groupId, groupEntity);
        return new ResponseEntity<>(
                groupMapper.mapTo(updatedGroup),
                HttpStatus.OK);
    }

    @PreAuthorize("@groupService.isOwner(#groupId, authentication.name)")
    @DeleteMapping("/delete/{groupId}")
    public ResponseEntity<Void> deleteGroup (@PathVariable Long groupId){
        if(!groupService.isExists(groupId)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        groupService.delete(groupId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("@groupService.isOwner(#groupId, #ruleId, authentication.name)")
    @PatchMapping("/{groupId}/addRule/{ruleId}")
    public ResponseEntity<GroupDto> addRuleToGroup(@PathVariable Long groupId, @PathVariable Long ruleId, Authentication authentication ) {

        GroupEntity retrievedDBGroup = groupService.findById(groupId).orElseThrow(() -> new RuntimeException("Group not Found"));

        HomeAutomationRuleEntity homeAutomationRuleEntity = ruleService.findById(ruleId).orElseThrow(() -> new RuntimeException("Rule not Found"));

        //just trying if the mistake is because of the other side not being persisted?
        homeAutomationRuleEntity.setGroupEntity(retrievedDBGroup);
        ruleService.partialUpdate(ruleId, homeAutomationRuleEntity);

        List<HomeAutomationRuleEntity> currentRuleEntities = retrievedDBGroup.getRuleEntities();
        List<HomeAutomationRuleEntity> ruleListToBeUpdated = new ArrayList<>(currentRuleEntities);
        ruleListToBeUpdated.add(homeAutomationRuleEntity);

        retrievedDBGroup.setRuleEntities(new ArrayList<>(ruleListToBeUpdated));

        if (!retrievedDBGroup.getRuleEntities().contains(homeAutomationRuleEntity)) throw new RuntimeException("The device could not be added");


        GroupEntity groupEntityUpdated = groupService.partialUpdate(groupId, retrievedDBGroup);
//        GroupEntity groupEntityUpdated = groupService.saveUpdate(groupId, retrievedDBGroup);

        GroupEntity groupEntityDBUpdated = groupService.findById(groupEntityUpdated.getGroupId()).orElseThrow(() -> new RuntimeException("Group not Found"));

        return new ResponseEntity<>(groupMapper.mapTo(groupEntityDBUpdated), HttpStatus.OK);
    }



}
