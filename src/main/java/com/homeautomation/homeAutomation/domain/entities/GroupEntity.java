package com.homeautomation.homeAutomation.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
//@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name="`Groups`")
public class GroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    private String name;

    @OneToMany(mappedBy = "groupEntity")
    private List<HomeAutomationRuleEntity> rules;

    //    I think this is might be unnecessary since the devices will be give over through the rules or the behavior table anyway
    @OneToMany(mappedBy = "groupEntity")
    private List<DeviceEntity> devices;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public GroupEntity(long l, String livingRoom, List<HomeAutomationRuleEntity> homeAutomationRuleEntities) {

    }

    public GroupEntity(String name, Long groupId, List<HomeAutomationRuleEntity> rules, List<DeviceEntity> devices, UserEntity userEntity) {
        this.name = name;
        this.groupId = groupId;
        this.rules = rules;
        this.devices = devices;
        this.userEntity = userEntity;
    }
}
