package com.homeautomation.homeAutomation.controller;

import com.homeautomation.homeAutomation.domain.dto.BehaviourDto;
import com.homeautomation.homeAutomation.domain.dto.DeviceDto;
import com.homeautomation.homeAutomation.domain.dto.HomeAutomationRuleDto;
import com.homeautomation.homeAutomation.domain.entities.BehaviourEntity;
import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import com.homeautomation.homeAutomation.mapper.impl.BehaviourMapperImpl;
import com.homeautomation.homeAutomation.mapper.impl.DeviceMapperImpl;
import com.homeautomation.homeAutomation.mapper.impl.HomeAutomationRuleMapperImpl;
import com.homeautomation.homeAutomation.services.BehaviourService;
import com.homeautomation.homeAutomation.services.HomeAutomationRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class BehaviourController {

	@Autowired
	private BehaviourService behaviourService;
	@Autowired
	private BehaviourMapperImpl behaviourMapper;
	@Autowired
	private HomeAutomationRuleService homeAutomationRuleService;
	@Autowired
	private HomeAutomationRuleMapperImpl homeAutomationRuleMapper;
	@Autowired
	private DeviceMapperImpl deviceMapper;


    @PostMapping("/behaviours")
    public ResponseEntity<BehaviourDto> createBehaviour (@PathVariable String behaviourString , @RequestBody HomeAutomationRuleDto homeAutomationRuleDto, @RequestBody DeviceDto deviceDto) {
		BehaviourDto.Behaviour behaviour = switch (behaviourString.toUpperCase()) {
			case "ON" -> BehaviourDto.Behaviour.ON;
			case "OFF" -> BehaviourDto.Behaviour.OFF;
			case "STAND_BY" -> BehaviourDto.Behaviour.STAND_BY;
			case "TIMED" -> BehaviourDto.Behaviour.TIMED;
			default -> throw new IllegalArgumentException("Invalid behaviourType: " + behaviourString);
		};
		HomeAutomationRuleEntity homeAutomationRuleEntity = homeAutomationRuleMapper.mapFrom(homeAutomationRuleDto);
		DeviceEntity deviceEntity = deviceMapper.mapFrom(deviceDto);
			BehaviourDto behaviourDto = new BehaviourDto(behaviour, homeAutomationRuleEntity, deviceEntity);
			return new ResponseEntity<>(behaviourDto, HttpStatus.OK);
	}


	@PutMapping("/behaviours/{behaviourId}")
	public ResponseEntity<BehaviourDto> updateFullBehaviour (@PathVariable Long behaviourId, @RequestBody BehaviourDto behaviourDto) {
        if(!behaviourService.isExists(behaviourId)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		behaviourDto.setBehaviourId(behaviourId);
		BehaviourEntity updatedBehaviourEntity = behaviourMapper.mapFrom(behaviourDto);
		BehaviourEntity savedUpdateBehaviour = behaviourService.saveUpdate(behaviourId, updatedBehaviourEntity);
		return new ResponseEntity<>(behaviourMapper.mapTo(savedUpdateBehaviour), HttpStatus.OK);
    }

	@PatchMapping("/behaviour/{behavioursId}")
	public ResponseEntity<BehaviourDto> partialUpdate(@PathVariable Long id, @RequestBody BehaviourDto behaviourDto) {
		if(!behaviourService.isExists(id)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		BehaviourEntity behaviourEntity = behaviourMapper.mapFrom(behaviourDto);
		BehaviourEntity updatedBehaviour = behaviourService.partialUpdate(id, behaviourEntity);
		return new ResponseEntity<>(
				behaviourMapper.mapTo(updatedBehaviour),
				HttpStatus.OK);
	}

	@DeleteMapping("/behaviours/rules/{behaviourId}")
	public ResponseEntity deleteBehaviour(@PathVariable Long id) {
		if(!homeAutomationRuleService.isBehaviourExistsInRule(id)){
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		//TODO should this delete out of homeAutomationService or the behaviour table? Will it cascade delete?
		homeAutomationRuleService.deleteBehaviourById(id);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}


	@GetMapping("/behaviours/{behaviourId}")
	public ResponseEntity<BehaviourDto> getBehaviourById (@PathVariable Long behaviourId) {
		Optional<BehaviourEntity> returnedBehaviourDto = behaviourService.findOne(behaviourId);
		return returnedBehaviourDto.map(behaviourEntity -> {
			BehaviourDto behaviourDto = behaviourMapper.mapTo(behaviourEntity);
			return new ResponseEntity<>(behaviourDto, HttpStatus.OK);
		}).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}



}
