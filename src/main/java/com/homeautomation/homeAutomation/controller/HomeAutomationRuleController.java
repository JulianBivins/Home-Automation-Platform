package com.homeautomation.homeAutomation.controller;

import com.homeautomation.homeAutomation.domain.dto.HomeAutomationRuleDto;
import com.homeautomation.homeAutomation.domain.entities.BehaviourEntity;
import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import com.homeautomation.homeAutomation.mapper.Mapper;
import com.homeautomation.homeAutomation.services.BehaviourService;
import com.homeautomation.homeAutomation.services.HomeAutomationRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    @Autowired
    private BehaviourService behaviourService;

//    public HomeAutomationRuleController(HomeAutomationRuleService homeAutomationRuleService, Mapper<HomeAutomationRuleEntity, HomeAutomationRuleDto> homeAutomationRuleMapper) {
//        this.homeAutomationRuleService = homeAutomationRuleService;
//        this.homeAutomationRuleMapper = homeAutomationRuleMapper;
//    }


//TODO ALL
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


    @GetMapping("/rules/{ruleId}/behaviours")
    public ResponseEntity<ArrayList<String>> retrieveBehavioursByRule (@RequestBody HomeAutomationRuleDto homeAutomationRuleDto) {
        List<BehaviourEntity> behaviours = behaviourService.getBehavioursByRuleID(homeAutomationRuleDto);
        ArrayList<String> behaviourArray = new ArrayList<>();
        for(var behaviour : behaviours) {
            behaviourArray.add(String.valueOf(behaviour.getBehaviour()));
        }
        return new ResponseEntity<>(behaviourArray, HttpStatus.OK);
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

    //TODO
    @PatchMapping("rules/{device}")
    void addDevice (@RequestBody DeviceEntity deviceEntity){
        //Device id will be generated for the specific homeautomationrule

    }


    @DeleteMapping( "/rules/{ruleId}")
    public ResponseEntity deleteRule(@PathVariable Long id) {
         if(!homeAutomationRuleService.isExists(id)) {
             return new ResponseEntity(HttpStatus.NOT_FOUND);
         }
        homeAutomationRuleService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }



}
