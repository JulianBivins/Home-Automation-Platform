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

    List<HomeAutomationRuleEntity> findByUserEntity_UserId(Long userId);
    Optional<HomeAutomationRuleEntity> findByRuleName(String ruleName);
    List<HomeAutomationRuleEntity> findByGroupEntities_GroupId(Long groupId);
}
