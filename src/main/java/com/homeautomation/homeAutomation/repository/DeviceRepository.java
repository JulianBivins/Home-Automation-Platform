package com.homeautomation.homeAutomation.repository;

import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<DeviceEntity, Long> {
//    List<DeviceEntity> findByGroupEntity_GroupId(Long userId);
}
