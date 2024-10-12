package com.homeautomation.homeAutomation.domain.dto;

import com.fasterxml.jackson.annotation.*;
import com.homeautomation.homeAutomation.config.ValidationGroups;
import com.homeautomation.homeAutomation.domain.entities.GroupEntity;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

//    @NotNull(message = "User ID cannot be null", groups = ValidationGroups.Update.class)
    Long userId;

//    @NotBlank(message = "Username is mandatory", groups = ValidationGroups.Create.class)
    String username;

    @JsonIgnore
//    @NotBlank(message = "Username is mandatory", groups = ValidationGroups.Create.class)
    String password;

//    @JsonManagedReference(value = "user-rules")
    private List<HomeAutomationRuleDto> rules = new ArrayList<>();

    private List<GroupDto> groups = new ArrayList<>();

//    @NotEmpty(message = "User must have at least one role", groups = ValidationGroups.Create.class)
    private Set<Roles> role = new HashSet<>();

    public enum Roles {
        USER,
        ADMIN
    }

    public UserDto(Long l, String userEntityB, List<HomeAutomationRuleDto> homeAutomationRuleEntities) {

    }
}
