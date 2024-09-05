package com.homeautomation.homeAutomation.repository;

import com.homeautomation.homeAutomation.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Modifying
    @Query("DELETE FROM UserEntity u WHERE u.userId = :id")
    void deleteByIdCustom(@Param("id") Long id);
    Optional<UserEntity> findByUsername(String username);
}
