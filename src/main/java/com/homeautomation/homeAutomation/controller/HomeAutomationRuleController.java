package com.homeautomation.homeAutomation.controller;

import com.homeautomation.homeAutomation.domain.dto.HomeAutomationRuleDto;
import com.homeautomation.homeAutomation.domain.dto.UserDto;
import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.mapper.Mapper;
import com.homeautomation.homeAutomation.services.HomeAutomationRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class HomeAutomationRuleController {

    @Autowired
    private  HomeAutomationRuleService homeAutomationRuleService;

    @Autowired
    private  Mapper<HomeAutomationRuleEntity, HomeAutomationRuleDto> homeAutomationRuleMapper;

//    public HomeAutomationRuleController(HomeAutomationRuleService homeAutomationRuleService, Mapper<HomeAutomationRuleEntity, HomeAutomationRuleDto> homeAutomationRuleMapper) {
//        this.homeAutomationRuleService = homeAutomationRuleService;
//        this.homeAutomationRuleMapper = homeAutomationRuleMapper;
//    }



    //need to check this solution

     @GetMapping("rules/{ruleId}")
        public ResponseEntity<HomeAutomationRuleDto> getRuleById (@PathVariable Long ruleId) {
        //Every I rule id should be uniquely tied to the user. user1  rule id 1 different from user2 rule2 rule id 1
         //need to first validate which rule belongs to which user and then give out appropriate response
            Optional<HomeAutomationRuleEntity> returnedRule = homeAutomationRuleService.findOne(ruleId);
            return returnedRule.map(ruleEntity -> {
                HomeAutomationRuleDto homeAutomationRuleDto = homeAutomationRuleMapper.mapTo(ruleEntity);
                return new ResponseEntity<>(homeAutomationRuleDto, HttpStatus.OK);
            }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }

    @GetMapping("/rules/list")
    public List<HomeAutomationRuleDto> listHomeAutomationRules() {
        //need to make sure only rules tied to specific user is being spit out. Need to test. Don't know if the Jpa annotations solve this problem for me yet.
        //need to first validate which rule belongs to which user and then give out appropriate response
        Iterable<HomeAutomationRuleEntity> rules = homeAutomationRuleService.findAll();
        return StreamSupport.stream(
                rules.spliterator(), false)
                .map(homeAutomationRuleMapper::mapTo)
                .collect(Collectors.toList());
    }


    @PostMapping("rules/{ruleId}")
    public ResponseEntity<HomeAutomationRuleDto> createUpdateFullRule (@PathVariable Long ruleId, @RequestBody HomeAutomationRuleDto homeAutomationRuleDto) {

        HomeAutomationRuleEntity homeAutomationRuleEntity = homeAutomationRuleMapper.mapFrom(homeAutomationRuleDto);
        boolean ruleExists = homeAutomationRuleService.isExists(ruleId);
        HomeAutomationRuleEntity savedHomeAutomationRule = homeAutomationRuleService.saveUpdate(ruleId, homeAutomationRuleEntity);
        HomeAutomationRuleDto saedUpdatedHomeAutomationRuleDto = homeAutomationRuleMapper.mapTo(savedHomeAutomationRule);

        if(ruleExists){
            return new ResponseEntity(saedUpdatedHomeAutomationRuleDto, HttpStatus.OK);
        } else {
            return new ResponseEntity(saedUpdatedHomeAutomationRuleDto, HttpStatus.CREATED);
        }

    }

    @PatchMapping("rules/{device}")
    void addDevice (@RequestBody DeviceEntity deviceEntity){
        //Device id will be generated for the specific homeautomationrule

    }


    @DeleteMapping( "/users/{userId}")
    public ResponseEntity deleteRule(@PathVariable Long id) {
        //right now deleted with or without actually checking if rule exists.
        //should change implementation to include return type of boolean for self measures and to see if it was successful
        homeAutomationRuleService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }



}
