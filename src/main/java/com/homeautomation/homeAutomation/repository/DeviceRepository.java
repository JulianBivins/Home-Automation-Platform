package com.homeautomation.homeAutomation.repository;

import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends CrudRepository<DeviceEntity, Long> {
}
