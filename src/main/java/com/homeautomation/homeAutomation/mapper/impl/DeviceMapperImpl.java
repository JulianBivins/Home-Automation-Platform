package com.homeautomation.homeAutomation.mapper.impl;

import com.homeautomation.homeAutomation.domain.dto.DeviceDto;
import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
public class DeviceMapperImpl implements Mapper<DeviceEntity, DeviceDto> {

    private ModelMapper modelMapper;

//    public DeviceMapperImpl (ModelMapper modelMapper) {
//        this.modelMapper = modelMapper;
//    }

    public DeviceMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

        TypeMap<DeviceDto, DeviceEntity> typeMap = this.modelMapper.emptyTypeMap(DeviceDto.class, DeviceEntity.class);

        typeMap.addMappings(mapper -> mapper.skip(DeviceEntity::setUserEntity));

        typeMap.implicitMappings();
    }

    @Override
    public DeviceDto mapTo(DeviceEntity deviceEntity) {
        return modelMapper.map(deviceEntity, DeviceDto.class);
    }

    @Override
    public DeviceEntity mapFrom(DeviceDto deviceDto) {
        return modelMapper.map(deviceDto, DeviceEntity.class);
    }
}
