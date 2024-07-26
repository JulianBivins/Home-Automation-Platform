package com.homeautomation.homeAutomation.services.impl;


import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import com.homeautomation.homeAutomation.repository.HomeAutomationRuleRepository;
import com.homeautomation.homeAutomation.services.HomeAutomationRuleService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HomeAutomationRuleServiceImpl implements HomeAutomationRuleService {

    final private HomeAutomationRuleRepository homeAutomationRuleRepository;

    public HomeAutomationRuleServiceImpl(HomeAutomationRuleRepository homeAutomationRuleRepository) {
        this.homeAutomationRuleRepository = homeAutomationRuleRepository;
    }


    @Override
    public HomeAutomationRuleEntity saveUpdate(Long id, HomeAutomationRuleEntity homeAutomationRuleEntity) {
        homeAutomationRuleEntity.setRule_id(id);
        return homeAutomationRuleRepository.save(homeAutomationRuleEntity);
    }

    @Override
    public Iterable<HomeAutomationRuleEntity> findAll() {
        return homeAutomationRuleRepository.findAll();
    }

    @Override
    public Optional<HomeAutomationRuleEntity> findOne(Long id) {
        return Optional.empty();
    }

    @Override
    public boolean isExists(Long id) {
        return homeAutomationRuleRepository.existsById(id);
    }

    @Override
    public HomeAutomationRuleEntity partialUpdate(Long id, HomeAutomationRuleEntity homeAutomationRuleEntity) {
        return null;
    }

    @Override
    public void delete(Long id) {
        homeAutomationRuleRepository.deleteById(id);
    }
}
