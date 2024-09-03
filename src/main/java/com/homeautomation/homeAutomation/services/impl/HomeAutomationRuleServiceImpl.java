package com.homeautomation.homeAutomation.services.impl;


import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.repository.HomeAutomationRuleRepository;
import com.homeautomation.homeAutomation.services.HomeAutomationRuleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HomeAutomationRuleServiceImpl implements HomeAutomationRuleService {

    final private HomeAutomationRuleRepository homeAutomationRuleRepository;

    public HomeAutomationRuleServiceImpl(HomeAutomationRuleRepository homeAutomationRuleRepository) {
        this.homeAutomationRuleRepository = homeAutomationRuleRepository;
    }

    @Override
    public HomeAutomationRuleEntity save(HomeAutomationRuleEntity homeAutomationRuleEntity) {
        return homeAutomationRuleRepository.save(homeAutomationRuleEntity);
    }

    @Override
    public HomeAutomationRuleEntity saveUpdate(Long id, HomeAutomationRuleEntity homeAutomationRuleEntity) {
        homeAutomationRuleEntity.setRuleId(id);
        return homeAutomationRuleRepository.save(homeAutomationRuleEntity);
    }

    @Override
    public Iterable<HomeAutomationRuleEntity> findAll() {
        return homeAutomationRuleRepository.findAll();
    }

    @Override
    public Optional<HomeAutomationRuleEntity> findOne(Long id) {
        return homeAutomationRuleRepository.findById(id);
    }

    @Override
    public List<HomeAutomationRuleEntity> getRulesByUserId(UserEntity userEntity) {
        return homeAutomationRuleRepository.findByUserEntity_UserId(userEntity.getUserId());
    }

    @Override
    public Optional<HomeAutomationRuleEntity> findByRuleName(String ruleName) {
        return homeAutomationRuleRepository.findByRuleName(ruleName);
    }


    @Override
    public HomeAutomationRuleEntity partialUpdate(Long id, HomeAutomationRuleEntity homeAutomationRuleEntity) {
        homeAutomationRuleEntity.setRuleId(id);
        return homeAutomationRuleRepository.findById(id).map(existingRule -> {
            Optional.ofNullable(homeAutomationRuleEntity.getRuleName()).ifPresent(existingRule::setRuleName);
            Optional.ofNullable(homeAutomationRuleEntity.getDescription()).ifPresent(existingRule::setDescription);
            Optional.ofNullable(homeAutomationRuleEntity.getGroupEntities()).ifPresent(newGroups -> existingRule.setGroupEntities(new ArrayList<>(newGroups)));
            return homeAutomationRuleRepository.save(existingRule);
        }).orElseThrow(() -> new RuntimeException("Rule does not exist"));
    }

//    @Override
//    public HomeAutomationRuleEntity partialUpdate(Long id, HomeAutomationRuleEntity homeAutomationRuleEntity) {
//        // Find the existing rule by ID
//        return homeAutomationRuleRepository.findById(id).map(existingRule -> {
//            // Update the basic fields only if they are provided in the current entity
//            Optional.ofNullable(homeAutomationRuleEntity.getRuleName())
//                    .ifPresent(existingRule::setRuleName);
//
//            Optional.ofNullable(homeAutomationRuleEntity.getDescription())
//                    .ifPresent(existingRule::setDescription);
//
//            // Update group entities if provided; create a new mutable list to avoid issues with immutability
//            Optional.ofNullable(homeAutomationRuleEntity.getGroupEntities())
//                    .ifPresent(newGroups -> existingRule.setGroupEntities(new ArrayList<>(newGroups)));
//            Optional.ofNullable(homeAutomationRuleEntity.getEvent()).ifPresent(existingRule::setEvent);
//
//            // Save the updated existing rule entity
//            return homeAutomationRuleRepository.save(existingRule);
//        }).orElseThrow(() -> new RuntimeException("Rule does not exist"));
//    }



    @Override
    public boolean isExists(Long id) {
        return homeAutomationRuleRepository.existsById(id);
    }

    @Override
    public void delete(Long id) {
        homeAutomationRuleRepository.deleteById(id);
    }

    @Override
    public void removeDeviceFromRule(Long ruleId, Long deviceId) {
        Optional<HomeAutomationRuleEntity> ruleOptional = homeAutomationRuleRepository.findById(ruleId);

        if (ruleOptional.isPresent()) {
            HomeAutomationRuleEntity rule = ruleOptional.get();

            rule.getDeviceEntities().removeIf(device -> device.getDeviceId().equals(deviceId));

            homeAutomationRuleRepository.save(rule);
        } else {
            throw new RuntimeException("Rule not found with id: " + ruleId);
        }
    }

//    @Override
//    public List<HomeAutomationRuleEntity> findByGroupEntities_GroupId(Long groupId) {
//        return homeAutomationRuleRepository.findByGroupEntities_GroupId(groupId);
//    }




}
