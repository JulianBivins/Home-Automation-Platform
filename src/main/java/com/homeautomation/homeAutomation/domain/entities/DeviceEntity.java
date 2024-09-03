package com.homeautomation.homeAutomation.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
//@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name="devices")
public class DeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deviceId;

    // ex: Lights -> Living room
    private String name;

    public enum DeviceType {
            LIGHTS,
            //Washer, Fridge, Blinds...
            UTILITY,
            SPEAKER,
            TELEVISION,
            CAMERA,
    }

    @Enumerated(EnumType.STRING)
    private DeviceType type;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToMany(mappedBy = "deviceEntities", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<HomeAutomationRuleEntity> rules = new ArrayList<>();
}

