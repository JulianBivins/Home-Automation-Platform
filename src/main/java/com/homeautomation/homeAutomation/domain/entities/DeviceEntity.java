package com.homeautomation.homeAutomation.domain.entities;

import com.homeautomation.homeAutomation.config.DeviceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="device")
public class DeviceEntity {

    @Id
    Long device_Id;

    // ex: Lights -> Living room
    String name;

    DeviceType type;
}
