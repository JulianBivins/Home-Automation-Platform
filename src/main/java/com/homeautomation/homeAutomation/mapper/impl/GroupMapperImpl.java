package com.homeautomation.homeAutomation.mapper.impl;

import com.homeautomation.homeAutomation.domain.dto.DeviceDto;
import com.homeautomation.homeAutomation.domain.dto.GroupDto;
import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.GroupEntity;
import com.homeautomation.homeAutomation.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class GroupMapperImpl implements Mapper<GroupEntity, GroupDto> {

    private ModelMapper modelMapper;

    public GroupMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public GroupDto mapTo(GroupEntity groupEntity) {
        return modelMapper.map(groupEntity, GroupDto.class);
    }

    @Override
    public GroupEntity mapFrom(GroupDto groupDto) {
        return modelMapper.map(groupDto, GroupEntity.class);
    }
}
