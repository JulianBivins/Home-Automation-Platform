package com.homeautomation.homeAutomation.domain.dto;

import com.homeautomation.homeAutomation.domain.entities.BehaviourEntity;
import com.homeautomation.homeAutomation.domain.entities.GroupEntity;
import com.homeautomation.homeAutomation.domain.entities.HomeAutomationRuleEntity;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeAutomationRuleDto {

    private String rule_id;

    private String ruleName;

    private String description;

    //!Might need to be Dto
    private UserEntity userEntity;

    //!Might need to be Dto
    private GroupEntity groupEntity;

    private List<BehaviourEntity> behaviourEntities;

    public enum Event {
        AUTO, // aka Time sensitive
        BEFORE_OTHER,
        AFTER_OTHER

    }
    private Event trigger;

}
