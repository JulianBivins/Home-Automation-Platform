package com.homeautomation.homeAutomation.domain.entities;

import com.homeautomation.homeAutomation.domain.dto.BehaviourDto;
import com.homeautomation.homeAutomation.domain.dto.GroupDto;
import com.homeautomation.homeAutomation.domain.dto.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="home_automation_rules")
public class HomeAutomationRuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long rule_id;

    private String ruleName;

    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    //!Might need to be Dto
    private UserDto userDto;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id")
    //!Might need to be Dto
    private GroupDto groupDto;

    //I think this is unnecessary since the devices will be give over through the rules or the behavior table anyway
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
//    @JoinColumn(name = "device_id")
//    private List<DeviceEntity> device;

    @OneToMany(mappedBy = "homeAutomationRuleDto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BehaviourDto> behaviourDtos;

    public enum Event {
        TIME,
        PERIOD,
        BEFORE_OTHER,
        AFTER_OTHER,
        WHEN_INSERT_ON,
        WHEN_INSERT_OFF
    }
    private Event trigger;

}
