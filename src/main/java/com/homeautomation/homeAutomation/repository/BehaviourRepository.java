package com.homeautomation.homeAutomation.repository;

import com.homeautomation.homeAutomation.domain.entities.BehaviourEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BehaviourRepository extends JpaRepository<BehaviourEntity, Long> {
    List<BehaviourEntity> findByRuleId(Long ruleId);

    List<BehaviourEntity> findByDeviceId(Long deviceId);
}
