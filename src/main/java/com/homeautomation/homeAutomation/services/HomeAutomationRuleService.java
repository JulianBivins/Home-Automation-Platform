package com.homeautomation.homeAutomation.services;

import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;

import java.util.Optional;

public interface HomeAutomationRuleService {
    HomeAutomationRuleEntity saveUpdate(Long id, HomeAutomationRuleEntity homeAutomationRuleEntity);

    Iterable<HomeAutomationRuleEntity> findAll();

    Optional<HomeAutomationRuleEntity> findOne(Long id);

    boolean isExists(Long id);

    HomeAutomationRuleEntity partialUpdate(Long id, HomeAutomationRuleEntity homeAutomationRuleEntity);

    void delete(Long id);
}
