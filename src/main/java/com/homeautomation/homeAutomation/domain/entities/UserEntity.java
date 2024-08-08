package com.homeautomation.homeAutomation.domain.entities;

import com.homeautomation.homeAutomation.domain.dto.HomeAutomationRuleDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long user_Id;

    String username;

    String password;

    @OneToMany(mappedBy = "userEntity")
    private List<HomeAutomationRuleDto> rules;
}
