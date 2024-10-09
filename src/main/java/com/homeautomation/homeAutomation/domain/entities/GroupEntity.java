package com.homeautomation.homeAutomation.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @ManyToOne
    @JoinColumn(name = "rule_id")
//    @JsonManagedReference
    private HomeAutomationRuleEntity rule;

    @ManyToOne
    @JoinColumn(name = "user_id")
//    @JsonBackReference
//    @JsonIgnore
    private UserEntity userEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupEntity that = (GroupEntity) o;

        if (!groupId.equals(that.groupId)) return false;
        if (!name.equals(that.name)) return false;
        if (!Objects.equals(rule, that.rule)) return false;
        return Objects.equals(userEntity, that.userEntity);
    }

    @Override
    public int hashCode() {
        int result = groupId.hashCode();
        result = 31 * result + name.hashCode();
        result = rule != null ? 31 * result + rule.hashCode() : 0;
        result = userEntity != null ? 31 * result + userEntity.hashCode() : 0;
        return result;
    }

}
