package com.homeautomation.homeAutomation.services;

import com.homeautomation.homeAutomation.domain.entities.BehaviourEntity;
import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;

import java.util.List;
import java.util.Optional;

public interface BehaviourService {

    BehaviourEntity save(BehaviourEntity behaviourEntity);

    BehaviourEntity saveUpdate(Long id, BehaviourEntity behaviourEntity);


    Iterable<BehaviourEntity> findAll();

    Optional<BehaviourEntity> findOne(Long id);

    boolean isExists(Long id);


    //not sure if necessary
//    String getBehaviourByDeviceID(DeviceEntity deviceEntity);

    List<BehaviourEntity> getBehavioursByRuleID(HomeAutomationRuleEntity homeAutomationRuleEntity);

    void delete(Long id);
}
