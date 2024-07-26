package com.homeautomation.homeAutomation.mapper.impl;

import com.homeautomation.homeAutomation.domain.dto.DeviceDto;
import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class DeviceMapper implements Mapper<DeviceEntity, DeviceDto> {
    @Override
    public DeviceDto mapTo(DeviceEntity deviceEntity) {
        return null;
    }

    @Override
    public DeviceEntity mapFrom(DeviceDto deviceDto) {
        return null;
    }
}
