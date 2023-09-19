package com.zero.triptalk.user.repository;

import com.zero.triptalk.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByEmail(String username);

    Optional<UserEntity> findByNickname(String nickname);
}