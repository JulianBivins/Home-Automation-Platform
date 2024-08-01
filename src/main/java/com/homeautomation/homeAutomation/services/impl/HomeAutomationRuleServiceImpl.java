package com.homeautomation.homeAutomation.services.impl;


import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.repository.HomeAutomationRuleRepository;
import com.homeautomation.homeAutomation.services.HomeAutomationRuleService;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HomeAutomationRuleServiceImpl implements HomeAutomationRuleService {

    final private HomeAutomationRuleRepository homeAutomationRuleRepository;

    public HomeAutomationRuleServiceImpl(HomeAutomationRuleRepository homeAutomationRuleRepository) {
        this.homeAutomationRuleRepository = homeAutomationRuleRepository;
    }


    @Override
    public HomeAutomationRuleEntity save(HomeAutomationRuleEntity homeAutomationRuleEntity) {
        return homeAutomationRuleRepository.save(homeAutomationRuleEntity);
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
        return homeAutomationRuleRepository.findById(id);
    }

    //TODO
    @Override
    public List<HomeAutomationRuleEntity> getRulesByUserID(UserEntity userEntity) {
        return homeAutomationRuleRepository.findAllById(userEntity.getUser_Id());
    }

    @Override
    public HomeAutomationRuleEntity partialUpdate(Long id, HomeAutomationRuleEntity homeAutomationRuleEntity) {
        homeAutomationRuleEntity.setRule_id(id);
        return homeAutomationRuleRepository.findById(id).map(existingRule -> {
            Optional.ofNullable(homeAutomationRuleEntity.getRuleName()).ifPresent(existingRule::setRuleName);
            Optional.ofNullable(homeAutomationRuleEntity.getDescription()).ifPresent(existingRule::setDescription);
            Optional.ofNullable(homeAutomationRuleEntity.getGroupEntity()).ifPresent(existingRule::setGroupEntity);
            Optional.ofNullable(homeAutomationRuleEntity.getBehaviourEntities()).ifPresent(existingRule::setBehaviourEntities);
            Optional.ofNullable(homeAutomationRuleEntity.getTrigger()).ifPresent(existingRule::setTrigger);
            return homeAutomationRuleRepository.save(existingRule);
        }).orElseThrow(() -> new RuntimeException("Rule does not exist"));
    }

    @Override
    public boolean isExists(Long id) {
        return homeAutomationRuleRepository.existsById(id);
    }


    @Override
    public void delete(Long id) {
        homeAutomationRuleRepository.deleteById(id);
    }
}
