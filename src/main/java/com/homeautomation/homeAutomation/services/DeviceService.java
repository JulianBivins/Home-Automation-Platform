package com.homeautomation.homeAutomation.services;

import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;

import java.util.Optional;

public interface DeviceService {
    DeviceEntity saveUpdate(Long id, DeviceEntity deviceEntity);

    Iterable<DeviceEntity> findAll();

    Optional<DeviceEntity> findOne(Long id);

    boolean isExists(Long id);

    DeviceEntity partialUpdate(Long id, DeviceEntity deviceEntity);

    void delete(Long id);
}
