package com.homeautomation.homeAutomation.repository;

import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeAutomationRuleRepository extends JpaRepository<HomeAutomationRuleEntity, Long> {

    boolean existsByBehaviourEntities_DeviceEntity_DeviceId(Long deviceId);
    boolean
//    isBehaviourExistsInRule
    existsByBehaviourEntities_BehaviourId
            (Long behaviourId);
    List<HomeAutomationRuleEntity> findByUserEntity_UserId(Long userId);
//    void deleteDeviceById(Long deviceId);
//    void deleteBehaviourByBehaviourId(Long behaviourId);

}