package com.homeautomation.homeAutomation.mapper.impl;

import com.homeautomation.homeAutomation.domain.dto.BehaviourDto;
import com.homeautomation.homeAutomation.domain.entities.BehaviourEntity;
import com.homeautomation.homeAutomation.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BehaviourMapperImpl implements Mapper<BehaviourEntity, BehaviourDto> {
    private ModelMapper modelMapper;

    public BehaviourMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    @Override
    public BehaviourDto mapTo(BehaviourEntity behaviourEntity) {
        return modelMapper.map(behaviourEntity, BehaviourDto.class);
    }


    @Override
    public BehaviourEntity mapFrom(BehaviourDto behaviourDto) {
        return modelMapper.map(behaviourDto, BehaviourEntity.class);
    }
}
