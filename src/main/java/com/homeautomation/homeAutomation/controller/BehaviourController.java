package com.homeautomation.homeAutomation.controller;

import com.homeautomation.homeAutomation.domain.dto.BehaviourDto;
import com.homeautomation.homeAutomation.domain.entities.BehaviourEntity;
import com.homeautomation.homeAutomation.mapper.impl.BehaviourMapperImpl;
import com.homeautomation.homeAutomation.mapper.impl.DeviceMapperImpl;
import com.homeautomation.homeAutomation.services.BehaviourService;
import com.homeautomation.homeAutomation.services.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BehaviourController {

	@Autowired
	private BehaviourService behaviourService;
	@Autowired
	private BehaviourMapperImpl behaviourMapper;

    @PostMapping("/behaviours")
    public ResponseEntity<BehaviourDto> createBehaviour (@PathVariable)


	@PutMapping("/behaviours/{behaviourId}")
	public ResponseEntity<BehaviourDto> updateBehaviour () {
        return null
    }
	@DeleteMapping("/behaviours/{behaviourId}")
	public ResponseEntity deleteBehaviour(@PathVariable Long id) {
		//right now deleted with or without actually checking if user exists.
		//should change implementation to include return type of boolean for self measures
		userService.delete(id);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	•	@GetMapping("/behaviours/{behaviourId}")
	•	Endpoint to retrieve a behaviour by its ID.
            •	@GetMapping("/devices/{deviceId}/behaviours")
	•	Endpoint to retrieve all behaviours for a specific device.
	•	@GetMapping("/rules/{ruleId}/behaviours")
	•	Endpoint to retrieve all behaviours for a specific rule.

}
