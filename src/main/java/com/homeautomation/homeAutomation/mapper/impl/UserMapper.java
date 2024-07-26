package com.homeautomation.homeAutomation.mapper.impl;

import com.homeautomation.homeAutomation.domain.dto.UserDto;
import com.homeautomation.homeAutomation.domain.entities.DeviceEntity;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<UserEntity, UserDto> {
    @Override
    public UserDto mapTo(UserEntity userEntity) {
        return null;
    }

    @Override
    public UserEntity mapFrom(UserDto userDto) {
        return null;
    }
}
