package com.homeautomation.homeAutomation.repository;

import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<DeviceEntity, Long> {
List<DeviceEntity> findByUserEntity_UserId(Long userId);

    List<DeviceEntity> findByRules_RuleId(Long ruleId);

    List<DeviceEntity> findByRules_GroupEntities_GroupId(Long groupId);



}
