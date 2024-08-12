package com.homeautomation.homeAutomation.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
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
}
