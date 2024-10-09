package com.homeautomation.homeAutomation.domain.dto;

import com.fasterxml.jackson.annotation.*;
import com.homeautomation.homeAutomation.domain.entities.GroupEntity;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId",    scope = UserDto.class)
public class UserDto {

    Long userId;

    String username;

    @JsonIgnore
    String password;

//    @JsonManagedReference(value = "user-rules")
    private List<HomeAutomationRuleDto> rules = new ArrayList<>();

    private List<GroupEntity> groups = new ArrayList<>();

    private Set<Roles> role = new HashSet<>();

    public enum Roles {
        USER,
        ADMIN
    }

    public UserDto(Long l, String userEntityB, List<HomeAutomationRuleDto> homeAutomationRuleEntities) {

    }
}
