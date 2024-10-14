package com.homeautomation.homeAutomation.services;

import com.homeautomation.homeAutomation.domain.dto.DeviceDto;
import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;

import java.util.List;
import java.util.Optional;

public interface DeviceService {


    DeviceEntity save(
            DeviceEntity deviceEntity);

    DeviceEntity saveUpdate(
            Long id,
            DeviceEntity deviceEntity);


    Iterable<DeviceEntity> findAll();

    Optional<DeviceEntity> findById(Long id);

    List<DeviceEntity> findDevicesByUserId(Long userId);
    List<DeviceEntity> findDevicesByRuleId(Long ruleId);
    List<DeviceEntity> findDevicesByGroupId(Long groupId);

    boolean isExists(Long id);

//    List<DeviceEntity> getDevicesByGroupId(Long groupId);

    void delete(Long id);

    DeviceEntity partialUpdate(Long id, DeviceEntity deviceEntity);


    boolean isOwner(Long deviceId, String currentUsername);

    List<DeviceDto> getDevicesByUser(String currentUsername);
}
