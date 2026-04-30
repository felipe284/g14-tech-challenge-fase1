package com.fiap_g14.foodlink.api.repository;

import com.fiap_g14.foodlink.api.domain.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID>, JpaSpecificationExecutor<UserEntity> {
    @Query("SELECT u FROM UserEntity u WHERE u.email = :email OR u.login = :login")
    Optional<UserEntity> findByEmailAndLogin(@Param("email") String email, @Param("login") String login);
    Optional<UserEntity> findByLogin(String login);
    Boolean existsByEmail(String email);
    Boolean existsByLogin(String login);
}


