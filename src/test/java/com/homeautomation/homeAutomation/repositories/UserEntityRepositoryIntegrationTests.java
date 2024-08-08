package com.homeautomation.homeAutomation.repositories;

import com.homeautomation.homeAutomation.config.TestDataUtil;
import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import com.homeautomation.homeAutomation.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class UserEntityRepositoryIntegrationTests {

    private UserRepository underTest;

    @Autowired
    public UserEntityRepositoryIntegrationTests(UserRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    @Transactional
    public void testThatUserCanBeCreatedAndRecalled() {
        UserEntity userEntity = TestDataUtil.createTestUserEntityA();
        underTest.save(userEntity);
        Optional<UserEntity> result = underTest.findById(userEntity.getUser_Id());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userEntity);
    }

}
