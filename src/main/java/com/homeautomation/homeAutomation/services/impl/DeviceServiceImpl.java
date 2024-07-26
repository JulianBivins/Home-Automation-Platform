package com.homeautomation.homeAutomation.services.impl;


import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import com.homeautomation.homeAutomation.repository.DeviceRepository;
import com.homeautomation.homeAutomation.services.DeviceService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeviceServiceImpl implements DeviceService {

    final private DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public DeviceEntity saveUpdate(Long id, DeviceEntity deviceEntity) {
        deviceEntity.setDevice_id(id);
        return deviceRepository.save(deviceEntity);
    }

    @Override
    public Iterable<DeviceEntity> findAll() {
        return deviceRepository.findAll();
    }


    @Override
    public Optional<DeviceEntity> findOne(Long id) {
        return Optional.empty();
    }

    @Override
    public boolean isExists(Long id) {
        return deviceRepository.existsById(id);
    }

    @Override
    public DeviceEntity partialUpdate(Long id, DeviceEntity deviceEntity) {
        return null;
    }

    @Override
    public void delete(Long id) {
        deviceRepository.deleteById(id);
    }
}
