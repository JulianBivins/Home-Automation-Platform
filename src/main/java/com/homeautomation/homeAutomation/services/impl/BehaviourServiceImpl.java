package com.homeautomation.homeAutomation.services.impl;

import com.homeautomation.homeAutomation.domain.dto.DeviceDto;
import com.homeautomation.homeAutomation.domain.dto.HomeAutomationRuleDto;
import com.homeautomation.homeAutomation.domain.entities.BehaviourEntity;
import com.homeautomation.homeAutomation.repository.BehaviourRepository;
import com.homeautomation.homeAutomation.services.BehaviourService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BehaviourServiceImpl implements BehaviourService {

    private BehaviourRepository behaviourRepository;

    public BehaviourServiceImpl(BehaviourRepository behaviourRepository) {
        this.behaviourRepository = behaviourRepository;
    }


    @Override
    public BehaviourEntity save(BehaviourEntity behaviourEntity) {
        return behaviourRepository.save(behaviourEntity);
    }

    @Override
    public BehaviourEntity saveUpdate(Long id, BehaviourEntity behaviourEntity) {
        behaviourEntity.setBehaviourId(id);
        return behaviourRepository.save(behaviourEntity);
    }

    @Override
    public Iterable<BehaviourEntity> findAll() {
        return behaviourRepository.findAll();
    }

    @Override
    public Optional<BehaviourEntity> findOne(Long id) {
        return behaviourRepository.findById(id);
    }

    @Override
    public boolean isExists(Long id) {
        return behaviourRepository.existsById(id);
    }

    @Override
    public BehaviourEntity partialUpdate(Long id, BehaviourEntity behaviourEntity) {
        behaviourEntity.setBehaviourId(id);
        return behaviourRepository.findById(id).map(existingBehaviour -> {
            Optional.ofNullable(behaviourEntity.getBehaviour()).ifPresent(existingBehaviour::setBehaviour);
            return behaviourRepository.save(existingBehaviour);
        }).orElseThrow(() -> new RuntimeException("Behaviour does not exist"));
    }

    @Override
    public List<BehaviourEntity> getBehavioursByRuleID(HomeAutomationRuleDto homeAutomationRuleDto) {
        return behaviourRepository.findByHomeAutomationRuleEntity_RuleId(homeAutomationRuleDto.getRuleId());
    }

    @Override
    public List<BehaviourEntity> getBehavioursByDeviceID(DeviceDto deviceDto) {
        return behaviourRepository.findByDeviceEntity_DeviceId(deviceDto.getDeviceId());
    }

    @Override
    public void delete(Long id) {
        behaviourRepository.deleteById(id);
    }
}
