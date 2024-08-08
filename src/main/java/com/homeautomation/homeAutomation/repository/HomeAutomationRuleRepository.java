package com.homeautomation.homeAutomation.repository;

import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeAutomationRuleRepository extends JpaRepository<HomeAutomationRuleEntity, Long> {

    boolean existsByDeviceId(Long deviceId);
    boolean isBehaviourExistsInRule(Long behaviourId);
    List<HomeAutomationRuleEntity> findByUserId(Long userId);
    void deleteDeviceById(Long deviceId);
    void deleteBehaviourById(Long behaviourId);

}