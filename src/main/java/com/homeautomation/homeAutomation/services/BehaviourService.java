//package com.homeautomation.homeAutomation.services;
//
//import com.homeautomation.homeAutomation.domain.dto.DeviceDto;
//import com.homeautomation.homeAutomation.domain.dto.HomeAutomationRuleDto;
//import com.homeautomation.homeAutomation.domain.entities.BehaviourEntity;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface BehaviourService {
//
//    BehaviourEntity save(BehaviourEntity behaviourEntity);
//
//    BehaviourEntity saveUpdate(Long id, BehaviourEntity behaviourEntity);
//
//    Iterable<BehaviourEntity> findAll();
//
//    Optional<BehaviourEntity> findOne(Long id);
//
//    boolean isExists(Long id);
//
//    BehaviourEntity partialUpdate(Long id, BehaviourEntity behaviourEntity);
//
//    List<BehaviourEntity> getBehavioursByDeviceID(DeviceDto deviceDto);
//
//    List<BehaviourEntity> getBehavioursByRuleID(HomeAutomationRuleDto homeAutomationRuleDto);
//
//    void delete(Long id);
//}
