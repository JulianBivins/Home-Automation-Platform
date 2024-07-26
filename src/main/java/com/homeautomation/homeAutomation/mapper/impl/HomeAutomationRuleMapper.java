package com.homeautomation.homeAutomation.mapper.impl;

import com.homeautomation.homeAutomation.domain.dto.HomeAutomationRuleDto;
import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import com.homeautomation.homeAutomation.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class HomeAutomationRuleMapper implements Mapper<HomeAutomationRuleEntity, HomeAutomationRuleDto> {
    @Override
    public HomeAutomationRuleDto mapTo(HomeAutomationRuleEntity homeAutomationRuleEntity) {
        return null;
    }

    @Override
    public HomeAutomationRuleEntity mapFrom(HomeAutomationRuleDto homeAutomationRuleDto) {
        return null;
    }
}
