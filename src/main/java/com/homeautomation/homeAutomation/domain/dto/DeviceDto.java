package com.homeautomation.homeAutomation.domain.dto;

import com.homeautomation.homeAutomation.config.DeviceType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDto {


    Long device_Id;

    // ex: Lights -> Living room
    String name;

    DeviceType type;
}
