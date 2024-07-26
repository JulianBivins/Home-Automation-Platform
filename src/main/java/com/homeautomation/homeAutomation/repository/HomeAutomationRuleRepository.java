package com.homeautomation.homeAutomation.repository;

import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import org.apache.catalina.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeAutomationRuleRepository extends CrudRepository<HomeAutomationRuleEntity, Long> {
}
