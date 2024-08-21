package com.homeautomation.homeAutomation.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
//@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name="`groups`")
public class GroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    private String name;

    @OneToMany(mappedBy = "groupEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<HomeAutomationRuleEntity> rules
           = new ArrayList<>()
            ;


    //    I think this is might be unnecessary since the devices will be give over through the rules or the behavior table anyway
    @OneToMany(mappedBy = "groupEntity", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<DeviceEntity> devices
            = new ArrayList<>()
            ;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

}
