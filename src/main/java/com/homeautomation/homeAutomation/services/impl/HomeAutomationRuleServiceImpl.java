package com.homeautomation.homeAutomation.services.impl;


import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.repository.HomeAutomationRuleRepository;
import com.homeautomation.homeAutomation.services.HomeAutomationRuleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<HomeAutomationRuleEntity> findByGroupEntities_GroupId(Long groupId) {
        return homeAutomationRuleRepository.findByGroupEntities_GroupId(groupId);
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
            Optional.ofNullable(homeAutomationRuleEntity.getGroupEntities()).ifPresent(newGroups -> {
                existingRule.setGroupEntities(new ArrayList<>(newGroups));
            });
            Optional.ofNullable(homeAutomationRuleEntity.getBehaviourEntities()).ifPresent(newBehaviours -> {
                existingRule.setBehaviourEntities(new ArrayList<>(newBehaviours));
            });
            Optional.ofNullable(homeAutomationRuleEntity.getEvent()).ifPresent(existingRule::setEvent);
            return homeAutomationRuleRepository.save(existingRule);
        }).orElseThrow(() -> new RuntimeException("Rule does not exist"));
    }

    @Override
    public boolean isExists(Long id) {
        return homeAutomationRuleRepository.existsById(id);
    }

    @Override
    public boolean isDeviceExistsInRule(Long deviceId) {
        return homeAutomationRuleRepository.existsByBehaviourEntities_DeviceEntity_DeviceId(deviceId);
    }

    @Override
    public boolean isBehaviourExistsInRule(Long behaviourId) {
        return homeAutomationRuleRepository.existsByBehaviourEntities_BehaviourId(behaviourId);
    }


    @Override
    public void delete(Long id) {
        homeAutomationRuleRepository.deleteById(id);
    }

//    @Override
//    public void deleteDeviceById(Long deviceId) {
//        homeAutomationRuleRepository.deleteDeviceById(deviceId);
//    }
    @Override
    @Transactional
    public void removeDeviceFromRule(Long ruleId, Long deviceId) {
        HomeAutomationRuleEntity rule = homeAutomationRuleRepository.findById(ruleId)
                .orElseThrow(() -> new RuntimeException("Rule not found with id " + ruleId));

        rule.getBehaviourEntities().removeIf(behaviour ->
                behaviour.getDeviceEntity().getDeviceId().equals(deviceId));

        homeAutomationRuleRepository.save(rule);
    }


//    @Override
//    public void deleteBehaviourById(Long behaviourId) {
//        homeAutomationRuleRepository.deleteBehaviourByBehaviourId(behaviourId);
//    }

    @Override
    @Transactional
    public void removeBehaviourFromRule(Long ruleId, Long behaviourId) {
        HomeAutomationRuleEntity rule = homeAutomationRuleRepository.findById(ruleId)
                .orElseThrow(() -> new RuntimeException("Rule not found with id " + ruleId));
        rule.getBehaviourEntities().removeIf(behaviour -> behaviour.getBehaviourId().equals(behaviourId));

        homeAutomationRuleRepository.save(rule);
    }

}
