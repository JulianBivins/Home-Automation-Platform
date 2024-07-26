package com.homeautomation.homeAutomation.controller;

import com.homeautomation.homeAutomation.domain.dto.HomeAutomationRuleDto;
import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import com.homeautomation.homeAutomation.mapper.Mapper;
import com.homeautomation.homeAutomation.services.HomeAutomationRuleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeAutomationRuleController {

    private final HomeAutomationRuleService homeAutomationRuleService;

    private final Mapper<HomeAutomationRuleEntity, HomeAutomationRuleDto> homeAutomationRuleMapper;

    public HomeAutomationRuleController(HomeAutomationRuleService homeAutomationRuleService, Mapper<HomeAutomationRuleEntity, HomeAutomationRuleDto> homeAutomationRuleMapper) {
        this.homeAutomationRuleService = homeAutomationRuleService;
        this.homeAutomationRuleMapper = homeAutomationRuleMapper;
    }

    @PostMapping("/rules{id}")
    public ResponseEntity<HomeAutomationRuleDto> createRule(
            @PathVariable Long id,
            @RequestBody HomeAutomationRuleDto homeAutomationRuleDto
    ) {
        HomeAutomationRuleEntity homeAutomationRuleEntity = homeAutomationRuleMapper.mapFrom(homeAutomationRuleDto);
        boolean ruleExists = homeAutomationRuleService.isExists(id);
        HomeAutomationRuleEntity savedHomeAutomationRuleEntity = homeAutomationRuleService.saveUpdate(id, homeAutomationRuleEntity);
        HomeAutomationRuleDto savedUpdatedHomeAutomationDto = homeAutomationRuleMapper.mapTo(savedHomeAutomationRuleEntity);
        if(ruleExists){
            return new ResponseEntity(savedUpdatedHomeAutomationDto, HttpStatus.OK);
        } else {
            return new ResponseEntity(savedUpdatedHomeAutomationDto, HttpStatus.CREATED);
        }
    }


}
