package com.homeautomation.homeAutomation.repository;

import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HomeAutomationRuleRepository extends JpaRepository<HomeAutomationRuleEntity, Long> {

//    boolean existsByBehaviourEntities_DeviceEntity_DeviceId(Long deviceId);
//    boolean
////    isBehaviourExistsInRule
//    existsByBehaviourEntities_BehaviourId
//            (Long behaviourId);
    List<HomeAutomationRuleEntity> findByUserEntity_UserId(Long userId);
//
//    @Query("SELECT r FROM HomeAutomationRuleEntity r JOIN r.groupEntities g WHERE g.groupId = :groupId")
//    List<HomeAutomationRuleEntity> findByGroupId(@Param("groupId") Long groupId);

    //    List<HomeAutomationRuleEntity> findByGroupEntities_GroupId(Long groupId);
//    List<HomeAutomationRuleEntity> findByGroupEntity_GroupId(Long groupId);

    Optional<HomeAutomationRuleEntity> findByRuleName(String ruleName);
//    void deleteDeviceById(Long deviceId);
//    void deleteBehaviourByBehaviourId(Long behaviourId);

}