package com.homeautomation.homeAutomation.services;

import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface HomeAutomationRuleService {

    HomeAutomationRuleEntity save (HomeAutomationRuleEntity homeAutomationRuleEntity);

    HomeAutomationRuleEntity saveUpdate(Long id, HomeAutomationRuleEntity homeAutomationRuleEntity);

    Iterable<HomeAutomationRuleEntity> findAll();

    Optional<HomeAutomationRuleEntity> findOne(Long id);

    List<HomeAutomationRuleEntity> getRulesByUserId(UserEntity userEntity);


    HomeAutomationRuleEntity partialUpdate(Long id, HomeAutomationRuleEntity homeAutomationRuleEntity);

    boolean isExists(Long id);

    boolean isDeviceExistsInRule(Long deviceId);

    boolean isBehaviourExistsInRule(Long behaviourId);

    void delete(Long id);

    void deleteDeviceById(Long deviceId);

    void deleteBehaviourById(Long behaviourId);
}
