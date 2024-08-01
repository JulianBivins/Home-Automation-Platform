package com.homeautomation.homeAutomation.mapper.impl;

import com.homeautomation.homeAutomation.domain.dto.DeviceDto;
import com.homeautomation.homeAutomation.domain.dto.HomeAutomationRuleDto;
import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import com.homeautomation.homeAutomation.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class HomeAutomationRuleMapperImpl implements Mapper<HomeAutomationRuleEntity, HomeAutomationRuleDto> {

    private ModelMapper modelMapper;

    public HomeAutomationRuleMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public HomeAutomationRuleDto mapTo(HomeAutomationRuleEntity homeAutomationRuleEntity) {
        return modelMapper.map(homeAutomationRuleEntity, HomeAutomationRuleDto.class);
    }

    @Override
    public HomeAutomationRuleEntity mapFrom(HomeAutomationRuleDto homeAutomationRuleDto) {
        return modelMapper.map(homeAutomationRuleDto, HomeAutomationRuleEntity.class);
    }
}
