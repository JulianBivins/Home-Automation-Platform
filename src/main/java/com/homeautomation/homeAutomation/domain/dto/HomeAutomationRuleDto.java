package com.homeautomation.homeAutomation.domain.dto;

import com.homeautomation.homeAutomation.domain.entities.BehaviourEntity;
import com.homeautomation.homeAutomation.domain.entities.GroupEntity;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeAutomationRuleDto {

    private Long ruleId;

    private String ruleName;

    private String description;

    //!Might need to be Dto
    private UserEntity userEntity;

    //!Might need to be Entity
    private GroupEntity groupEntity;

    private List<BehaviourEntity> behaviourEntitys;

    public enum Event {
        TIME,
        PERIOD,
        BEFORE_OTHER,
        AFTER_OTHER,
        WHEN_INSERT_ON,
        WHEN_INSERT_OFF
    }
    private Event event;

}
